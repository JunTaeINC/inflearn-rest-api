package hello.study.restapi.event;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;

public class EventResource extends EntityModel<Event> {

	public EventResource(Event event) {
		super(event);
		add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
	}
}