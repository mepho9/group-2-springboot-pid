package be.iccbxl.pid.reservations_springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SpringSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(BCryptVersion.$2Y, 12);
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/").permitAll()
						.requestMatchers(
								"/login", "/login**",
								"/css/**", "/js/**",
								"/forgot-password", "/reset-password",
								"/reset-success", "/register"
						).permitAll()
						.requestMatchers("/reservations/**").authenticated()
						.requestMatchers("/representations/*/reserve").authenticated()
						.requestMatchers("/admin").hasRole("ADMIN")
						.requestMatchers("/user").hasRole("MEMBER")
						.requestMatchers("/admin/reviews").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("login")
						.failureUrl("/login?loginError=true")
						.permitAll()
				)
				.logout(logout -> logout
						.logoutSuccessUrl("/login?logoutSuccess=true")
						.permitAll()
				)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(
								new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true")
						)
						.accessDeniedPage("/403")
				)
				.build();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/api/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/**").permitAll()
				)
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		return http.build();
	}
}