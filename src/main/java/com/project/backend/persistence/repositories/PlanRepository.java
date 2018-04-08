package com.project.backend.persistence.repositories;

import com.project.backend.persistence.domain.backend.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zen on 14/05/17.
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer> {
}
