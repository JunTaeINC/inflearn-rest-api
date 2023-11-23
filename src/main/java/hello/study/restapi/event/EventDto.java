package hello.study.restapi.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	@NotNull
	private LocalDateTime beginEnrollmentDateTime;
	@NotNull
	private LocalDateTime closeEnrollmentDateTime;
	@NotNull
	private LocalDateTime beginEventDateTime;
	@NotNull
	private LocalDateTime endEventDateTime;
	private String location;
	@Min(0)
	private int basePrice;
	@Min(0)
	private int maxPrice;
	@Min(0)
	private int limitOfEnrollment;
}