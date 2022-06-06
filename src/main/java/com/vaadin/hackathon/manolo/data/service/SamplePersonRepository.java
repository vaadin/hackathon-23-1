package com.vaadin.hackathon.manolo.data.service;

import com.vaadin.hackathon.manolo.data.entity.SamplePerson;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

}