package com.example.demo.exception;

public class NotPermittedActionException extends Exception{
    public NotPermittedActionException(){
        super("Permitted action");
    }
}
