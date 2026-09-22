package br.com.isiflix.facebank.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {
		http.cors(cors -> {
			cors.configurationSource(this.corsConfigurationSource());
		})
		
		.csrf( (csrf) -> { 
			csrf.disable();
		})
		.authorizeHttpRequests( (auth) -> {
			auth.requestMatchers(HttpMethod.POST, "/clientes").permitAll()
			    .requestMatchers(HttpMethod.POST, "/login").permitAll()
			    .requestMatchers(HttpMethod.POST, "/face/register").permitAll()
			    .requestMatchers(HttpMethod.GET, "/face/training").permitAll()
			    .requestMatchers(HttpMethod.POST, "/face/recognize").permitAll()
			    .requestMatchers(HttpMethod.GET, "/h2-console/**").permitAll()
			    .anyRequest().authenticated();
		})
		.addFilterBefore(new IsiBankFilter(), UsernamePasswordAuthenticationFilter.class)
		.headers( (header) -> header.frameOptions((iframe) -> iframe.disable() ));				
		return http.build();
	}
	
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("*"));
	    configuration.setAllowedMethods(Arrays.asList("*"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

}