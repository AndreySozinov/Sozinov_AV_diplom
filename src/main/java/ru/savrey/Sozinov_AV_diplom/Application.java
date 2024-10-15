package ru.savrey.Sozinov_AV_diplom;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import ru.savrey.Sozinov_AV_diplom.model.User;
import ru.savrey.Sozinov_AV_diplom.repository.UserRepository;

@EnableJpaRepositories("ru.savrey.Sozinov_AV_diplom.repository")
@EntityScan("ru.savrey.Sozinov_AV_diplom.model")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public CommandLineRunner adminLoader(UserRepository repo, PasswordEncoder passwordEncoder) {
		return args -> {
			repo.save(new User(
					"Созинов",
					"Андрей",
					"Викторович",
					"+7(963)436-49-43",
					"savrey@ya.ru",
					"savrey",
					passwordEncoder.encode("s57a282v")));
		};
	}

//	@Bean
//	public SpringTemplateEngine templateEngine() {
//		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		templateEngine.setTemplateResolver(templateResolver());
//		templateEngine.addDialect(new SpringSecurityDialect());
//		return templateEngine;
//	}
}
