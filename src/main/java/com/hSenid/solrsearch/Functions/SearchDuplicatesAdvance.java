package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Entity.DocCountsResultsPair;
import com.sun.security.ntlm.Client;
import org.apache.lucene.analysis.Analyzer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.schema.AnalyzerDefinition;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchDuplicatesAdvance {

    private static final Logger LOGGER = Logger.getLogger(SearchDuplicatesAdvance.class.getName());

    public SearchLogics searchLogics;

    public SearchDuplicatesAdvance(SearchLogics searchLogics) {
        this.searchLogics = searchLogics;
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
            LOGGER.log(Level.WARNING, e.toString());
        }
        return null;
    }

    public QueryResponse searchD103(SolrDocument a, String datetime, String core) {
        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdTermCountFilter(a, datetime);
        String mm = "100%";
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core);
        } catch (IOException | SolrServerException e) {
            LOGGER.log(Level.WARNING, e.toString());
        }
        return null;
    }

    public QueryResponse searchD104(SolrDocument a, String datetime, String core) {
        String q = queryFromSolrDocD103(a);
        datetime = datetimeAndAppIdTermCountFilter(a, datetime);
        String mm = "-1";
        try {
            return searchLogics.searchDateRangeGetAllDismax(q, datetime, "smsEN", mm, core);
        } catch (IOException | SolrServerException e) {
            LOGGER.log(Level.WARNING, e.toString());
        }
        return null;
    }

    private String datetimeAndAppIdFilter(SolrDocument a, String datetime) {
        String appId = a.getFieldValue("app_id").toString();
        String timestamp = a.getFieldValue("timestamp").toString();
        datetime = datetime + " AND app_id:" + appId + " AND NOT timestamp:" + timestamp;
        return datetime;
    }

    private String datetimeAndAppIdTermCountFilter(SolrDocument a, String datetime) {
        StringBuilder temp = new StringBuilder(datetimeAndAppIdFilter(a, datetime));
        int termCount = Integer.parseInt(a.getFieldValue("termCount").toString());
        temp.append(" AND termCount: [ * TO " + termCount + " ] ");
        return temp.toString();

    }

    private String queryFromSolrDoc(SolrDocument a) {
        String sms = ClientUtils.escapeQueryChars(a.getFieldValue("sms").toString());
        return "\"" + sms + "\"";
    }

    private String queryFromSolrDocD103(SolrDocument a) {
        String sms = a.getFieldValue("sms").toString();
        return "( " + sms + " )";
    }

}
