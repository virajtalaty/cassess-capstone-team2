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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
public class CassessApplication {

   /* @RequestMapping("/resource")
    public Map<String, Object> home(){
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    @RequestMapping("/user")
    public Principal user(Principal user){
        return user;
    }*/

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    @EnableWebSecurity
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter{
        @Override
        protected void configure(HttpSecurity http) throws Exception{
            http
                    .httpBasic().and()
                    .authorizeRequests()
                    .antMatchers("/index.html", "/partials/home.html", "/partials/dashboard.html",
                            "/partials/login.html", "/")
                    .permitAll().anyRequest().authenticated()
                    .and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            /*
    		ConsumeChannels consumeChannels = (ConsumeChannels) ctx.getBean("consumeChannels");
    		consumeChannels.getChannelsList();

            ConsumeUsers consumeUsers = (ConsumeUsers) ctx.getBean("consumeUsers");
    		consumeUsers.getUserInfo("U2G79FELT");
    		
            ConsumeAuthUser consumeAuthUser = (ConsumeAuthUser) ctx.getBean("consumeAuthUser");
            AuthUser auth = consumeAuthUser.getUserInfo();
        	
            ConsumeProjectList consumeProjectList = (ConsumeProjectList) ctx.getBean("consumeProjectList");
            Project proj = consumeProjectList.getProjectInfo(auth.getAuth_token(), auth.getId());
            
            GetTaskData getTaskData = (GetTaskData) ctx.getBean("getTaskData");
            getTaskData.getTasks(proj.getId(), auth.getAuth_token(), 1);
            getTaskData.getMembers(proj.getId(), auth.getAuth_token(), 1);
            getTaskData.getTaskTotals();
            GatherGitHubData gatherGitHubData = (GatherGitHubData) ctx.getBean("gatherGitHubData");
            gatherGitHubData.fetchData();
            //get commit List returns all commits there are
            System.out.println(gatherGitHubData.getCommitList());*/
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(CassessApplication.class, args);
    }

}
