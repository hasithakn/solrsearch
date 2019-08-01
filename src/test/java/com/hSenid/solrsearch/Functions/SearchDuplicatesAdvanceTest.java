package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Dao.CSVDataDao;
import com.hSenid.solrsearch.Entity.DocCountsResultsPair;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SearchDuplicatesAdvanceTest {

    SearchLogics searchLogics = new SearchLogics();
    CSVDataDao csvDataDao = new CSVDataDao();
    String core = "experiment3";
    SearchDuplicatesAdvance searchDuplicatesAdvance = new SearchDuplicatesAdvance(searchLogics);

    @Test
    public void searchWithinTimePeriod() {
        SolrDocument doc = getDoc();
        DocCountsResultsPair docCountsResultsPair = searchDuplicatesAdvance.searchWithinTimePeriod(
                doc,
                "-1MONTHS",
                "",
                core);
        long numFound = docCountsResultsPair.getSolrDocuments().getNumFound();
        System.out.println();

    }

    @Test
    public void searchForDuplicatesWithLongArray() {
        SolrDocument doc = getDoc();
        String s = TimeFunctions.addTimeFilter(doc, "-1MONTHS", "");
        DocCountsResultsPair docCountsResultsPair = searchDuplicatesAdvance
                .searchForDuplicatesWithLongArray(doc,
                        s,
                        core);
        long numFound = docCountsResultsPair.getSolrDocuments().getNumFound();
        System.out.println();
    }

    @Test
    public void searchForDuplicates() {
        SolrDocument doc = getDoc();
        String s = TimeFunctions.addTimeFilter(doc, "-24HOURS", "");
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchForDuplicates(doc,
                s,
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        System.out.println();
    }

    @Test
    public void searchD101() {
        SolrDocument doc = getDoc();
        System.out.println(doc.getFieldValue("sms"));
        String s = TimeFunctions.addTimeFilter(doc, "-1MONTHS", "");
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD101(doc,
                s,
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        solrDocuments.stream().forEach(e-> System.out.println(e.getFieldValue("timestamp")+" : "+e.getFieldValue("sms")));
        System.out.println();

    }

    @Test
    public void searchD102() {
        SolrDocument doc = getDoc();
        System.out.println(doc.getFieldValue("sms"));
        String s = TimeFunctions.addTimeFilter(doc, "-1MONTHS", "");
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD102(doc,
                s,
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        solrDocuments.stream().forEach(e-> System.out.println(e.getFieldValue("timestamp")+" : "+e.getFieldValue("sms")));
        System.out.println();
    }

    @Test
    public void searchD103() {
        SolrDocument doc = getDoc();
        System.out.println(doc.getFieldValue("sms"));
        String s = TimeFunctions.addTimeFilter(doc, "-1MONTHS", "");
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD103(doc,
                s,
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        solrDocuments.stream().forEach(e-> System.out.println(e.getFieldValue("timestamp")+" : "+e.getFieldValue("sms")));
        System.out.println();
    }

    @Test
    public void searchD104() {
        SolrDocument doc = getDoc();
        System.out.println(doc.getFieldValue("sms"));
        String s = TimeFunctions.addTimeFilter(doc, "-1MONTHS", "");
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD104(doc,
                s,
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        solrDocuments.stream().forEach(e-> System.out.println(e.getFieldValue("timestamp")+" : "+e.getFieldValue("sms")));
        System.out.println();
    }


    public SolrDocument getDoc() {

        try {
            return searchLogics.searchDateRange("timestamp:1551436459480",
                    "",
                    "",
                    0,
                    1,
                    core).getResults().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }
}