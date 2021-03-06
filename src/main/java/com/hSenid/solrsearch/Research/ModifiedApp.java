package com.hSenid.solrsearch.Research;

import com.hSenid.solrsearch.Dao.AnalysisDao;
import com.hSenid.solrsearch.Entity.AnalysedData;
import com.hSenid.solrsearch.Entity.DocCountsResultsPair;
import com.hSenid.solrsearch.FileIO.FileIO;
import com.hSenid.solrsearch.Functions.SearchDuplicates;
import com.hSenid.solrsearch.Functions.SearchDuplicatesAdvance;
import com.hSenid.solrsearch.Functions.SearchLogics;
import com.hSenid.solrsearch.Functions.SearchingMethods;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ModifiedApp {

    private final static Logger LOGGER = Logger.getLogger(ModifiedApp.class.getName());

    public static final String APP_ID_PATH = "/home/hasithan/hsenid/analysis/AppId.csv";
    private static ArrayList<String> appids = new ArrayList<>();

    private static final SearchLogics searchLogics = new SearchLogics();
    private static final SearchDuplicatesAdvance SDA = new SearchDuplicatesAdvance(searchLogics);
    private static final SearchingMethods SM = new SearchingMethods(SDA);

    private static AnalysisDao analysisDao = new AnalysisDao();
    private static final String CORE = "experiment5";
    private static final String DB = "1d1mNew";


    private static final String DATE_RANGE_TO_SEARCH =
            "datetime: [2019-03-01T00:00:00Z TO 2019-03-02T00:00:00Z]";

    private static final String SEARCH_DUP_IN_THIS_RANGE =
            "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]";

    public static void main(String[] args) {
        try {
            appids = FileIO.getAppids(APP_ID_PATH);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, e.toString());
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
        } catch (IOException | SolrServerException e) {
            LOGGER.log(Level.WARNING, e.toString());
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
                        DocCountsResultsPair docCountsResultsPair = SM.searchWithinTimePeriod(
                                a,
                                "-1MONTHS",
                                "",
                                CORE);
                        AnalysedData analysedData = new AnalysedData();
                        long[] longs = docCountsResultsPair.getLongs();
                        analysedData.setApp_id(a.getFieldValue("app_id").toString());
                        analysedData.setDoc_id(a.getFieldValue("timestamp").toString());
                        analysedData.setD101((int) longs[0]);
                        analysedData.setD102((int) longs[1]);
                        analysedData.setD103((int) longs[2]);
                        analysedData.setD104((int) longs[3]);
                        LOGGER.log(Level.INFO, analysedData.getDoc_id() + " : " + longs[0] + "," + longs[1] + "," + longs[2] + "," + longs[3]);
                        analysisDao.set(analysedData, DB);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, e.toString());
                    }
                });
    }
}
