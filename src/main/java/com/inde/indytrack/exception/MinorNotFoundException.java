package com.inde.indytrack.exception;

public class MinorNotFoundException extends RuntimeException {
    public MinorNotFoundException(String name) {
        super("Could not find the " + name + " minor");
    }
}
