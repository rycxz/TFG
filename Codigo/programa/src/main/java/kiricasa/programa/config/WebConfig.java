package kiricasa.programa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto hace que http://localhost:8080/uploads/** apunte a la carpeta real uploads/ en el proyecto
        registry.addResourceHandler("/uploads/**")
              .addResourceLocations("file:C:/Users/recur/Desktop/TFG/Codigo/programa/uploads/");

    }
}
