package com.cassess.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@Order(2)
@EnableWebSecurity
public class SecurityConfigurationRest extends WebSecurityConfigurerAdapter {

    private static String REALM="REST_REALM";

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("nicest").password("password").roles("NICEST");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //CSRF Disabled for Rest API access for the time being
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/rest/**").hasRole("NICEST")
                .and().httpBasic().realmName(REALM).authenticationEntryPoint(getRestBasicAuthEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public RestBasicAuthenticationEntryPoint getRestBasicAuthEntryPoint(){
        return new RestBasicAuthenticationEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
