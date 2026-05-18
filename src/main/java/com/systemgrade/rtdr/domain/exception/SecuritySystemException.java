package com.systemgrade.rtdr.domain.exception;

public class SecuritySystemException extends RuntimeException {

    public SecuritySystemException(String message) {
        super(message);
    }

    public SecuritySystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public static final class DataAccess extends SecuritySystemException {
        public DataAccess(String message, Throwable cause) { super(message, cause); }
    }

    public static final class Detection extends SecuritySystemException {
        public Detection(String message) { super(message); }
    }

    public static final class Enforcement extends SecuritySystemException {
        public Enforcement(String message, Throwable cause) { super(message, cause); }
    }
}
