package hjs.mall.security;

import hjs.mall.security.JwtAuthenticationFilter;
import hjs.mall.security.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .formLogin(Customizer.withDefaults())
//                .build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                // use ID, Password for Base64
        http
                .formLogin(Customizer.withDefaults())
                // it is not used because the system is based on jwt instead of session
                .csrf(AbstractHttpConfigurer::disable)
                // CORS Configuration
                .cors(c -> {
                            CorsConfigurationSource source = request -> {
                                // Cors Acceptable Pattern
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(
                                        List.of("*")
                                );
                                config.setAllowedMethods(
                                        List.of("*")
                                );
                                return config;
                            };
                            c.configurationSource(source);
                        }
                )
                // Spring Security session policy : disallow session creation and usage
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // set whitelist
                .authorizeHttpRequests(auth ->
                        // allow user to register or login
                        auth.requestMatchers("/v1/members/register",
                                        "/v1/members/login",
                                        "/v1/refresh-token")
                                .permitAll()
                                .anyRequest().authenticated()
                )
                // JWT 인증 필터 적용
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                // error handling
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setCharacterEncoding("utf-8");
                            response.setContentType("text/html; charset=UTF-8");
                            response.getWriter().write("Unauthorized");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (authException instanceof BadCredentialsException) {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write(authException.getMessage());
                            } else {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write("Unauthenticated");
                            }
                        })
                );


        return http.build();
    }

}

