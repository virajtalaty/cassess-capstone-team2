package com.cassess;

import com.cassess.dao.slack.ConsumeChannels;
import com.cassess.dao.slack.ConsumeUsers;
import com.cassess.model.github.GatherGitHubData;
import com.cassess.model.taiga.ConsumeAuthUser;
import com.cassess.model.taiga.ConsumeProjectList;
import org.springframework.boot.CommandLineRunner;
import com.cassess.model.github.GatherGitHubData;
import com.cassess.model.taiga.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SpringBootApplication
@RestController
public class CassessApplication extends SpringBootServletInitializer {

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
            //System.out.println("Let's inspect the beans provided by Spring Boot:");

            //ConsumeChannels consumeChannels = (ConsumeChannels) ctx.getBean("consumeChannels");
    		//consumeChannels.getChannelsList();
            //ConsumeUsers consumeUsers = (ConsumeUsers) ctx.getBean("consumeUsers");
    		//consumeUsers.getUserInfo("U2G79FELT");
    		
            //ConsumeAuthUser consumeAuthUser = (ConsumeAuthUser) ctx.getBean("consumeAuthUser");
            //AuthUser auth = consumeAuthUser.getUserInfo();
        	
            //ConsumeProjectList consumeProjectList = (ConsumeProjectList) ctx.getBean("consumeProjectList");
            //Project proj = consumeProjectList.getProjectInfo(auth.getAuth_token(), auth.getId());
            
            GetTaskData getTaskData = (GetTaskData) ctx.getBean("getTaskData");
            getTaskData.getTasks(proj.getId(), auth.getAuth_token(), 1);
            getTaskData.getMembers(proj.getId(), auth.getAuth_token(), 1);
            getTaskData.getTaskTotals();*/
            GatherGitHubData gatherGitHubData = (GatherGitHubData) ctx.getBean("gatherGitHubData");
            gatherGitHubData.fetchData("tjjohn1","cassess-capstone-team2");
            //get commit List returns all commits there are
            //System.out.println(gatherGitHubData.getCommitList());
        };
    }

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

}
