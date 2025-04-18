
// .formLogin(httpForm -> {
//     httpForm.loginPage("/login").permitAll(); // Login page for web users
//     httpForm.defaultSuccessUrl("/home"); // Redirect after successful login
//     httpForm.failureForwardUrl("/login?error=true"); // Redirect after failed login
// })

package e_was.backend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import e_was.backend.Service.MyAppService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final MyAppService appUserService;

    public SecurityConfig(MyAppService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable())
                .cors(httpCors -> {}) 
                .authorizeHttpRequests(registry -> {
                    // Permit all static resources and public endpoints
                    registry.requestMatchers(new AntPathRequestMatcher("/signup")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/login")).permitAll();

                    // Permit all API endpoints
                    registry.requestMatchers(new AntPathRequestMatcher("/api/status/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/organization/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/organizationStats/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/user/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/pickup/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/item/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/transaction/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/rewards/**")).permitAll();
                    registry.requestMatchers(new AntPathRequestMatcher("/api/userRewards/**")).permitAll();

                    // Require authentication for all other requests
                    registry.anyRequest().authenticated();
                })
                .build();
    }
}
