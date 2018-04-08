package com.project.backend.persistence.repositories;

import com.project.backend.persistence.domain.backend.Credits;
import org.springframework.data.repository.CrudRepository;


/**
 * Created by zen on 19/08/17.
 */
public interface CreditsRepository extends CrudRepository<Credits, Long> {
}
