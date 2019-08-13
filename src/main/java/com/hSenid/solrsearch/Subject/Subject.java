package com.hSenid.solrsearch.Subject;

import com.hSenid.solrsearch.Entity.DetailedDuplicate;
import com.hSenid.solrsearch.Observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private ArrayList<Observer> observers = new ArrayList<>();
    private List<DetailedDuplicate> dbRows = new ArrayList<>();

    public List<DetailedDuplicate> getDBRows() {
        return dbRows;
    }

    public void setDBRows(List<DetailedDuplicate> dbRows) {
        this.dbRows = dbRows;
        notifyAllObservers();
    }

    public void attach(Observer o) {
        observers.add(o);
    }

    public void notifyAllObservers() {
        observers.forEach(Observer::update);
    }

}
