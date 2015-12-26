package com.delirium.finapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig
    extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

//    @Autowired
//    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    //
    // http.csrf().disable()
    // .authorizeRequests()
    // .anyRequest().permitAll();
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests().antMatchers("/*").permitAll()
            .antMatchers("/www/js/*").permitAll().antMatchers("/assets/js/*/*/*").permitAll()
            .antMatchers("/www/css/*").permitAll().antMatchers("/assets/css/*/*/*").permitAll()
            .antMatchers("/www/images/*").permitAll();

//            .antMatchers("/custom/js/*").permitAll().antMatchers("/custom/css/*").permitAll()
//            .antMatchers("/custom/images/*").permitAll()

//            .antMatchers("/locale").permitAll().antMatchers("/locales/*").permitAll()
//            .antMatchers("/app/locale/*").permitAll().anyRequest().fullyAuthenticated();

        http.formLogin().loginPage("/www/login/login.html")
            .failureUrl("/www/login/login.html").loginProcessingUrl("/login-action")
            .failureHandler((request, response, authentication) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            })
            .successHandler((request, response, authentication) -> {
                response.setStatus(HttpStatus.OK.value());
            })
            .permitAll().and().httpBasic();

        http.logout().logoutUrl("/logout-action")
            .logoutSuccessHandler((request, response, authentication) -> {
                response.setStatus(HttpStatus.OK.value());
            })
//            .logoutSuccessUrl("/www/login/login.html")
            .permitAll();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}
