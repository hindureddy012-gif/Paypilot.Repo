package com.paypilot.exception;

public class BillNotFoundException extends RuntimeException {

    public BillNotFoundException(Long id) {

        super("Bill not found with id: " + id);
    }
}