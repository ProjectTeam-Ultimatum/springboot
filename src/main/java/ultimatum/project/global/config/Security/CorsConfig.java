package ultimatum.project.global.config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//@Configuration
//public class CorsConfig {
//
//   @Bean
//   public CorsFilter corsFilter() {
//      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//      CorsConfiguration config = new CorsConfiguration();
//      config.setAllowCredentials(true);
//      config.addAllowedOrigin("*");
//      config.addAllowedHeader("*");
//      config.addAllowedMethod("*");
//
//      source.registerCorsConfiguration("/api/**", config);
//      source.registerCorsConfiguration("/login_proc", config);
//      return new CorsFilter(source);
//   }
//
//}
@Configuration
public class CorsConfig {

   @Bean
   public CorsFilter corsFilter() {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOriginPattern("http://localhost:8081"); // 실제 배포시에는 구체적인 도메인으로 제한하세요
      config.addAllowedOriginPattern("http://localhost:8080");
      config.addAllowedHeader("*");
      config.addExposedHeader("Authorization"); // 이 부분이 중요
      config.addAllowedMethod("*");

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
   }
}