package hello.study.restapi.event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import hello.study.restapi.common.ErrorsResource;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
			return badRequest(errors);
		}

		eventValidator.validate(eventDto, errors);
		if (errors.hasErrors()) {
			return badRequest(errors);
		}

		Event event = modelMapper.map(eventDto, Event.class);
		event.update();

		Event newEvent = eventRepository.save(event);
		WebMvcLinkBuilder selfLink = linkTo(EventController.class).slash(newEvent.getId());
		URI createUri = selfLink.toUri();

		EventResource eventResource = new EventResource(event);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLink.withRel("update-event"));
		eventResource.add((Link.of("/docs/index.html#resources-events-create").withRel("profile")));

		return ResponseEntity.created(createUri).body(eventResource);
	}

	@GetMapping()
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> page = eventRepository.findAll(pageable);
		var pagedModel = assembler.toModel(page, EventResource::new);
		pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));

		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id) {
		Optional<Event> optionalEvent = eventRepository.findById(id);

		if (optionalEvent.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Event event = optionalEvent.get();
		EventResource eventResource = new EventResource(event);
		eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
		return ResponseEntity.ok(eventResource);
	}

	private static ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(ErrorsResource.modelOf(errors));
	}
}