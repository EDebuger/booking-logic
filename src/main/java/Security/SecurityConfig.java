package Security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }


    @Bean // decides which endpoints are accesible and how
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/estaurants/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/restaurants/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/restaurants/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/restaurants/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/tables/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/tables/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/tables/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/tables/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/bookings/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/bookings/**").hasRole("ADMIN") // change a user's booking, but don't add
//                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/users/deleteUser/").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/restaurants/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.POST, "/restaurants/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/restaurants/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.GET, "/restaurants/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/tables/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.POST, "/tables/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/tables/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.GET, "/tables/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.GET, "/bookings/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/bookings/**").hasRole("ADMINADMIN") // change a user's booking, but don't add
//                        .requestMatchers(HttpMethod.DELETE, "/users/deleteUser/").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/users/deleteUser/").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/users/deleteAdmin/").hasRole("SUPERADMIN")
//                        .requestMatchers(HttpMethod.POST, "/users/insertUser").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/users/getByName/").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/tables/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/restaurants/**").hasRole("USER")
//                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("USER") // change own profile
//                        .requestMatchers(HttpMethod.POST, "/bookings/postBooking").hasRole("USER")
//                        .requestMatchers(HttpMethod.GET, "/bookings/getByUserId/").hasRole("USER") // get their own
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults())
//                .logout(Customizer.withDefaults());

        http // could allow different origins/ports
                    .cors(Customizer.withDefaults())
                        .csrf(csrf -> csrf.disable())
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll() // meant to let anyone use it
                                .anyRequest().authenticated())
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling(ex -> ex
                                .authenticationEntryPoint(jwtAuthEntryPoint));



        return http.build();
    }

    @Bean // encrypt password when it gets passed around
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Value("${cors.allowed-origins}")
    private String allowedOrigins; // if somebody else needs access

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:5173"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return source;
        }
}
