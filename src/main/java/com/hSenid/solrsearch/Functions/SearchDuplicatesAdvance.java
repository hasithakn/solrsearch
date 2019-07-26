package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Entity.DocCountResultsPair;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;

public class SearchDuplicatesAdvance {


    public SearchLogics searchLogics;

    public SearchDuplicatesAdvance(SearchLogics searchLogics) {
        this.searchLogics = searchLogics;
    }

    public SolrDocumentList searchWithinTimePeriod(
            SolrDocument a,
            String timeFilter1,
            String timeFilter2,
            String CORE
    ) {
        String id = a.getFieldValue("id").toString();
        String datetime = TimeFunctions.addTimeFilter(a, timeFilter1, timeFilter2);
        SolrDocumentList solrDocuments = searchForDuplicates(a, datetime, CORE);
        return solrDocuments;

    }

    public SolrDocumentList searchForDuplicates(SolrDocument a, String datetime, String core) {

        SolrDocumentList solrDocuments = searchD101(a, datetime, core);
        if (solrDocuments == null | solrDocuments.size() == 0) {
            solrDocuments = searchD102(a, datetime, core);
            if (solrDocuments == null | solrDocuments.size() == 0) {
                solrDocuments = searchD103(a, datetime, core);
                if (solrDocuments == null | solrDocuments.size() == 0) {
                    solrDocuments = searchD104(a, datetime, core);
                }
            }
        }
        return solrDocuments;
    }


    public SolrDocumentList searchD101(SolrDocument a, String datetime, String core) {
        datetime = datetimeAndAppIdFilter(a, datetime);
        String q = queryFromSolrDoc(a);
        try {
            return searchLogics.searchDateRangeGetAll(q, datetime, "sms", core).getResults();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SolrDocumentList searchD102(SolrDocument a, String datetime, String core) {
        datetime = datetimeAndAppIdFilter(a, datetime);
        String q = queryFromSolrDoc(a);
        try {
            return searchLogics.searchDateRangeGetAll(q, datetime, "smsNoPunctuations", core).getResults();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SolrDocumentList searchD103(SolrDocument a, String datetime, String core) {

        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdFilter(a, datetime);
        int mm = a.getFieldValue("sms").toString().split(" ").length;
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core).getResults();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public SolrDocumentList searchD104(SolrDocument a, String datetime, String core) {



        return null;
    }

    private String datetimeAndAppIdFilter(SolrDocument a, String datetime) {
        String appId = a.getFieldValue("app_id").toString();
        datetime = datetime + " AND app_id:" + appId;
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
