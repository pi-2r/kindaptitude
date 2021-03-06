package com.project.backend.persistence.repositories;

import com.project.backend.persistence.domain.backend.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zen on 17/08/17.
 */
@Repository
@Transactional(readOnly = true)
public interface PaymentRepository extends CrudRepository<Payment, Long> {


}
