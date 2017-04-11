package edu.asu.cassess.service.taiga;

import edu.asu.cassess.persist.entity.rest.Course;
import edu.asu.cassess.persist.entity.taiga.*;
import edu.asu.cassess.dao.taiga.*;
import edu.asu.cassess.persist.repo.taiga.TaskRepo;
import edu.asu.cassess.persist.repo.taiga.TaskTotalsRepo;
import edu.asu.cassess.service.rest.ICourseService;
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
import java.util.List;

@Service
@Transactional
public class TaskDataService implements ITaskDataService {

    private RestTemplate restTemplate;

    private String tasksListURL;

    @Autowired
    private TaskRepo TaskDao;

    @Autowired
    private TaskTotalsRepo TaskTotalsRepo;

    @Autowired
    private ITaskQueryDao TaskQueryDao;

    @Autowired
    private IMemberQueryDao MemberQueryDao;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private IProjectQueryDao projectsDao;

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

        //System.out.println("Page: " + page);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<TaskData[]> taskList = restTemplate.getForEntity(tasksListURL + id + "&page=" + page, TaskData[].class, request);

        TaskData[] tasks = taskList.getBody();

        //System.out.println("Number Results: " + tasks.length);

        for (int i = 0; i < tasks.length - 1; i++) {

            TaskDao.save(tasks[i]);
        }
        //System.out.println("Headers Response" + taskList.getHeaders());

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
        for (MemberData member: memberNames) {
            String name = member.getFull_name();
            int closedTasks = TaskQueryDao.getClosedTasks(name);
            int newTasks = TaskQueryDao.getNewTasks(name);
            int inProgressTasks = TaskQueryDao.getInProgressTasks(name);
            int readyForTestTasks = TaskQueryDao.getReadyForTestTasks(name);
            int openTasks = newTasks + inProgressTasks + readyForTestTasks;
            TaskTotalsRepo.save(new TaskTotals(new TaskTotalsID(member.getUser_email()), name, member.getProject_name(), member.getTeam(), course, closedTasks, newTasks, inProgressTasks,
                    readyForTestTasks, openTasks));
        }
        TaskQueryDao.truncateTaskData();
    }

    /* Method to obtain all task totals for members of a particular course,
        occurring on a schedule
     */
    @Override
    public void updateTaskTotals(String course) {
        System.out.println("Updating Tasks");
        Course tempCourse = (Course) courseService.read(course);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String formatted = df.format(new Date());
        Date current = new Date();
        try {
            current = df.parse(formatted);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(current.before(tempCourse.getEnd_date())) {
            String token = tempCourse.getTaiga_token();
            List<ProjectIDSlug> idSlugList = projectsDao.listGetTaigaProjectIDSlug(course);
            for (ProjectIDSlug idSlug : idSlugList) {
                System.out.println("Id: " + idSlug.getId() + "/Slug: " + idSlug.getSlug());
                getTasks(idSlug.getId(), token, 1);
                getTaskTotals(idSlug.getSlug(), course);
            }
        }
    }
}

