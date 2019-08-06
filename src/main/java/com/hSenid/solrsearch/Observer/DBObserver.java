package com.hSenid.solrsearch.Observer;

import com.hSenid.solrsearch.Dao.DetailedDuplicateDao;
import com.hSenid.solrsearch.Subject.Subject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DBObserver extends Observer {

    private static final Logger LOGGER = Logger.getLogger(DBObserver.class.getName());
    private static final String DB = "1d1730D101D102";
    private static DetailedDuplicateDao detailedDuplicateDao = new DetailedDuplicateDao();

    public DBObserver(Subject subject) {
        this.subject = subject;
        subject.attach(this);
    }

    @Override
    public void update() {
        LOGGER.log(Level.INFO, "Updated " + subject.getDBRows().size());
        LOGGER.log(Level.INFO, "Adding to DB");
        subject.getDBRows().forEach(e -> detailedDuplicateDao.set(e, DB));
    }

}
