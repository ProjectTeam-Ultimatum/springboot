package ultimatum.project.global.config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();

      // 개발 환경에서 허용할 origin 설정 (실제 배포시에는 필요한 origin으로 변경)
//      config.addAllowedOrigin("https://testvue-flax.vercel.app");
      config.addAllowedOrigin("http://localhost:8081/");
      config.addAllowedOrigin("https://jejurang.site/");

      // 필요한 헤더 및 메서드 설정
      config.addAllowedHeader("*");
      config.addExposedHeader("Authorization");
      config.addAllowedMethod("*");

      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
   }
}
