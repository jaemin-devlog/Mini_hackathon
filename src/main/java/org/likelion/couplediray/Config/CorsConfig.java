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
                .allowedOrigins("*") // ✅ 모든 출처 허용 (개발 중엔 OK)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false); // 세션 안 쓰면 false
    }
}