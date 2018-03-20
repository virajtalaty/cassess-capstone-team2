package edu.asu.cassess.service.taiga;

import edu.asu.cassess.dao.taiga.IMemberQueryDao;
import edu.asu.cassess.dao.taiga.IProjectQueryDao;
import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.rest.Student;
import edu.asu.cassess.persist.entity.taiga.*;
import edu.asu.cassess.persist.repo.taiga.TaskTotalsRepo;
import edu.asu.cassess.service.rest.CourseService;
import edu.asu.cassess.service.rest.ICourseService;
import edu.asu.cassess.service.rest.IStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaskDataService implements ITaskDataService {

    class MutableInt {
        int value = 1; // note that we start at 1 since we're counting
        public void increment () { ++value;      }
        public int  get ()       { return value; }
    }

    private RestTemplate restTemplate;

    private String tasksListURL;

    private Map<String, MutableInt> closedMap = new HashMap<String, MutableInt>();
    private Map<String, MutableInt> newMap = new HashMap<String, MutableInt>();
    private Map<String, MutableInt> inProgressMap = new HashMap<String, MutableInt>();
    private Map<String, MutableInt> readyForTestMap = new HashMap<String, MutableInt>();


    @Autowired
    private TaskTotalsRepo TaskTotalsRepo;

    @Autowired
    private IMemberQueryDao MemberQueryDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IProjectQueryDao projectsDao;

    @Autowired
    private IStudentsService studentsService;

    public TaskDataService() {
        restTemplate = new RestTemplate();
        tasksListURL = "https://api.taiga.io/api/v1/tasks?project=";
    }

    @Override
    public TaskData[] getTasks(Long id, String token, int page) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        headers.add("x-disable-pagination", "True");

        System.out.println("Page: " + page);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<TaskData[]> taskList = restTemplate.getForEntity(tasksListURL + id + "&page=" + page, TaskData[].class, request);

        TaskData[] tasks = taskList.getBody();

        for (TaskData task:tasks) {
            if (task.getAssignmentData() != null) {
                //System.out.println("-----------------------------****************************************Full Name Display: " + task.getAssignmentData().getFull_name_display());
                //System.out.println("-----------------------------****************************************ClosedMap " + closedMap.get(task.getAssignmentData().getFull_name_display()));
                MutableInt closedCount = closedMap.get(task.getAssignmentData().getFull_name_display());
                //System.out.println("-----------------------------****************************************NewMap " + newMap.get(task.getAssignmentData().getFull_name_display()));
                MutableInt newCount = newMap.get(task.getAssignmentData().getFull_name_display());
                //System.out.println("-----------------------------****************************************InProgressMap " + inProgressMap.get(task.getAssignmentData().getFull_name_display()));
                MutableInt inProgressCount = inProgressMap.get(task.getAssignmentData().getFull_name_display());
                //System.out.println("-----------------------------****************************************ReadyForTestMap " + readyForTestMap.get(task.getAssignmentData().getFull_name_display()));
                MutableInt readyForTestCount = readyForTestMap.get(task.getAssignmentData().getFull_name_display());

                if (task.getStatusData() != null) {
                    if (task.getStatusData().getName() != null) {
                        if (task.getStatusData().getName().equalsIgnoreCase("Closed")) {
                            if (closedCount == null) {
                                closedMap.put(task.getAssignmentData().getFull_name_display(), new MutableInt());
                            } else {
                                closedCount.increment();
                            }
                        }

                        if (task.getStatusData().getName().equalsIgnoreCase("New")) {
                            if (newCount == null) {
                                newMap.put(task.getAssignmentData().getFull_name_display(), new MutableInt());
                            } else {
                                newCount.increment();
                            }
                        }
                        //System.out.println("-----------------------------****************************************InProgressStatus " + task.getStatusData().getName().equalsIgnoreCase("In Progress"));
                        if (task.getStatusData().getName().equalsIgnoreCase("In Progress")) {
                            if (inProgressCount == null) {
                                inProgressMap.put(task.getAssignmentData().getFull_name_display(), new MutableInt());
                            } else {
                                inProgressCount.increment();
                            }
                        }
                        //System.out.println("-----------------------------****************************************ReadyForTestStatus " + task.getStatusData().getName().equalsIgnoreCase("Ready For Test"));
                        if (task.getStatusData().getName().equalsIgnoreCase("Ready For Test")) {
                            if (readyForTestCount == null) {
                                readyForTestMap.put(task.getAssignmentData().getFull_name_display(), new MutableInt());
                            } else {
                                readyForTestCount.increment();
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Headers Response" + taskList.getHeaders());

        if (taskList.getHeaders().containsKey("x-pagination-next")) {
            page++;
            return getTasks(id, token, page);
        } else {

            return tasks;
        }
    }

    @Override
    public void getTaskTotals(String slug, String course) {
        List<MemberData> memberNames = MemberQueryDao.getMembers("Product Owner", slug);
        for (MemberData member : memberNames) {
            Student student = new Student();
            Object object = studentsService.find(member.getCompositeId().getEmail(), member.getCompositeId().getTeam(), course);
            if(object.getClass() == Student.class){
                student = (Student) object;
            }
            String name = member.getFull_name();
            int closedTasks = 0;
            int newTasks = 0;
            int inProgressTasks = 0;
            int readyForTestTasks = 0;
            if(closedMap.get(name) != null) {
                closedTasks = closedMap.get(name).get();
            }
            if(newMap.get(name) != null) {
                newTasks = newMap.get(name).get();
            }
            if(inProgressMap.get(name) != null) {
                inProgressTasks = inProgressMap.get(name).get();
            }
            if(readyForTestMap.get(name) != null) {
                readyForTestTasks = readyForTestMap.get(name).get();
            }
            int openTasks = newTasks + inProgressTasks + readyForTestTasks;
            //System.out.println("----------------------------**********************************************=========User: " + name);
            //System.out.println("----------------------------**********************************************=========ClosedCount: " + closedTasks);
            //System.out.println("----------------------------**********************************************=========NewCount: " + newTasks);
            //System.out.println("----------------------------**********************************************=========inProgressCount: " + inProgressTasks);
            //System.out.println("----------------------------**********************************************=========readyForTestCount: " + readyForTestTasks);
            if (student.getEnabled() != null) {
                if (student.getEnabled() != false) {
                    TaskTotalsRepo.save(new TaskTotals(new TaskTotalsID(member.getCompositeId().getEmail(), member.getCompositeId().getTeam(), course), name, member.getProject_name(), closedTasks, newTasks, inProgressTasks,
                            readyForTestTasks, openTasks));
                }
            }
        }
    }

    /* Method to obtain all task totals for members of a particular course,
        occurring on a schedule
     */
    @Override
    public void updateTaskTotals(String course) {
        System.out.println("Updating Tasks");
        if (courseService == null) courseService = new CourseService();
        Course tempCourse = (Course) courseService.read(course);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String formatted = df.format(new Date());
        Date current = new Date();
        try {
            current = df.parse(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (current.before(tempCourse.getEnd_date())) {
            String token = tempCourse.getTaiga_token();
            List<ProjectIDSlug> idSlugList = projectsDao.listGetTaigaProjectIDSlug(course);
            for (ProjectIDSlug idSlug : idSlugList) {
                System.out.println("Id: " + idSlug.getId() + "/Slug: " + idSlug.getSlug());
                getTasks(idSlug.getId(), token, 1);
                getTaskTotals(idSlug.getSlug(), course);
                closedMap.clear();
                newMap.clear();
                inProgressMap.clear();
                readyForTestMap.clear();
            }
        }
    }
}

