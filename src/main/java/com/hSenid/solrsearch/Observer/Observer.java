package com.hSenid.solrsearch.Observer;

import com.hSenid.solrsearch.Subject.Subject;

public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
