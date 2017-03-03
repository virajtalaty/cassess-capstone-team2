package com.cassess.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Order(1)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static String REALM="REST_REALM";

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("ADMIN").and()
                .withUser("nicest").password("password").roles("NICEST");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable().httpBasic().and()
                .authorizeRequests()
                .antMatchers("/index.html", "/partials/home.html", "/partials/dashboard.html",
                        "/partials/login.html", "/").hasRole("ADMIN")
                .antMatchers("/rest/**").hasRole("NICEST")
                .and()
                .httpBasic().realmName(REALM).authenticationEntryPoint(getRestBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public RestBasicAuthenticationEntryPoint getRestBasicAuthEntryPoint(){
        return new RestBasicAuthenticationEntryPoint();
    }

}

