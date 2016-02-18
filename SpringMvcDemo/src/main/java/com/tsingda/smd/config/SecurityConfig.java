package com.tsingda.smd.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Log logger = LogFactory.getLog(SecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.debug("=============Security============");
        logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
        http.authorizeRequests()
            .antMatchers("/resources/**", "/signup", "/about", "/").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/login").permitAll()
            .and().logout().logoutUrl("/logout").permitAll()
            .and().httpBasic();

    }
}
