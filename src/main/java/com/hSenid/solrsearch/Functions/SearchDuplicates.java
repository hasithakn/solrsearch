package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Entity.DocCountResultsPair;
import com.hSenid.solrsearch.Entity.DocCountsResultsPair;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SearchDuplicates {

    public SolrDocumentList searchWithinTimePeriod(
            SolrDocument a,
            String timeFilter1,
            String timeFilter2,
            String CORE,
            SearchLogics searchLogics
    ) {
        Date tempDate = (Date) a.getFieldValue("datetime");
        String timeTemp = TimeFunctions.timestampToISO(tempDate);
        String id = a.getFieldValue("id").toString();

        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(timeTemp);
        temp.append(timeFilter1);
        temp.append(" TO ");
        temp.append(timeTemp + timeFilter2 + " ]");


        System.out.println(temp.toString());

        SolrDocumentList solrDocuments = searchForDuplicates(a, temp.toString(), CORE, searchLogics).getSolrDocuments();
        return solrDocuments;

    }



    public DocCountsResultsPair searchForDuplicates(
            SolrDocument a,
            String dateRangeToSearchINDup,
            String CORE,
            SearchLogics searchLogics
    ) {

        DocCountsResultsPair docCountsResultsPair = new DocCountsResultsPair();

        long[] longs = new long[4];
        //-1 for exception
        //0 for not found
        //>0 for matches

        DocCountResultsPair D101 = searchForDuplicatesD101(
                a,
                dateRangeToSearchINDup,
                CORE,
                searchLogics);

        docCountsResultsPair.setSolrDocuments(D101.getSolrDocuments());

        if (D101.getDocCount() <= 0) {
            DocCountResultsPair D102 = searchForDuplicatesD102(
                    a,
                    dateRangeToSearchINDup,
                    CORE,
                    searchLogics);
            docCountsResultsPair.setSolrDocuments(D102.getSolrDocuments());
            if (D102.getDocCount() <= 0) {
                DocCountResultsPair[] D103andD104 = searchForDuplicatesD103andD104(
                        a,
                        dateRangeToSearchINDup,
                        CORE,
                        searchLogics);
                longs[0] = D101.getDocCount();
                longs[1] = D102.getDocCount();
                longs[2] = D103andD104[0].getDocCount();
                longs[3] = D103andD104[1].getDocCount();

                if (D103andD104[0].getSolrDocuments() != null) {
                    docCountsResultsPair.setSolrDocuments(D103andD104[0].getSolrDocuments());
                } else {
                    docCountsResultsPair.setSolrDocuments(D103andD104[1].getSolrDocuments());
                }

            } else {
                longs[0] = D101.getDocCount();
                longs[1] = D102.getDocCount();
                longs[2] = -1L;
                longs[3] = -1L;
            }
        } else {
            longs[0] = D101.getDocCount();
            longs[1] = -1L;
            longs[2] = -1L;
            longs[3] = -1L;
        }
        docCountsResultsPair.setLongs(longs);
        return docCountsResultsPair;
    }

    private DocCountResultsPair searchForDuplicatesD101(
            SolrDocument a,
            String dateRange,
            String core,
            SearchLogics searchLogics
    ) {

        DocCountResultsPair resultsPair = new DocCountResultsPair();

        String sms = a.getFieldValue("sms").toString();
        String app_id = a.getFieldValue("app_id").toString();
        dateRange = dateRange + " AND app_id:" + app_id;
        sms = sms.replaceAll("\"", "\\\\\"");
        String q = "\"" + sms + "\"";
        long numFound = -1;

        resultsPair.setDocCount(numFound);
        resultsPair.setSolrDocuments(null);
        try {
            QueryResponse searchDocs = searchLogics.searchDateRange(q,
                    dateRange,
                    "sms",
                    0,
                    1,
                    core);
            numFound = searchDocs.getResults().getNumFound();
            resultsPair.setDocCount(numFound);
            if (numFound > 0) {
                resultsPair.setSolrDocuments(searchDocs.getResults());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (HttpSolrClient.RemoteSolrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsPair;
    }

    private DocCountResultsPair searchForDuplicatesD102(
            SolrDocument a,
            String dateRange,
            String core,
            SearchLogics searchLogics
    ) {

        DocCountResultsPair resultsPair = new DocCountResultsPair();

        String sms = a.getFieldValue("sms").toString();
        sms = sms.replaceAll("\"", "\\\\\"");
        String app_id = a.getFieldValue("app_id").toString();
        dateRange = dateRange + " AND app_id:" + app_id;
        String q = "\"" + sms + "\"";
        QueryResponse searchDocs = null;
        long numFound = -1;

        resultsPair.setDocCount(numFound);
        resultsPair.setSolrDocuments(null);
        try {
            searchDocs = searchLogics.searchDateRange(q,
                    dateRange,
                    "smsNoPunctuations",
                    0,
                    1,
                    core);

            numFound = searchDocs.getResults().getNumFound();
            resultsPair.setDocCount(numFound);
            if (numFound > 0) {
                resultsPair.setSolrDocuments(searchDocs.getResults());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (HttpSolrClient.RemoteSolrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultsPair;
    }

    private DocCountResultsPair[] searchForDuplicatesD103andD104(
            SolrDocument a,
            String dateRange,
            String core,
            SearchLogics searchLogics
    ) {

        DocCountResultsPair[] docCountResultsPairs = new DocCountResultsPair[2];

        DocCountResultsPair resultsPairD103 = new DocCountResultsPair();
        DocCountResultsPair resultsPairD104 = new DocCountResultsPair();

        String sms = a.getFieldValue("sms").toString();
        sms = sms.replaceAll(":", " ");
        sms = sms.replaceAll("\"", " ");
        String app_id = a.getFieldValue("app_id").toString();
        dateRange = dateRange + " AND app_id:" + app_id;
        String q = "( " + sms + " )";
        QueryResponse searchDocs = null;
        int length = sms.split(" ").length;

        long numFoundD103 = -1;
        resultsPairD103.setDocCount(numFoundD103);
        resultsPairD103.setSolrDocuments(null);
        resultsPairD104.setDocCount(-1);
        resultsPairD104.setSolrDocuments(null);

        try {
            searchDocs = searchLogics.searchDateRangeDismax(q,
                    dateRange,
                    "smsEN",
                    0,
                    1,
                    Integer.toString(length),
                    core);
            numFoundD103 = searchDocs.getResults().getNumFound();
            resultsPairD103.setDocCount(numFoundD103);
            resultsPairD103.setSolrDocuments(searchDocs.getResults());

            if (numFoundD103 <= 0) {
                String parsedquery_toString = searchDocs.getDebugMap().get("parsedquery_toString").toString();
                int totTerms = 0;
                try {
                    totTerms = getTotTerms(length, parsedquery_toString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long numFoundD104 = -1;
                searchDocs = searchLogics.searchDateRangeDismax(q,
                        dateRange,
                        "smsEN",
                        0,
                        1,
                        Integer.toString(totTerms),
                        core);

                numFoundD104 = searchDocs.getResults().getNumFound();
                resultsPairD104.setDocCount(numFoundD104);
                resultsPairD104.setSolrDocuments(searchDocs.getResults());
            } else {
                resultsPairD104.setDocCount(-1);
                resultsPairD104.setSolrDocuments(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (HttpSolrClient.RemoteSolrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        docCountResultsPairs[0] = resultsPairD103;
        docCountResultsPairs[1] = resultsPairD104;

        return docCountResultsPairs;
    }

    private int getTotTerms(int length, String parsedquery_toString) {
        int totTerms = -1;
        if (length > 1) {
            try {
                int i = parsedquery_toString.indexOf("~", 0);
                int j = parsedquery_toString.indexOf(")", i);
                String terms = parsedquery_toString.substring(i + 1, j);
                totTerms = Integer.parseInt(terms);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                throw e;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }

            if (totTerms == -1) {
                totTerms = length - 1;
            } else {
                totTerms = totTerms - 1;
            }
            return totTerms;
        } else {
            return 0;
        }
    }
}
