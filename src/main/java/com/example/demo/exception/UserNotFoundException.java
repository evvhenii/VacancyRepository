package com.example.demo.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("User is not found");
    }
}
