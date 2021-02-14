package com.example.demo.exception;

public class VacancyNotFoundException extends Exception{
    public  VacancyNotFoundException(){
        super("Vacancy is not found");
    }
}
