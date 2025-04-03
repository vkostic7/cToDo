package veljko.couple_todo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import veljko.couple_todo.entities.User;
import veljko.couple_todo.repos.UserRepo;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepo userRepo;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/register/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(form -> form
                        .logoutSuccessUrl("/")
                        .permitAll())
                .formLogin(form -> form
                        .defaultSuccessUrl("/choose")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepo.findByUserName(username);
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUserName())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }
}