package thuc.ute.rest_api_ajax;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import thuc.ute.rest_api_ajax.config.StorageProperties;
import thuc.ute.rest_api_ajax.service.IStorageService;
@EnableConfigurationProperties(StorageProperties.class)

@SpringBootApplication
public class RestApiAjaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiAjaxApplication.class, args);
	}
	// thêm cấu hình storage
	@Bean
    CommandLineRunner init(IStorageService storageService) {
		return (args -> {
			storageService.init();
		});
	}

}
