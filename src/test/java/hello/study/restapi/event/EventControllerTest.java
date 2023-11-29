package hello.study.restapi.event;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.study.restapi.common.RestDocsConfiguration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
			.andExpect(jsonPath("_links.self").exists())
			.andExpect(jsonPath("_links.query-events").exists())
			.andExpect(jsonPath("_links.update-event").exists())
			.andDo(document("create-event",
				links(
					linkWithRel("self").description("link to self"),
					linkWithRel("query-events").description("link to query-events"),
					linkWithRel("update-event").description("link to update-event")
				),
				requestHeaders(
					headerWithName(HttpHeaders.ACCEPT).description("accept header"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
				),
				requestFields(
					fieldWithPath("name").description("Name of new event"),
					fieldWithPath("description").description("description of new event"),
					fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
					fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
					fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
					fieldWithPath("endEventDateTime").description("date time of end of new event"),
					fieldWithPath("location").description("location of new event"),
					fieldWithPath("basePrice").description("base price of new event"),
					fieldWithPath("maxPrice").description("max price of new event"),
					fieldWithPath("limitOfEnrollment").description("limit of enrollment")
				),
				responseHeaders(
					headerWithName(HttpHeaders.LOCATION).description("Location header"),
					headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
				),
				relaxedResponseFields(
					fieldWithPath("id").description("identifier of new event"),
					fieldWithPath("name").description("Name of new event"),
					fieldWithPath("description").description("description of new event"),
					fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
					fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
					fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
					fieldWithPath("endEventDateTime").description("date time of end of new event"),
					fieldWithPath("location").description("location of new event"),
					fieldWithPath("basePrice").description("base price of new event"),
					fieldWithPath("maxPrice").description("max price of new event"),
					fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
					fieldWithPath("free").description("it tells if this event is free or not"),
					fieldWithPath("offline").description("it tells if this event is offline event or not"),
					fieldWithPath("eventStatus").description("event status")
				)
			))
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