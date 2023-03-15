package by.suhareva.adapterservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Adapter Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .email("suharevaksu86@gmail.com")
                                .url("https://github.com/KsuSuhareva/AdapterService")
                                .name("Suhareva Kseniya")
                        )
                );
    }
}
