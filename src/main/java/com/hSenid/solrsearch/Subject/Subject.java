package com.hSenid.solrsearch.Subject;

import com.hSenid.solrsearch.Observer.Observer;

import java.util.ArrayList;

public class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();
    private ArrayList<String> DBRows = new ArrayList<>();

    public ArrayList<String> getDBRows() {
        return DBRows;
    }

    public void setDBRows(ArrayList<String> _DBRows) {
        this.DBRows = _DBRows;
        notifyAllObservers();
    }

    public void attach(Observer _o) {
        observers.add(_o);
    }

    public void notifyAllObservers() {
        observers.stream().forEach(observer -> observer.update());
    }

}
