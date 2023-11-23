package hello.study.restapi.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EventTest {

	@Test
	void builder() {
		Event event = Event.builder().build();

		assertThat(event).isNotNull();
	}

	@Test
	void javaBean() {
		Event event = new Event();

		String name = "hello";
		String desc = "just Hello";

		event.setName(name);
		event.setDescription(desc);

		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(desc);
	}
}