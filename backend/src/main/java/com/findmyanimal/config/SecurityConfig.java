package com.findmyanimal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Environment env;
    public SecurityConfig(Environment env) {
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (isLocalMode()) {
            setLocalMode(http);
        } else {
            setProdMode(http);
        }
    }
    private boolean isLocalMode() {
        String profile = env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "";
        return profile.equals("local");
    }

    private void setLocalMode(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable();

        http
                .headers()
                .frameOptions()
                .sameOrigin();

        http
                .authorizeRequests()
                .antMatchers("/h2-console/*").anonymous()
                .antMatchers("/v2/api-docs").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/swagger/**").anonymous()
                .antMatchers("/swagger-ui/**").anonymous()
                .antMatchers("/api/**").anonymous()

                .anyRequest().authenticated()
                .and()
                .formLogin().disable();
    }

    private void setProdMode(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable();

        http
                .authorizeRequests()
                .antMatchers("/v2/api-docs").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/swagger/**").anonymous()
                .antMatchers("/swagger-ui/**").anonymous()
                .antMatchers("/api/**").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
