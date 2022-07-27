package com.dentalportal.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomSuccessHandler successHandler;
    private final CustomErrorHandler errorHandler;
    private  String[] allowedPaths = {
        "/error",
            "/fail"
    };

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("Lokesh").password(passwordEncoder.encode("admin")).roles("ADMIN")
        .and()
        .withUser("admin").password(passwordEncoder.encode("admin")).roles("USER");

//        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
         http.authorizeRequests().antMatchers(allowedPaths).permitAll();
        http.authorizeRequests()
            .antMatchers("/hello2/**").hasRole("ADMIN").and().authorizeRequests()
            .antMatchers("/hello1/**").hasRole("USER").and().authorizeRequests()
            .anyRequest().authenticated().and().formLogin().loginPage("/login")
//            .defaultSuccessUrl("/hello1", true)
//            .failureForwardUrl("/error");
            .successHandler(successHandler)
            .failureHandler(errorHandler)
            .permitAll()
        .and()
        .logout()
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .logoutSuccessUrl("/login")
        .permitAll();
    }
}
