package org.likelion.couplediray.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//프론트나 다른 도메인에서 오는 요청을 허용할 지 확인
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500")// ✅ 정확한 출처만 허용
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // ✅ 세션 유지 가능
    }
}