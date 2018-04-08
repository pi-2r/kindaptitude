package com.project.backend.service;

import com.project.backend.persistence.domain.backend.PasswordResetToken;
import com.project.backend.persistence.domain.backend.User;
import com.project.backend.persistence.repositories.PasswordResetTokenRepository;
import com.project.backend.persistence.repositories.UserRepository;
import com.project.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;


/**
 * Created by zen on 01/05/17.
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private Tools tools;

    @Value("${token.expiration.length.minutes}")
    private int tokenExpirationInMinutes;

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);

    /**
     * Creates a new Password Reset Token for the user identified by the given email.
     * @param email The email uniquely identifying the user
     * @return a new Password Reset Token for the user identified by the given email or null if none was found
     */
    @Transactional
    public PasswordResetToken createPasswordResetTokenForEmail(String email) {

        PasswordResetToken passwordResetToken = null;

        User user = userRepository.findByEmail(email);

        if (null != user) {

            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordResetToken = new PasswordResetToken(tools.getToken(), user, now, tokenExpirationInMinutes);

            passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
            LOG.debug("Successfully created token {}  for user {}", tools.getToken(), user.getUsername());
        } else {
            LOG.warn("We couldn't find a user for the given email {}", email);
        }

        return passwordResetToken;

    }

    /**
     * Retrieves a Password Reset Token for the given token id.
     * @param token The token to be returned
     * @return A Password Reset Token if one was found or null if none was found.
     */
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

}
