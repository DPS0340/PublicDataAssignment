package com.example.publicdataassignment;

public interface Observerble {
    void add(Observer observer);
    void remove(Observer observer);
    void notice();
}
