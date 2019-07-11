package com.hSenid.solrsearch;

import com.hSenid.solrsearch.Dao.CSVDataDao;
import com.hSenid.solrsearch.Functions.SearchLogics;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.Iterator;

/**
 * Hello world!
 */
public class App {
    public static int c = 0;
    public static SearchLogics searchLogics = new SearchLogics();

    public static void main(String[] args) {

//            QueryResponse reader = csvDataDao.reader("sms:hi", "score,*", "", "0", "1", "", "", "on", "csv_core2");
        int start = 0;
        QueryResponse csv_core2 = searchLogics.searchDateRange("sms:hello -sms:apu",
                "datetime:[2019-03-01T00:00:00Z TO 2019-03-07T00:00:00Z]",
                start,
                100,
                "csv_core2");

        SolrDocumentList results = csv_core2.getResults();
        long numFound = results.getNumFound();

        processData(results);

        if (numFound > 100) {
            for (int i = 0; i < (numFound / 100); i++) {
                start += 100;
                QueryResponse csv_coreIN = searchLogics.searchDateRange("sms:hello -sms:apu",
                        "datetime:[2019-03-01T00:00:00Z TO 2019-03-07T00:00:00Z]",
                        start,
                        100,
                        "csv_core2");
                SolrDocumentList results1 = csv_coreIN.getResults();
                processData(results1);

            }
        }

        System.out.println(c);


        //todo itterate 1 byone and search dplicates


        //todo store details in db


        System.out.println();

    }

    private static void processData(SolrDocumentList results) {
        Iterator iterator = results.iterator();
        while (iterator.hasNext()) {
            SolrDocument a = (SolrDocument) iterator.next();
            c++;
            String sms = a.getFieldValue("sms").toString();
            String app_id = a.getFieldValue("app_id").toString();
            sms = sms.replaceAll("\\\\", " ");
            sms = sms.replace(":", " ");
            sms = sms.replaceAll("\n", " ");
            sms = sms.replaceAll("\\.", " ");
            String[] s = sms.split(" ");
            StringBuilder stringBuilder = new StringBuilder();
            for (String split : s) {
                if (!split.equals("")) {
                    stringBuilder.append("+" + split + " ");
                }
            }
            String q = "sms:(" + stringBuilder + ") AND app_id:" + app_id;
            System.out.println();
//            QueryResponse csv_core2 = searchLogics.searchDateRange("sms:("+stringBuilder,
//                    "datetime:[2019-03-01T00:00:00Z TO 2019-03-07T00:00:00Z]",
//                    start,
//                    100,
//                    "csv_core2");

        }
    }
}
