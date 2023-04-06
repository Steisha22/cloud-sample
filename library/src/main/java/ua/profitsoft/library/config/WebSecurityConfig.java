package ua.profitsoft.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    @Profile("!basic")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/home")
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    @Profile("basic")
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authRequests -> authRequests
                        .requestMatchers("/", "/api/**").permitAll()
                        //.requestMatchers("/api/books").hasAuthority("PRIV_API")
                        //.requestMatchers("/api/books/**").hasAuthority("PRIV_API")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .build();
    }

}
