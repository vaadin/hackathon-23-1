package com.vaadin.hackathon.manolo.data.endpoint;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hackathon.manolo.data.entity.SamplePerson;
import com.vaadin.hackathon.manolo.data.service.SamplePersonService;

import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Endpoint
@AnonymousAllowed
public class SamplePersonEndpoint {

    private final SamplePersonService service;
    private Many<UUID> sink = Sinks.many().multicast().directBestEffort();

    @Autowired
    public SamplePersonEndpoint(SamplePersonService service) {
        this.service = service;
    }

    @Nonnull
    public Page<@Nonnull SamplePerson> list(Pageable page) {
        return service.list(page);
    }

    public Optional<SamplePerson> get(@Nonnull UUID id) {
        return service.get(id);
    }

    @Nonnull
    public SamplePerson update(@Nonnull SamplePerson entity) {
        entity = service.update(entity);
        sink.tryEmitNext(entity.getId());
        return entity;
    }

    public void delete(@Nonnull UUID id) {
        sink.tryEmitNext(id);
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

    @Nonnull
    public Flux<UUID> personUpdated() {
        return sink.asFlux();
    }
}
