package com.example.toonda;

import com.example.toonda.config.jwt.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class ToonDaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToonDaApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "https://toon-da.vercel.app/")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        .exposedHeaders(JwtUtil.AUTHORIZATION_HEADER); //token 내용 전달
            }
        };
    }

}
