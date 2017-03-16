package com.cassess;

import com.cassess.dao.rest.CourseServiceDaoImpl;
import com.cassess.entity.taiga.AuthUser;
import com.cassess.entity.taiga.CourseList;
import com.cassess.entity.taiga.Project;
import com.cassess.model.github.GatherGitHubData;
import com.cassess.service.taiga.AuthUserService;
import com.cassess.service.taiga.MembersService;
import com.cassess.service.taiga.ProjectService;
import com.cassess.service.taiga.TaskDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;


@SpringBootApplication
@RestController
public class CassessApplication extends SpringBootServletInitializer {

    @Autowired
    private CourseServiceDaoImpl coursesDao;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(CassessApplication.class);
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/resources/static/");
        resolver.setSuffix(".html");
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");



            //ConsumeChannels consumeChannels = (ConsumeChannels) ctx.getBean("consumeChannels");
    		//consumeChannels.getChannelsList();
            //ConsumeUsers consumeUsers = (ConsumeUsers) ctx.getBean("consumeUsers");
    		//consumeUsers.getUserInfo("U2G79FELT");

            /* Old methods for utilizing Taiga calls and filling tables
             */
            AuthUserService authUserService = (AuthUserService) ctx.getBean("authUserService");
            AuthUser auth = authUserService.getUserInfo();
            ProjectService projectService = (ProjectService) ctx.getBean("projectService");
            Project proj = projectService.getProjectInfo(auth.getAuth_token(), "tjjohn1-ser-401-capstone-project-team-2");
            MembersService membersService = (MembersService) ctx.getBean("membersService");
            membersService.getMembers(proj.getId(), auth.getAuth_token(), 1);
            TaskDataService taskDataService = (TaskDataService) ctx.getBean("taskDataService");
            taskDataService.getTasks(proj.getId(), auth.getAuth_token(), 1);
            taskDataService.getTaskTotals(proj.getSlug());

            /* New single call methods to update operations using Course and Student tables
            provisioned by nicest only, for external API retrieval and internal SQL reading
            Data is retrieved only from provisioned tables, so they must be updated first

            List<CourseList> courseList = coursesDao.listGetCourses();
            ProjectService projectService = (ProjectService) ctx.getBean("projectService");
            for(CourseList course:courseList) {
                projectService.updateProjects(course.getCourse());
            }

            MembersService membersService = (MembersService) ctx.getBean("membersService");
            for(CourseList course:courseList) {
                membersService.updateMembership(course.getCourse());
            }

            TaskDataService taskDataService = (TaskDataService) ctx.getBean("taskDataService");
            for(CourseList course:courseList) {
                taskDataService.updateTaskTotals(course.getCourse());
            }

            */

            GatherGitHubData gatherGitHubData = (GatherGitHubData) ctx.getBean("gatherGitHubData");
            gatherGitHubData.fetchData("tjjohn1","cassess-capstone-team2");
        };
    }

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

}
