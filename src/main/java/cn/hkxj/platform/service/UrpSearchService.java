package cn.hkxj.platform.service;

import cn.hkxj.platform.dao.ClassCourseTimetableDao;
import cn.hkxj.platform.dao.CourseTimeTableDao;
import cn.hkxj.platform.pojo.*;
import cn.hkxj.platform.spider.newmodel.searchclass.ClassInfoSearchResult;
import cn.hkxj.platform.spider.newmodel.searchclass.CourseTimetableSearchResult;
import cn.hkxj.platform.spider.newmodel.searchclass.SearchClassInfoPost;
import cn.hkxj.platform.spider.newmodel.searchclassroom.SearchClassroomPost;
import cn.hkxj.platform.spider.newmodel.searchclassroom.SearchClassroomResult;
import cn.hkxj.platform.spider.newmodel.searchclassroom.SearchResultWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrpSearchService {
    @Resource
    private NewUrpSpiderService newUrpSpiderService;
    @Resource
    private CourseTimeTableDao courseTimeTableDao;
    @Resource
    private ClassCourseTimetableDao classCourseTimetableDao;


    public List<CourseTimetableSearchResult> searchTimetableByCourse(Course course) {
        for (List<CourseTimetableSearchResult> resultList : newUrpSpiderService.searchCourseTimeTable(course)) {
            return resultList;
        }
        return Collections.emptyList();
    }

    public List<CourseTimetableSearchResult> searchTeacherCourseTimetable(Teacher teacher) {
        for (List<CourseTimetableSearchResult> resultList :
                newUrpSpiderService.searchCourseTimetableByTeacher(teacher.getAccount())) {
            return resultList;
        }
        return Collections.emptyList();
    }


    public List<SearchClassroomResult> searchUrpClassroom(SearchClassroomPost searchClassroomPost) {
        for (SearchResultWrapper<SearchClassroomResult> resultWrapper : newUrpSpiderService.searchClassroomInfo(searchClassroomPost)) {
            return resultWrapper.getPageData().getRecords();
        }
        return Collections.emptyList();
    }

    public List<SearchClassroomResult> searchAllUrpClassroom() {
        SearchClassroomPost post = new SearchClassroomPost();
        post.setExecutiveEducationPlanNum("2019-2020-1-1");
        return searchUrpClassroom(post);
    }


    public List<UrpClass> searchUrpClass(SearchClassInfoPost searchClassInfoPost) {
        return newUrpSpiderService.getClassInfoSearchResult(searchClassInfoPost).stream()
                .flatMap(x -> x.getRecords().stream())
                .map(ClassInfoSearchResult::transToUrpClass)
                .collect(Collectors.toList());
    }

    public List<CourseTimetable> searchCourse(String termYear, int termOrder, String classNum) {
        return newUrpSpiderService.searchClassTimeTable(termYear, termOrder, classNum)
                .stream().flatMap(Collection::stream)
                .map(CourseTimetableSearchResult::transToCourseTimetable)
                .flatMap(Collection::stream)
                .peek(x -> x.setGmtCreate(new Date()))
                .collect(Collectors.toList());

    }


    public void saveCurrentClassTimetable() {
        SearchClassInfoPost post = new SearchClassInfoPost();
        post.setExecutiveEducationPlanNum("2019-2020-2-1");
        ArrayList<String> fail = new ArrayList<>();
        for (UrpClass urpClass : searchUrpClass(post)) {
            try {
                List<CourseTimetable> list = searchCourse("2019-2020", 2, urpClass.getClassNum());
                courseTimeTableDao.insertBatch(list);
                List<ClassCourseTimetable> collect = list.stream().map(x -> new ClassCourseTimetable()
                        .setClassId(urpClass.getClassNum())
                        .setCourseTimetableId(x.getId())
                        .setTermOrder(x.getTermOrder())
                        .setTermYear(x.getTermYear())
                ).collect(Collectors.toList());

                classCourseTimetableDao.insertBatch(collect);
            } catch (Exception e) {
                e.printStackTrace();
                fail.add(urpClass.getClassNum());
                continue;
            }


            System.out.println(urpClass.getClassNum());
        }
        System.out.println(fail);

    }

}
