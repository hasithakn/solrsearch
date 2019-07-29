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
                "-3MONTHS",
                "-3DAYS",
                core);
        long numFound = docCountsResultsPair.getSolrDocuments().getNumFound();
        System.out.println();

    }

    @Test
    public void searchForDuplicatesWithLongArray() {
        SolrDocument doc = getDoc();
        DocCountsResultsPair docCountsResultsPair = searchDuplicatesAdvance
                .searchForDuplicatesWithLongArray(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core);
        long numFound = docCountsResultsPair.getSolrDocuments().getNumFound();
        System.out.println();
    }

    @Test
    public void searchForDuplicates() {
        SolrDocument doc = getDoc();
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchForDuplicates(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        System.out.println();
    }

    @Test
    public void searchD101() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD101(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        System.out.println();

    }

    @Test
    public void searchD102() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD102(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        System.out.println();
    }

    @Test
    public void searchD103() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD103(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        System.out.println();

    }

    @Test
    public void searchD104() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD104(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core).getResults();
        long numFound = solrDocuments.getNumFound();
        System.out.println();
    }


    public SolrDocument getDoc() {

        try {
            return searchLogics.searchDateRange("timestamp:1551496896119",
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