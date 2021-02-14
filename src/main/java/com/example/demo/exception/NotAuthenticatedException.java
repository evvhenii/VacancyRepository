package com.example.demo.exception;

public class NotAuthenticatedException extends Exception{
    public NotAuthenticatedException(){
        super("User is not authenticated");
    }
}
