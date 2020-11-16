package me.study.demoinflearnrestapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
// RepresentationModel을 사용해서 처리해도된다. @JSonUnWrapped 을 사용함
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
