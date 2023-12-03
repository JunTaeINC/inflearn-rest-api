package hello.study.restapi.common;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import hello.study.restapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;

public class ErrorsResource extends EntityModel<Errors> {

	public static EntityModel<Errors> modelOf(Errors errors) {
		EntityModel<Errors> errorsModel = EntityModel.of(errors);
		errorsModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
		return errorsModel;
	}
}