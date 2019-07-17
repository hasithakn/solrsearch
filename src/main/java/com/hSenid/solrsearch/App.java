package com.hSenid.solrsearch;

import com.hSenid.solrsearch.Dao.AnalysisDao;
import com.hSenid.solrsearch.Entity.AnalysedData;
import com.hSenid.solrsearch.Functions.SearchDuplicates;
import com.hSenid.solrsearch.Functions.SearchLogics;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Hello world!
 */
public class App {
    public static int c = 0;
    public static SearchLogics searchLogics = new SearchLogics();
    public static SearchDuplicates searchDuplicates = new SearchDuplicates();
    public static AnalysisDao analysisDao = new AnalysisDao();
    public static final String CORE = "experimentCore";
    public static final String dateRangeToSearchFormDup = "datetime:[2019-03-01T00:00:00Z TO 2019-03-01T23:59:59Z]";
    public static final String dateRangeToSearchINDup = "datetime:[2019-02-14T00:00:00Z TO 2019-02-28T00:00:00Z]";
    public static HashMap<String, Long[]> appIDvsDupMap = new HashMap<>();

    public static void main(String[] args) {

        int start = 0;
        final int batch = 10000;
        final String q = "sms:*";

        SolrDocumentList results = executeQ(start, dateRangeToSearchFormDup, q, batch);
        long numFound = results.getNumFound();
        int noOfTimes = (int) (numFound / batch);
//        if (results != null) {
//            processData(results);
//        }

        final SolrDocumentList[] solrDocumentLists = new SolrDocumentList[noOfTimes + 1];
        Thread[] ts = new Thread[noOfTimes + 1];

        solrDocumentLists[0] = results;
        if (numFound > batch) {
            for (int i = 0; i < noOfTimes; i++) {
                start += batch;
                final SolrDocumentList results1 = executeQ(start, dateRangeToSearchFormDup, q, batch);
                if (results1 != null) {
                    solrDocumentLists[i + 1] = results1;
                }
            }
        }

        for (int i = 0; i < noOfTimes + 1; i++) {
            final int temp = i;
            ts[i] = new Thread() {
                public void run() {
                    processData(solrDocumentLists[temp]);
                }
            };
        }
        for (int i = 0; i < noOfTimes + 1; i++) {
            ts[i].start();
        }

        //todo store details in db

    }

    private static SolrDocumentList executeQ(int start, String dateRangeAS_fq, String q, int batch) {
        QueryResponse csv_core2 = null;
        try {
            csv_core2 = searchLogics.searchDateRange(q,
                    dateRangeAS_fq,
                    "",
                    start,
                    batch,
                    CORE);
            System.out.println("executed for " + batch + " from " + start);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return csv_core2.getResults();
    }

    private static void processData(SolrDocumentList results) {
        Iterator iterator = results.iterator();
        while (iterator.hasNext()) {
            SolrDocument a = (SolrDocument) iterator.next();
//            synchronized (App.class) {
            c++;
            Long[] longs = searchDuplicates.searchForDuplicates(a, dateRangeToSearchINDup, CORE, searchLogics);
            AnalysedData analysedData = new AnalysedData();
            analysedData.setApp_id(a.getFieldValue("app_id").toString());
            analysedData.setDoc_id(a.getFieldValue("id").toString());
            analysedData.setD101((int) (long) longs[0]);
            analysedData.setD102((int) (long) longs[1]);
            analysedData.setD103((int) (long) longs[2]);
            analysedData.setD104((int) (long) longs[3]);
            analysisDao.set(analysedData, "analysis3");
            analysedData = null;
//            System.out.print(c + " " + a.getFieldValue("app_id").toString() + " : " + longs[0] + " ");
//            System.out.print(longs[1] + " ");
//            System.out.print(longs[2] + " ");
//            System.out.println(longs[3] + " " + Thread.currentThread().getName());
//            }


        }
    }


}
