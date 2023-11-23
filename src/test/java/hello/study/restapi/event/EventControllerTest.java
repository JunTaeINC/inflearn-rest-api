package hello.study.restapi.event;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
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
			.andExpect(jsonPath("id").value(Matchers.not(100)))
			.andExpect(jsonPath("free").value(Matchers.not(true)))
			.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
		;
	}

	@Test
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
	void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDto eventDto = EventDto.builder().build();

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(eventDto)))
			.andExpect(status().isBadRequest());
	}
}