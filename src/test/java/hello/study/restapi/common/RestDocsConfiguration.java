package hello.study.restapi.common;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RestDocsConfiguration {

	@Bean
	public RestDocsMockMvcConfigurationCustomizer customizer() {
		return configurer -> configurer.operationPreprocessors()
			.withRequestDefaults(prettyPrint())
			.withResponseDefaults(prettyPrint());
	}
}
