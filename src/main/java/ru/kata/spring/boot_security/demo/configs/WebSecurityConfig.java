package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kata.spring.boot_security.demo.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    private final UserService userService;


    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
//    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
    //    @Bean
    //    public PasswordEncoder passwordEncoder() {
    //        return new BCryptPasswordEncoder();
    //    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login") // страница с формой логина
                .successHandler(successUserHandler) // логика обработки при логине
                .loginProcessingUrl("/login") //action с формы логина
                .usernameParameter("username") //параметры логина и пароля с формы логина
                .passwordParameter("password")
                .permitAll(); //доступ к форме логина всем
        http
                .authorizeRequests() //страница регистрации недоступной для авторизированных пользователей
                .antMatchers( "/css/**").permitAll()
                .antMatchers( "/registration/**").anonymous()
                .antMatchers("/login").anonymous() //страница аутентификаци доступна анонимам
                .antMatchers("/user/**").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')").anyRequest().authenticated();

        http.logout()

                .permitAll() // разрешаем делать логаут всем
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // указываем URL логаута
                .logoutSuccessUrl("/login?logout") // указываем URL при удачном логауте
                .and().csrf().disable(); //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("admin")
//                        .roles("ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}