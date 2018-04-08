package com.project.backend.service;

import com.project.backend.persistence.domain.backend.Payment;
import com.project.backend.persistence.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zen on 18/08/17.
 */
@Service
@Transactional(readOnly = true)
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public void createPayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
