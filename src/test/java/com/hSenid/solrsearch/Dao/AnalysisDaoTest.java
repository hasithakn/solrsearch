package com.hSenid.solrsearch.Dao;

import com.hSenid.solrsearch.Entity.AnalysedData;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnalysisDaoTest {

    @Test
    public void set() {
        AnalysisDao analysisDao = new AnalysisDao();
        AnalysedData analysedData = new AnalysedData();
        analysedData.setApp_id("dfssfd");
        analysedData.setDoc_id("dfssfd");
        analysedData.setD101(1);
        analysedData.setD102(2);
        analysedData.setD103(3);
        analysedData.setD104(4);
        analysisDao.set(analysedData,"analysis1");
    }

    @Test
    public void getSize() {
    }
}