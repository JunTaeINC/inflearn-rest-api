package hello.study.restapi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

	/*
		스프링 3.x 버전에서는 Jackson 라이브러리를 이용한 JSON 직렬화/역직렬화 설정을 application.properties 파일에서 직접 설정할 수 없다.
		대신, Jackson 라이브러리의 ObjectMapper 클래스를 사용하여 직접 설정해야 한다.
	 */
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		/*
			Java 8의 날짜/시간 타입인 java.time.LocalDateTime을 Jackson 라이브러리가 기본적으로 지원하지 않기 때문에
			이를 해결하기 위해서는 jackson-datatype-jsr310 모듈을 추가
		*/
		objectMapper.registerModule(new JavaTimeModule());

		return objectMapper;
	}
}