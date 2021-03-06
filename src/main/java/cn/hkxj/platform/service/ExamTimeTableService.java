package cn.hkxj.platform.service;

import cn.hkxj.platform.dao.ExamTimetableDao;
import cn.hkxj.platform.dao.StudentExamTimeTableDao;
import cn.hkxj.platform.dao.StudentUserDao;
import cn.hkxj.platform.exceptions.PasswordUnCorrectException;
import cn.hkxj.platform.exceptions.UrpRequestException;
import cn.hkxj.platform.pojo.*;
import cn.hkxj.platform.spider.newmodel.examtime.UrpExamTime;
import cn.hkxj.platform.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author junrong.chen
 * @date 2018/11/28
 */
@Slf4j
@Service
public class ExamTimeTableService {
    @Resource
    private NewUrpSpiderService newUrpSpiderService;
    @Resource
    private UrpCourseService urpCourseService;
    @Resource
    private ExamTimetableDao examTimetableDao;
    @Resource
    private StudentExamTimeTableDao studentExamTimeTableDao;
    @Resource
    private RoomService roomService;
    @Resource
    private StudentUserDao studentUserDao;


    public List<Exam> getExamTimeListFromSpider(int account) {
        StudentUser student = studentUserDao.selectStudentByAccount(account);
        if (student == null) {
            throw new PasswordUnCorrectException();
        }
        List<UrpExamTime> examTime;
        try {
            examTime = newUrpSpiderService.getExamTime(student);
        } catch (UrpRequestException e) {

            if (e.getCode() >= 500) {
                return Collections.emptyList();
            }
            throw e;
        }


        return examTime.stream()
                .filter(x -> StringUtils.isNotEmpty(x.getDate()))
                .map(x -> {
                            if (StringUtils.isEmpty(x.getExamTime())) {
                                return new Exam()
                                        .setCourse(getCourseFromExamText(x.getCourseName()))
                                        .setDate(new Date())
                                        .setExamName(x.getExamName())
                                        .setClassRoom(new UrpClassroom());

                            }

                            String[] timeSplit = x.getExamTime().split("-");
                            return new Exam()
                                    .setCourse(getCourseFromExamText(x.getCourseName()))
                                    .setDate(DateUtils.localDateToDate(x.getDate(), DateUtils.DEFAULT_PATTERN))
                                    .setExamName(x.getExamName())
                                    .setStartTime(timeSplit[0])
                                    .setEndTime(timeSplit[1])
                                    .setClassRoom(getClassRoomFromText(x.getLocation()))
                                    .setExamDay(x.getWeek())
                                    .setExamWeekOfTerm(x.getWeekOfTerm());
                        }

                )
                .collect(Collectors.toList());
    }


    /**
     * 由于这个接口非常耗时，而且请求量非常高，所以不再每次都从抓取结果，每天更新一次
     *
     * @param account
     * @return
     */
    public List<Exam> getExamTimeList(int account) {

        List<ExamTimetable> currentTermExam = examTimetableDao.selectCurrentExamByAccount(Integer.toString(account));


        if (currentTermExam.isEmpty()) {
            List<Exam> examList = getExamTimeListFromSpider(account);
            SchoolTime schoolTime = DateUtils.getCurrentSchoolTime();

            List<ExamTimetable> list = examList.stream()
                    .map(x -> new ExamTimetable()
                            .setName(x.getExamName())
                            .setCourseNum(x.getCourse().getNum())
                            .setCourseOrder(x.getCourse().getCourseOrder())
                            .setRoomName(x.getClassRoom().getName())
                            .setStartTime(x.getStartTime())
                            .setEndTime(x.getEndTime())
                            .setExamDate(x.getDate())
                            .setDay(x.getExamDay())
                            .setTermYear(schoolTime.getTerm().getTermYear())
                            .setTermOrder(schoolTime.getTerm().getOrder())
                            .setSchoolWek(x.getExamWeekOfTerm()))
                    .collect(Collectors.toList());
            try {
//                saveExamTimeTable(account, list);
            } catch (Exception e) {
                log.error("save exam timetable error", e);
            }
            return examList;

        } else {
            Stream<ExamTimetable> stream = currentTermExam.stream();


            return stream
                    .map(x -> {
                        Exam exam = new Exam()
                                .setDate(x.getExamDate())
                                .setExamName(x.getName())
                                .setStartTime(x.getStartTime())
                                .setEndTime(x.getEndTime())
                                .setClassRoom(roomService.getClassRoomByName(x.getRoomName()))
                                .setExamDay(x.getDay())
                                .setExamWeekOfTerm(x.getSchoolWek());

                        if (exam.getExamName().contains("开学补考")){
                            Course course = urpCourseService.getCourse(x.getCourseNum(), x.getCourseNum(), "2019-2020", 1, null);
                            exam.setCourse(course);
                        }else {

                            Course course = urpCourseService.getCurrentTermCourse(x.getCourseNum(), x.getCourseOrder(),
                                    new Course().setCourseOrder(x.getCourseOrder()));
                            exam.setCourse(course);
                        }

                        return exam;
                    })
                    .collect(Collectors.toList());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveExamTimeTable(int account, List<ExamTimetable> examTimetableList) {
        examTimetableList.stream()
                .map(examTimetable -> {
                    Integer id;
                    List<ExamTimetable> pojoList = examTimetableDao.selectByPojo(examTimetable);
                    if (pojoList.isEmpty()) {
                        examTimetableDao.insertSelective(examTimetable);
                        id = examTimetable.getId();
                    } else {
                        id = pojoList.get(0).getId();
                    }

                    return new StudentExamTimetable()
                            .setAccount(Integer.toString(account))
                            .setExamTimetableId(id)
                            .setTermOrder(examTimetable.getTermOrder())
                            .setTermYear(examTimetable.getTermYear());

                }).forEach(x -> studentExamTimeTableDao.insertSelective(x));


    }

    private Course getCourseFromExamText(String examText) {

        String pattern = "（(.*?)-(.*?)）(.*)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(examText);

        if (m.find()) {
            String num = m.group(1);
            String order = m.group(2);
            String courseName = m.group(3);
            Course course = new Course().setCourseOrder(order).setName(courseName).setNum(num);
            if (course == null) {
                log.error("can not find course {} {}", num, order);
            }

            return course;

        } else {
            log.error("can not parse exam text {}", examText);
            return new Course();
        }

    }

    private UrpClassroom getClassRoomFromText(String date) {
        String[] split = date.split(" ");
        UrpClassroom room = roomService.getClassRoomByName(split[3]);
        if (room != null) {
            return room;
        }
        return new UrpClassroom();
    }


}
