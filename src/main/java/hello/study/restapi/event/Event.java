package hello.study.restapi.event;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private LocalDateTime beginEnrollmentDateTime;
	private LocalDateTime closeEnrollmentDateTime;
	private LocalDateTime beginEventDateTime;
	private LocalDateTime endEventDateTime;
	private String location;
	private int basePrice;
	private int maxPrice;
	private int limitOfEnrollment;
	private boolean offline;
	private boolean free;
	/*
	  Enumerated의 기본값은 EnumType.ORDINAL 인데 ORDINAL은 Enum의 순서에 따라서 index 값을 데이터베이스에 저장한다.
	  만약, Enum의 순서가 바뀐다거나 추가로 입력이 될경우 데이터의 정합성 문제를 야기할수있다. 그래서 String Type으로 저장하는게 더 좋다.

	  단점 : ORDINAL에 비해 약간 더 많은 저장 공간을 차지하고, 문자열 비교가 숫자 비교보다는 느리다
	 */
	@Enumerated(EnumType.STRING)
	private EventStatus eventStatus;
}