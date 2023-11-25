package hello.study.restapi.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

	@ParameterizedTest
	@CsvSource({
		"0, 0, true",
		"0, 100, false",
		"100, 0, false"
	})
	void testFree(int basePrice, int maxPrice, boolean isFree) {
		Event event = Event.builder()
			.basePrice(basePrice)
			.maxPrice(maxPrice)
			.build();

		event.update();

		assertThat(event.isFree()).isEqualTo(isFree);
	}

	@ParameterizedTest
	@CsvSource({
		"Kim's house, true",
		", false",
		" , false"
	})
	void testOffline(String location, boolean isOffline) {
		Event event = Event.builder()
			.location(location)
			.build();

		event.update();

		assertThat(event.isOffline()).isEqualTo(isOffline);
	}
}
