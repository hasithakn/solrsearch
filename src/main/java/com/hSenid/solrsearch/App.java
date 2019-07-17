package com.hSenid.solrsearch;

import com.hSenid.solrsearch.Functions.SearchDuplicates;
import com.hSenid.solrsearch.Functions.SearchLogics;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Hello world!
 */
public class App {
    public static int c = 0;
    public static SearchLogics searchLogics = new SearchLogics();
    public static SearchDuplicates searchDuplicates = new SearchDuplicates();
    public static final String CORE = "experimentCore";
    public static String dateRangeToSearchFormDup = "datetime:[2019-03-01T00:00:00Z TO 2019-03-01T01:00:00Z]";
    public static String dateRangeToSearchINDup = "datetime:[2019-02-21T00:00:00Z TO 2019-02-28T00:00:00Z]";
    public static HashMap<String, Long[]> appIDvsDupMap = new HashMap<>();

    public static void main(String[] args) {

        int start = 0;
        final int batch = 200;
        final String q = "sms:*";

        SolrDocumentList results = executeQ(start, dateRangeToSearchFormDup, q, batch);
        long numFound = results.getNumFound();
        long noOfTimes = numFound / batch;
        if (results != null) {
            processData(results);
        }

        if (numFound > batch) {
            for (int i = 0; i < noOfTimes; i++) {
                start += batch;
                SolrDocumentList results1 = executeQ(start, dateRangeToSearchFormDup, q, batch);
                if (results1 != null) {
                    processData(results1);
                }
            }
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
            System.out.println("executed for 100 from " + start);
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
            appIDvsDupMap.put(a.getFieldValue("app_id").toString(), longs);
            System.out.print(c + " " + a.getFieldValue("app_id").toString() + " : " + longs[0] + " ");
            System.out.print(longs[1] + " ");
            System.out.print(longs[2] + " ");
            System.out.println(longs[3] + " " + Thread.currentThread().getName());
//            }

        }
    }


}
