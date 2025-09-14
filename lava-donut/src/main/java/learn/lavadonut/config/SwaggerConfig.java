package learn.lavadonut.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Link: http://localhost:8080/swagger-ui/index.html

@Configuration
@OpenAPIDefinition(info = @Info(title = "Lavadonut API", version = "1.0", description = "API for managing stock portfolios and estimating tax impact"))
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi generalApi() {
        return GroupedOpenApi.builder()
                .group("general-api") // Link: http://
                .pathsToMatch("/api/**") // Matches all paths that start with '/api/'
                .build();
    }

//    // Bean for admin specific APIs
//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("admin-api")
//                .pathsToMatch("/api/admin/**")
//                .build();
//    }
}
