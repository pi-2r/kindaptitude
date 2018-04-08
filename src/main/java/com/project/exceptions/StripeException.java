package com.project.exceptions;

/**
 * Created by zen on 30/07/17.
 */
public class StripeException extends RuntimeException {

    public StripeException(Throwable e) {
        super(e);
    }
}