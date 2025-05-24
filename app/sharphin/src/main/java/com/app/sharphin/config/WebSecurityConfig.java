package com.app.sharphin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/css/**", "/js/**", "/webjars/**","/images/**","/sighup", "/sighin").permitAll()
				.requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/sighin")
				.failureUrl("/sighin?error")
				.defaultSuccessUrl("/", true)
				.permitAll()
			)
			.logout((logout) -> logout.permitAll()
			    .logoutUrl("/sighout")
			    .logoutSuccessUrl("/sighin")
			)
			.exceptionHandling((exceptions) -> exceptions
			    .accessDeniedPage("/403") // 権限不足時にリダイレクトする先のURL
		    );

		return http.build();
	}
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
