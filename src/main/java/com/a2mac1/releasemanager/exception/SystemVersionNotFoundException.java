package com.a2mac1.releasemanager.exception;

public class SystemVersionNotFoundException extends RuntimeException {

    public SystemVersionNotFoundException(int version) {
        super("System version " + version + " was not found");
    }
}
