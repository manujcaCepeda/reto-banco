package ec.com.banco.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                		.title("API del Banco - Gestión de Clientes, Cuentas y Movimientos")
                        .description("Documentación del servicio REST del banco. Incluye clientes, cuentas y movimientos.")
                        .version("1.0.0")
                        .contact(new Contact().name("Manuel Cepeda").email("manujca@hotmail.com")));
    }
}
