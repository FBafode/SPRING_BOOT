package com.afpa.employes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.afpa.employes.service.UserLoginDetailsService;

@Configuration
public class Security {
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserLoginDetailsService();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder()); 
        return authProvider;
    }


	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

 
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                
                .requestMatchers("/login", "/register").permitAll()
                .requestMatchers("/add", "/show/**", "/artic/**").hasAnyAuthority("USER", "EDITOR", "ADMIN")
               
                .anyRequest().authenticated()
				/*
				 * .requestMatchers("/login").permitAll()
				 * .requestMatchers("/artic/**").authenticated()
				 * .requestMatchers("/add").authenticated()
				 * .requestMatchers("/show/**").authenticated()
				 * .requestMatchers("/register").permitAll()
				 */
                .and()
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/artic"))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll());
		return http.build();
	}
}
