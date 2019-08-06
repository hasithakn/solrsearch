package com.hSenid.solrsearch.Research;

import com.hSenid.solrsearch.Entity.DetailedDuplicate;
import com.hSenid.solrsearch.FileIO.FileIO;
import com.hSenid.solrsearch.Functions.SearchDuplicatesAdvance;
import com.hSenid.solrsearch.Functions.SearchLogics;
import com.hSenid.solrsearch.Functions.SearchingMethods;
import com.hSenid.solrsearch.Functions.TimeFunctions;
import com.hSenid.solrsearch.Observer.DBObserver;
import com.hSenid.solrsearch.Subject.Subject;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This is to find duplicates on D101 and if not d101 then D102, (ignoring D103 and D104)
 */
public class SolrSearchV2 {

    private final static Logger LOGGER = Logger.getLogger(SolrSearch.class.getName());

    public static final String APP_ID_PATH = "/home/hasitha/hSenid/analysis/AppId.csv";
    private static ArrayList<String> appIdFilter = new ArrayList<>();

    private static ArrayList<DetailedDuplicate> detailedDuplicates = new ArrayList<>();

    private static final SearchLogics searchLogics = new SearchLogics();
    private static final SearchDuplicatesAdvance SDA = new SearchDuplicatesAdvance(searchLogics);
    private static final SearchingMethods SM = new SearchingMethods(SDA);

    private static final String CORE = "experiment3";

    private static final String DATE_RANGE_TO_SEARCH =
            "datetime: [2019-03-01T00:00:00Z TO 2019-03-02T00:00:00Z]";

    private static final String D1 = "2019-03-01T00:00:00Z";
    private static final String D7 = "2019-02-23T00:00:00Z";
    private static final String D30 = "2019-01-31T00:00:00Z";

    public static void main(String[] args) throws InterruptedException {

        Subject subject = new Subject();
        DBObserver dbObserver = new DBObserver(subject);

        try {
            appIdFilter = FileIO.getAppids(APP_ID_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int start = 0;
        final int batch = 10000;
        final String q = "sms:*";

        SolrDocumentList results = executeQ(start, DATE_RANGE_TO_SEARCH, q, batch);
        long numFound = results.getNumFound();

        int noOfTimes = (int) (numFound / batch);
        final SolrDocumentList[] solrDocumentLists = new SolrDocumentList[noOfTimes + 1];

        Thread[] ts = new Thread[noOfTimes + 1];

        solrDocumentLists[0] = results;
        if (numFound > batch) {
            for (int i = 0; i < noOfTimes; i++) {
                start += batch;
                final SolrDocumentList results1 = executeQ(start, DATE_RANGE_TO_SEARCH, q, batch);
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

        for (int i = 0; i < noOfTimes + 1; i++) {
            ts[i].join();
        }

        subject.setDBRows(detailedDuplicates);


    }

    private static SolrDocumentList executeQ(
            int start,
            String dateRangeAS_fq,
            String q,
            int batch
    ) {
        QueryResponse csv_core2 = null;
        try {
            csv_core2 = searchLogics.searchDateRange(q,
                    dateRangeAS_fq,
                    "",
                    start,
                    batch,
                    CORE);

            LOGGER.log(Level.INFO, "executed for " + batch + " from " + start);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return csv_core2.getResults();
    }

    private static void processData(SolrDocumentList results) {
        List<SolrDocument> filteredList = results.stream().parallel()
                .filter(csvSolr -> appIdFilter.contains(csvSolr.getFieldValue("app_id").toString()))
                .collect(Collectors.toList());

        filteredList.stream()
                .forEach(a -> {
                    try {
                        int d1 = 0;
                        int d7 = 0;
                        int d30 = 0;
                        JSONObject details = new JSONObject();

                        String s = TimeFunctions.addTimeFilterDateXTillDocTime(a, D1);
                        QueryResponse response = SM.searchForDuplicatesD101D102(a, s, CORE);
                        List<String> d1List = response.getResults().stream()
                                .map(e -> e.getFieldValue("timestamp").toString())
                                .collect(Collectors.toList());
                        d1 = (int) response.getResults().getNumFound();
                        details.put("d1", d1List);

                        s = TimeFunctions.getDateTimeWithXY(D7, D1);
                        response = SM.searchForDuplicatesD101D102(a, s, CORE);
                        List<String> d7List = response.getResults().stream()
                                .map(e -> e.getFieldValue("timestamp").toString())
                                .collect(Collectors.toList());
                        d7 = (int) response.getResults().getNumFound();
                        details.put("d7", d7List);

                        s = TimeFunctions.getDateTimeWithXY(D30, D7);
                        response = SM.searchForDuplicatesD101D102(a, s, CORE);
                        List<String> d30List = response.getResults().stream()
                                .map(e -> e.getFieldValue("timestamp").toString())
                                .collect(Collectors.toList());
                        d30 = (int) response.getResults().getNumFound();
                        details.put("d30", d30List);

                        DetailedDuplicate detailedDuplicate = new DetailedDuplicate();
                        detailedDuplicate.setApp_id(a.getFieldValue("app_id").toString());
                        detailedDuplicate.setTimestamp(a.getFieldValue("timestamp").toString());
                        detailedDuplicate.setD1(d1);
                        detailedDuplicate.setD7(d7);
                        detailedDuplicate.setD30(d30);
                        detailedDuplicate.setDetails(details.toString());

                        detailedDuplicates.add(detailedDuplicate);
                        LOGGER.log(Level.INFO,
                                detailedDuplicate.getTimestamp() + " -- " + d1 + " : " + d7 + " : " + d30
                        );
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, e.toString());
                    }
                });
    }
}

