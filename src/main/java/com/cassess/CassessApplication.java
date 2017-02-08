package com.cassess;

import com.cassess.model.github.GatherGitHubData;
import com.cassess.model.taiga.ConsumeAuthUser;
import com.cassess.model.taiga.ConsumeProjectList;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cassess.model.slack.ConsumeUsers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
@ImportResource({"classpath*:applicationContext.xml"})
public class CassessApplication {

    @RequestMapping("/resource")
    public Map<String, Object> home(){
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    @RequestMapping("/user")
    public Principal user(Principal user){
        return user;
    }

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
            ConsumeUsers consumeUsers = (ConsumeUsers) ctx.getBean("consumeUsers");
    		consumeUsers.getUserInfo("U2G79FELT");

            ConsumeAuthUser consumeAuthUser = (ConsumeAuthUser) ctx.getBean("consumeAuthUser");
            consumeAuthUser.getUserInfo();
            String token = consumeAuthUser.getToken("TaigaTestUser@gmail.com");
            System.out.println("Taiga Token: " + token);
            Long id = consumeAuthUser.getID("TaigaTestUser@gmail.com");
            System.out.println("Taiga Member ID: " + id);

            ConsumeProjectList consumeProjectList = (ConsumeProjectList) ctx.getBean("consumeProjectList");
            consumeProjectList.getProjectInfo(token, id);
            System.out.println("Taiga Project Name: " + consumeProjectList.getName("tjjohn1"));

            GatherGitHubData gatherGitHubData = (GatherGitHubData) ctx.getBean("gatherGitHubData");
            gatherGitHubData.fetchData();
            //get commit List returns all commits there are
            System.out.println(gatherGitHubData.getCommitList());
        };
    }
    
	public static void main(String[] args) {
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		SpringApplication.run(CassessApplication.class, args);
	}
}
