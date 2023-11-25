package hello.study.restapi.event;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EventControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("정상적으로 이벤트를 생성하는 테스트")
	void createEvent() throws Exception {
		EventDto eventDto = EventDto.builder()
			.name("Kim's")
			.description("HBD Party")
			.beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 16, 0, 0))
			.closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 16, 23, 59))
			.beginEventDateTime(LocalDateTime.of(2023, 11, 17, 23, 59))
			.endEventDateTime(LocalDateTime.of(2023, 11, 18, 23, 59))
			.basePrice(10_000)
			.maxPrice(100_000)
			.location("Kim's House")
			.build();

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE) // accept Header
				.content(objectMapper.writeValueAsString(eventDto)))
			.andDo(print()) // show Result
			.andExpect(status().isCreated()) // isCreated == 201
			.andExpect(jsonPath("free").value(false))
			.andExpect(jsonPath("offline").value(true))
			.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
		;
	}

	@Test
	@DisplayName("입력 받을 수 없는 값을 사용한 경우 에러가 발생하는 테스트")
	void createEvent_Bad_Request() throws Exception {
		Event event = Event.builder()
			.id(100)
			.name("Kim's")
			.description("HBD Party")
			.beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 16, 0, 0))
			.closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 16, 23, 59))
			.beginEventDateTime(LocalDateTime.of(2023, 11, 17, 23, 59))
			.endEventDateTime(LocalDateTime.of(2023, 11, 18, 23, 59))
			.basePrice(10_000)
			.maxPrice(100_000)
			.location("Kim's House")
			.free(true)
			.offline(false)
			.eventStatus(EventStatus.PUBLISHED)
			.build();

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE) // accept Header
				.content(objectMapper.writeValueAsString(event)))
			.andDo(print()) // show Result
			.andExpect(status().isBadRequest())
		;
	}

	@Test
	@DisplayName("입력 값이 비어있으면 에러가 발생하는 테스트")
	void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDto eventDto = EventDto.builder().build();

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("입력 값이 잘못된 경우 에러가 발생하는 테스트")
	void createEvent_Bad_Request_Wrong_Input() throws Exception {
		EventDto eventDto = EventDto.builder()
			.name("Kim's")
			.description("HBD Party")
			.beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 16, 0, 0))
			.closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 16, 23, 59))
			.beginEventDateTime(LocalDateTime.of(2023, 11, 12, 23, 59))
			.endEventDateTime(LocalDateTime.of(2023, 11, 11, 23, 59))
			.basePrice(100_000)
			.maxPrice(10_000)
			.location("Kim's House")
			.build();

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$[0].objectName").exists())
			.andExpect(jsonPath("$[0].defaultMessage").exists())
			.andExpect(jsonPath("$[0].codes").exists())
		;
	}
}