package hello.study.restapi.event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import jakarta.validation.Valid;
import java.net.URI;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// produces -> Response Headers / Content-Type
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;

	public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
		this.eventRepository = eventRepository;
		this.modelMapper = modelMapper;
		this.eventValidator = eventValidator;
	}

	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors.getAllErrors());
		}

		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors.getAllErrors());
		}

		Event event = modelMapper.map(eventDto, Event.class);
		event.update();

		Event newEvent = eventRepository.save(event);
		URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

		return ResponseEntity.created(createUri).body(event);
	}
}