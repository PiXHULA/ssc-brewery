package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                    UsernamePasswordAuthenticationFilter.class)
            .csrf().disable(); //THIS ONE SETS GLOBALLY

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeRequests(authorize -> authorize
                .antMatchers("/h2-console/**").permitAll() //do not use in production!
                .antMatchers("/", "/webjars/**", "/resources/**", "/login").permitAll()
                )
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

        //H2 console config
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); //NO OPERATIONS
//        return new LdapShaPasswordEncoder(); //LDAP
//        return new StandardPasswordEncoder(); //SHA-256
//        return new BCryptPasswordEncoder(10); //BCrypt
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); //Default to use {key} for encrypt password
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder(); //Implemented our own class with a custom delegating password encoder
    }

//    @Autowired
//    JpaUserDetailsService jpaUserDetailsService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());


//        auth.inMemoryAuthentication()
//                .withUser("spring")
////                .password("{noop}password")
//                .password("{bcrypt}$2a$10$SPJNT.U2z4K/BdmdIJ2KPO0FGDv9QSlXymPV1Lu1KhdYgAMIItumu")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
////                .password("{noop}password") //When no passwordEncoder is configured
////                .password("password") //When NO OP passwordEncoder is configured
////                .password("{SSHA}2pmyd+hvxMZV+MjUqRU8c8JZs5F59SZW03SdEQ==") //When LdapShaPasswordEncoder is configured
////                .password("3cafee77e83097d4e495f654a4fbf55acc5b3b0f5e9226681e740112c995d033e08f65a86d27926f") //When SHA-256 pw encoder is configured
//                .password("{sha256}e873fe42de9ba22aa53a8df5fd74f226aaf0e3c2b66463f2f1c9fe9ee1d414a077f9252ec4211869") //BCRYPT
//                .roles("USER");
//
//
//        auth.inMemoryAuthentication()
//                .withUser("joakim")
////                .password("{noop}tiger")
//                .password("{bcrypt10}$2a$15$wzjOQZFkKIyM1z0xTpM9m.S5J70UXaCog3CRBCSOFIYKohRg9wcQq")
//                .roles("CUSTOMER");
//}

    //    @Override
////    @Bean
////    protected UserDetailsService userDetailsService() {
////        UserDetails admin = User.withDefaultPasswordEncoder()
////                .username("spring")
////                .password("guru")
////                .roles("ADMIN")
////                .build();
////
////        UserDetails user = User.withDefaultPasswordEncoder()
////                .username("user")
////                .password("password")
////                .roles("USER")
////                .build();
////
////        return new InMemoryUserDetailsManager(admin, user);
////    }









}
