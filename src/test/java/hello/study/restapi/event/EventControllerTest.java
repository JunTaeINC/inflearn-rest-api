package hello.study.restapi.event;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class EventControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	EventRepository eventRepository;

	@Test
	void createEvent() throws Exception {
		Event event = Event.builder()
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
		event.setId(1);

		Mockito.when(eventRepository.save(event)).thenReturn(event);

		mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON_VALUE) // accept Header
				.content(objectMapper.writeValueAsString(event)))
			.andDo(print()) // show Result
			.andExpect(status().isCreated()) // isCreated == 201
			.andExpect(jsonPath("id").exists())
		;
	}
}