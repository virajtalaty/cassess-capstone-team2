package com.cassess;

import com.cassess.model.slack.UserObject;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.cassess.model.slack.ConsumeUsers;

import java.security.Principal;
import java.util.*;

@SpringBootApplication
@RestController
@ImportResource({"classpath*:applicationContext.xml"})
public class CassessApplication {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/resource")
    public Map<String, Object> home(){
        List<String> members = new ArrayList<>();
        Map<String, Object> model = new HashMap<>();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ConsumeUsers consumeUsers = (ConsumeUsers) context.getBean("consumeUsers");
        List<UserObject> users = consumeUsers.getUserList().getMembers();
        System.out.println("These are the people in the list: ");
        for ( UserObject member : users) {
            System.out.println(member.getName());
            String teamMember = member.getName();
            members.add(teamMember);
        }
        model.put("id", UUID.randomUUID().toString());
        model.put("content", members);
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
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/index.html", "/login.html", "/")
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
        };
    }
    
	public static void main(String[] args) {
		//ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		SpringApplication.run(CassessApplication.class, args);
	}
}
