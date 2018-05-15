package application.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    public static class WorkerSecurityConfiguration extends WebSecurityConfigurerAdapter {


        @Autowired
        BCryptPasswordEncoder passwordEncoder;


        public WorkerSecurityConfiguration() {
            super();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .debug(false)
                    .ignoring()
                    .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }

        @Bean
        public CorsFilter corsFilter() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.addAllowedOrigin("null"); // @Value: http://localhost:8080
            config.addAllowedOrigin("http://ec2-18-217-244-16.us-east-2.compute.amazonaws.com");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http

                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/admin/horarios/obtener-horarios-verano")
                    .permitAll()
                    .antMatchers(HttpMethod.OPTIONS,"/admin/horarios/obtener-horarios-verano")
                    .permitAll()

                    .antMatchers("/admin/**")
                    .hasAnyAuthority("BOSS", "UNDERBOSS")


                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error=loginError")
                    .defaultSuccessUrl("/admin/reservas")
                    .usernameParameter("userId")
                    .passwordParameter("password")

                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")

                    .and()
                    .exceptionHandling()
                    .accessDeniedPage("/403")


                    .and()
                    .csrf().disable()

                    .headers()
                    .frameOptions().disable()

                    .and()
                    .cors();

        }


/*
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","null","*"));
            configuration.setAllowedMethods(Arrays.asList("OPTIONS","GET","POST","PUT"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
*/



       /* @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(workerDetails).passwordEncoder(passwordEncoder);
        }*/

    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public StringKeyGenerator generateSecretKey() {
        return KeyGenerators.string();
    }
}

