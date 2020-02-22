package cn.hkxj.platform.service;

import cn.hkxj.platform.dao.StudentDao;
import cn.hkxj.platform.exceptions.PasswordUnCorrectException;
import cn.hkxj.platform.exceptions.UrpException;
import cn.hkxj.platform.pojo.Classes;
import cn.hkxj.platform.pojo.Course;
import cn.hkxj.platform.pojo.Student;
import cn.hkxj.platform.pojo.StudentUser;
import cn.hkxj.platform.spider.NewUrpSpider;
import cn.hkxj.platform.spider.model.UrpStudentInfo;
import cn.hkxj.platform.spider.newmodel.SearchResult;
import cn.hkxj.platform.spider.newmodel.course.UrpCourseForSpider;
import cn.hkxj.platform.spider.newmodel.coursetimetable.UrpCourseTimeTableForSpider;
import cn.hkxj.platform.spider.newmodel.evaluation.EvaluationPagePost;
import cn.hkxj.platform.spider.newmodel.evaluation.EvaluationPost;
import cn.hkxj.platform.spider.newmodel.evaluation.searchresult.TeachingEvaluation;
import cn.hkxj.platform.spider.newmodel.examtime.UrpExamTime;
import cn.hkxj.platform.spider.newmodel.grade.detail.GradeDetailSearchPost;
import cn.hkxj.platform.spider.newmodel.grade.detail.UrpGradeDetailForSpider;
import cn.hkxj.platform.spider.newmodel.grade.general.UrpGeneralGradeForSpider;
import cn.hkxj.platform.spider.newmodel.grade.scheme.Scheme;
import cn.hkxj.platform.spider.newmodel.searchclass.ClassInfoSearchResult;
import cn.hkxj.platform.spider.newmodel.searchclass.CourseTimetableSearchResult;
import cn.hkxj.platform.spider.newmodel.searchclass.SearchClassInfoPost;
import cn.hkxj.platform.spider.newmodel.searchclassroom.SearchClassroomPost;
import cn.hkxj.platform.spider.newmodel.searchclassroom.SearchClassroomResult;
import cn.hkxj.platform.spider.newmodel.searchclassroom.SearchResultWrapper;
import cn.hkxj.platform.spider.newmodel.searchcourse.SearchCoursePost;
import cn.hkxj.platform.spider.newmodel.searchcourse.SearchCourseResult;
import cn.hkxj.platform.spider.newmodel.searchteacher.SearchTeacherPost;
import cn.hkxj.platform.spider.newmodel.searchteacher.SearchTeacherResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static cn.hkxj.platform.utils.DESUtil.encrypt;

/**
 * 第一次登录成功后，将学号对应session的cookie持久化
 * 如果需要不使用验证码登录，使用之前需要校验该账号是否有可用的cookie
 *
 * @author junrong.chen
 * @date 2019/7/18
 */
@Slf4j
@Service
public class NewUrpSpiderService {
    @Resource
    private ClassService classService;
    @Resource
    private StudentDao studentDao;
    @Resource
    private OpenIdService openIdService;
    @Value("${student.password.salt}")
    private String key;

    /**
     * 这个方法只有基本得成绩信息  包括相信成绩信息的抓取使用{@see #getCurrentTermGrade()}
     */
    @Retryable(value = UrpException.class, maxAttempts = 3)
    List<UrpGeneralGradeForSpider> getCurrentGeneralGrade(StudentUser student) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
        return spider.getCurrentGeneralGrade();
    }

    UrpCourseForSpider getCourseFromSpider(String account, String password, String uid) {
        NewUrpSpider spider = getSpider(account, password);
        return spider.getUrpCourse(uid);
    }


    UrpCourseForSpider getCourseFromSpider(Student student, String uid) {
        return getCourseFromSpider(student.getAccount().toString(), student.getPassword(), uid);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<SearchResult<ClassInfoSearchResult>> getClassInfoSearchResult(SearchClassInfoPost searchClassInfoPost) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.getClassInfoSearchResult(searchClassInfoPost);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<List<CourseTimetableSearchResult>> searchClassTimeTable(String termYear, int termOrder, String classCode) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.getUrpCourseTimeTableByClassCode(termYear, termOrder, classCode);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<SearchResult<SearchTeacherResult>> searchTeacherInfo(SearchTeacherPost searchTeacherPost) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.searchTeacherInfo(searchTeacherPost);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<List<CourseTimetableSearchResult>> searchCourseTimetableByTeacher(String teacherNumber) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.getUrpCourseTimeTableByTeacherAccount(teacherNumber);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<SearchResultWrapper<SearchClassroomResult>> searchClassroomInfo(SearchClassroomPost searchClassroomPost) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.searchClassroomInfo(searchClassroomPost);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public SearchResult<SearchCourseResult> searchCourseInfo(SearchCoursePost searchCoursePost) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.searchCourseInfo(searchCoursePost);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<List<CourseTimetableSearchResult>> searchCourseTimeTable(Course course) {
        Student student = studentDao.selectStudentByAccount(2014025838);
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.getUrpCourseTimeTableByCourse(course);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public void checkStudentPassword(String account, String password) {
        new NewUrpSpider(account, password);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public UrpCourseTimeTableForSpider getUrpCourseTimeTable(StudentUser student) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
        return spider.getUrpCourseTimeTable();
    }


    @Retryable(value = UrpException.class, maxAttempts = 3)
    public TeachingEvaluation searchTeachingEvaluationInfo(StudentUser student) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
        return spider.searchTeachingEvaluationInfo();
    }


    @Retryable(value = UrpException.class, maxAttempts = 3)
    public void evaluate(StudentUser student, EvaluationPost evaluationPost) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
        spider.evaluation(evaluationPost);
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public String getEvaluationToken(StudentUser student, EvaluationPagePost evaluationPagePost) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
        return spider.getEvaluationToken(evaluationPagePost);
    }

    /**
     * 获取学生信息
     */
    @Retryable(value = UrpException.class, maxAttempts = 3)
    public Student getStudentInfo(String account, String password) {
        NewUrpSpider spider = getSpider(account, password);

        return getUserInfo(spider.getStudentInfo());
    }


    /**
     * 获取学生信息
     */
    @Retryable(value = UrpException.class, maxAttempts = 3)
    public StudentUser getStudentUserInfo(String account, String password) {
        NewUrpSpider spider = getSpider(account, password);
        UrpStudentInfo urpStudentInfo = spider.getStudentInfo();

        StudentUser studentUser = new StudentUser();
        //首先将student原有字段赋给studentUser
        studentUser.setAccount(urpStudentInfo.getAccount());
        studentUser.setPassword(encrypt(urpStudentInfo.getPassword(), account + key));
        studentUser.setName(urpStudentInfo.getName());
        studentUser.setSex(urpStudentInfo.getSex());
        studentUser.setEthnic(urpStudentInfo.getEthnic());
        studentUser.setIsCorrect(true);

        studentUser.setAcademyName(urpStudentInfo.getAcademy());
        studentUser.setSubjectName(urpStudentInfo.getMajor());
        studentUser.setClassName(urpStudentInfo.getClassname());
        return studentUser;
    }


    /**
     * 考试安排
     *
     * @return
     */
    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<UrpExamTime> getExamTime(String account, String password) {
        NewUrpSpider spider = getSpider(account, password);

        return spider.getExamTime();
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<UrpExamTime> getExamTime(StudentUser student) {
        return getExamTime(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
    }

    @Retryable(value = UrpException.class, maxAttempts = 3)
    public UrpGradeDetailForSpider getGradeDetail(Student student, GradeDetailSearchPost gradeDetailSearchPost) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getPassword());
        return spider.getUrpGradeDetail(gradeDetailSearchPost);
    }


    @Retryable(value = UrpException.class, maxAttempts = 3)
    public List<Scheme> getSchemeGrade(StudentUser student) {
        NewUrpSpider spider = getSpider(student.getAccount().toString(), student.getEnablePassword(student.getAccount().toString()+key));
        return spider.getSchemeGrade();
    }


    private Student getUserInfo(UrpStudentInfo studentInfo) {

        Classes classes = classService.parseSpiderResult(studentInfo);
        Student student = wrapperToStudent(studentInfo);
        student.setClasses(classes.getId());

        return student;
    }

    private NewUrpSpider getSpider(String account, String password) {
        try {
            return new NewUrpSpider(account, password);
        } catch (PasswordUnCorrectException e) {
            studentDao.updatePasswordUnCorrect(Integer.parseInt(account));
            openIdService.openIdUnbindAllPlatform(Integer.parseInt(account));
            throw e;
        }

    }

    private Student wrapperToStudent(UrpStudentInfo studentWrapper) {
        Student student = new Student();
        student.setAccount(studentWrapper.getAccount());
        student.setPassword(studentWrapper.getPassword());
        student.setEthnic(studentWrapper.getEthnic());
        student.setSex(studentWrapper.getSex());
        student.setName(studentWrapper.getName());
        student.setIsCorrect(true);
        return student;
    }
}
