package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Entity.DocCountsResultsPair;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;

public class SearchDuplicatesAdvance {

    public SearchLogics searchLogics;

    public SearchDuplicatesAdvance(SearchLogics searchLogics) {
        this.searchLogics = searchLogics;
    }

    public DocCountsResultsPair searchWithinTimePeriod(
            SolrDocument a,
            String timeFilter1,
            String timeFilter2,
            String CORE
    ) {
        String datetime = TimeFunctions.addTimeFilter(a, timeFilter1, timeFilter2);
        return searchForDuplicatesWithLongArray(a, datetime, CORE);
    }

    public QueryResponse searchForDuplicates(SolrDocument a, String datetime, String core) {

        QueryResponse solrDocuments = searchD101(a, datetime, core);
        if (solrDocuments == null | solrDocuments.getResults().size() == 0) {
            solrDocuments = searchD102(a, datetime, core);
            if (solrDocuments == null | solrDocuments.getResults().size() == 0) {
                solrDocuments = searchD103(a, datetime, core);
                if (solrDocuments == null | solrDocuments.getResults().size() == 0) {
                    solrDocuments = searchD104(a, datetime, core);
                }
            }
        }
        return solrDocuments;
    }

    public DocCountsResultsPair searchForDuplicatesWithLongArray(SolrDocument a, String datetime, String core) {

        DocCountsResultsPair docCountsResultsPair = new DocCountsResultsPair();
        long[] longs = new long[4];
        QueryResponse solrDocuments = searchD101(a, datetime, core);
        longs[0] = solrDocuments.getResults().getNumFound();
        longs[1] = -1;
        longs[2] = -1;
        longs[3] = -1;
        if (solrDocuments == null | solrDocuments.getResults().size() == 0) {
            solrDocuments = searchD102(a, datetime, core);
            longs[1] = solrDocuments.getResults().getNumFound();
            if (solrDocuments == null | solrDocuments.getResults().size() == 0) {
                solrDocuments = searchD103(a, datetime, core);
                longs[2] = solrDocuments.getResults().getNumFound();
                if (solrDocuments == null | solrDocuments.getResults().size() == 0) {
                    solrDocuments = searchD104(a, datetime, core);
                    longs[3] = solrDocuments.getResults().getNumFound();
                }
            }
        }
        docCountsResultsPair.setLongs(longs);
        docCountsResultsPair.setSolrDocuments(solrDocuments.getResults());
        return docCountsResultsPair;
    }


    public QueryResponse searchD101(SolrDocument a, String datetime, String core) {
        datetime = datetimeAndAppIdFilter(a, datetime);
        String q = queryFromSolrDoc(a);
        try {
            return searchLogics.searchDateRangeGetAll(q, datetime, "sms", core);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QueryResponse searchD102(SolrDocument a, String datetime, String core) {
        datetime = datetimeAndAppIdFilter(a, datetime);
        String q = queryFromSolrDoc(a);
        try {
            return searchLogics.searchDateRangeGetAll(q, datetime, "smsNoPunctuations", core);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public QueryResponse searchD103(SolrDocument a, String datetime, String core) {

        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdFilter(a, datetime);
        String mm = "100%";
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public QueryResponse searchD104(SolrDocument a, String datetime, String core) {
        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdFilter(a, datetime);
        String mm = "-1";
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String datetimeAndAppIdFilter(SolrDocument a, String datetime) {
        String appId = a.getFieldValue("app_id").toString();
        String timestamp = a.getFieldValue("timestamp").toString();
        datetime = datetime + " AND app_id:" + appId + " AND NOT timestamp:" + timestamp;
        return datetime;
    }

    private String queryFromSolrDoc(SolrDocument a) {
        String sms = a.getFieldValue("sms").toString();
        sms = sms.replaceAll("\"", "\\\\\"");
        return "\"" + sms + "\"";
    }

    private String queryFromSolrDocD103(SolrDocument a) {
        String sms = a.getFieldValue("sms").toString();
        sms = sms.replaceAll(":", " ");
        sms = sms.replaceAll("\"", " ");
        return "( " + sms + " )";
    }


}
