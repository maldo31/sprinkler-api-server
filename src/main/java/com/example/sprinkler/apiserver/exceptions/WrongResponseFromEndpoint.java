package com.example.sprinkler.apiserver.exceptions;

public class WrongResponseFromEndpoint extends Exception{

    public WrongResponseFromEndpoint() {
        super();
    }

    public WrongResponseFromEndpoint(String message) {
        super(message);
    }

    public WrongResponseFromEndpoint(Throwable cause) {
        super(cause);
    }
}
