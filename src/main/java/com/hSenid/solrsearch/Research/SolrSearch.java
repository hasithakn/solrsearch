package com.hSenid.solrsearch.Research;

import com.hSenid.solrsearch.Dao.AnalysisDao;
import com.hSenid.solrsearch.Dao.DetailedDuplicateDao;
import com.hSenid.solrsearch.Entity.DetailedDuplicate;
import com.hSenid.solrsearch.FileIO.FileIO;
import com.hSenid.solrsearch.Functions.SearchDuplicates;
import com.hSenid.solrsearch.Functions.SearchDuplicatesAdvance;
import com.hSenid.solrsearch.Functions.SearchLogics;
import com.hSenid.solrsearch.Functions.TimeFunctions;
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

public class SolrSearch {
    private final static Logger LOGGER = Logger.getLogger(SolrSearch.class.getName());

    public static final String APP_ID_PATH = "/home/hasithan/hsenid/analysis/AppId.csv";
    private static ArrayList<String> appids = new ArrayList<>();
    private static SearchLogics searchLogics = new SearchLogics();
    private static SearchDuplicatesAdvance searchDuplicatesAdvance = new SearchDuplicatesAdvance(searchLogics);

    private static DetailedDuplicateDao ddDao = new DetailedDuplicateDao();
    private static final String CORE = "experiment5";
    private static final String DB = "1d1730D104";

    private static final String DATE_RANGE_TO_SEARCH =
            "datetime: [2019-03-01T00:00:00Z TO 2019-03-02T00:00:00Z]";

    private static final String SEARCH_DUP_IN_THIS_RANGE =
            "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]";

    private static final String D1 = "2019-03-01T00:00:00Z";
    private static final String D7 = "2019-02-23T00:00:00Z";
    private static final String D30 = "2019-01-31T00:00:00Z";


    public static void main(String[] args) {
        try {
            appids = FileIO.getAppids(APP_ID_PATH);
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
//
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
                .filter(csvSolr -> appids.contains(csvSolr.getFieldValue("app_id").toString()))
                .collect(Collectors.toList());

        filteredList.stream()
                .forEach(a -> {
                    try {
                        int d1 = 0;
                        int d7 = 0;
                        int d30 = 0;
                        JSONObject details = new JSONObject();

                        String s = TimeFunctions.addTimeFilterDateXTillDocTime(a, D1);
                        QueryResponse response = searchDuplicatesAdvance.searchD104(a, s, CORE);
                        List<String> d1List = response.getResults().stream()
                                .map(e -> e.getFieldValue("timestamp").toString())
                                .collect(Collectors.toList());
                        d1 = (int) response.getResults().getNumFound();
                        details.put("d1", d1List);


                        s = TimeFunctions.getDateTimeWithXY(D7, D1);
                        response = searchDuplicatesAdvance.searchD104(a, s, CORE);
                        List<String> d7List = response.getResults().stream()
                                .map(e -> e.getFieldValue("timestamp").toString())
                                .collect(Collectors.toList());
                        d7 = (int) response.getResults().getNumFound();
                        details.put("d7", d7List);

                        s = TimeFunctions.getDateTimeWithXY(D30, D7);
                        response = searchDuplicatesAdvance.searchD104(a, s, CORE);
                        List<String> d30List = response.getResults().stream()
                                .map(e -> e.getFieldValue("timestamp").toString())
                                .collect(Collectors.toList());
                        d30 = (int) response.getResults().getNumFound();
                        details.put("d30", d30List);

                        DetailedDuplicate dd = new DetailedDuplicate();
                        dd.setApp_id(a.getFieldValue("app_id").toString());
                        dd.setTimestamp(a.getFieldValue("timestamp").toString());
                        dd.setD1(d1);
                        dd.setD7(d7);
                        dd.setD30(d30);
                        dd.setDetails(details.toString());
                        ddDao.set(dd, DB);
                        LOGGER.log(Level.INFO, dd.getTimestamp() + " -- " + d1 + " : " + d7 + " : " + d30);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, e.toString());
                    }
                });
    }
}
