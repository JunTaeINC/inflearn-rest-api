package hello.study.restapi.index;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hello.study.restapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;

public class IndexControllerTest extends BaseControllerTest {

	@Test
	void index() throws Exception {
		mockMvc.perform(get("/api"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("_links.events").exists());
	}
}