package fr.fin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfiguration {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(
                		new Info()
                		.title("Finally - Point of Sale Application")
                		.description("""
                				This is the API Documentation for Finally Point of Sale, developped by : \n
                				- Huan X. \n
                				- Mélanie H. \n
                				- Sébastien D. \n
                				- Etienne V.
                				""")
                		.version("0.0.1")
                );
    }

	@Bean
	Docket api() {
	  return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build();
	}
}
