package com.hSenid.solrsearch.Observer;

import com.hSenid.solrsearch.Subject.Subject;

public class DBObserver extends Observer {


    public DBObserver(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void update() {
        System.out.println("Updated " + subject.getDBRows().size());
    }
}
