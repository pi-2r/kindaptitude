package com.project.backend.persistence.repositories;

import com.project.backend.persistence.domain.backend.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zen on 14/05/17.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
