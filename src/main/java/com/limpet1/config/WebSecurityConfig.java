package com.limpet1.config;

import com.limpet1.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {                                 /*for authorisation*/
        http.headers()
                .frameOptions().disable().and()
                .authorizeRequests()
                .mvcMatchers("/", "/login1", "/registration", "/**/*.js", "/**/*.css", "/**/*.jpg", "js", "css", "/**/*.png")
                .permitAll()

                .mvcMatchers("/main/**").hasAnyRole("ADMIN", "USER")
                .mvcMatchers("/ico/**").hasAnyRole("ADMIN", "USER")
                .and().authorizeRequests()
                .anyRequest().authenticated()

                .and().formLogin().permitAll()
                .loginPage("/login1")
                .loginProcessingUrl("/do-login")
                .usernameParameter("email")               /*change default name: username (default in Spring Security)*/
                .passwordParameter("password")
                .defaultSuccessUrl("/main")              /*after authorisation to root ("/") page*/


                .and().logout().logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN").and()
                .sessionManagement().maximumSessions(25).and()
                .and()
                .csrf().disable();

    }



    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {                   /*for authentication*/
        auth.userDetailsService(userDetailsService);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
