package com.hSenid.solrsearch.Functions;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SearchDuplicatesAdvanceTest {

    SearchLogics searchLogics = new SearchLogics();
    String core = "experiment2";
    SearchDuplicatesAdvance searchDuplicatesAdvance = new SearchDuplicatesAdvance(searchLogics);

    @Test
    public void searchWithinTimePeriod() {
    }

    @Test
    public void searchForDuplicates() {
    }

    @Test
    public void searchD101() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD101(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core);
        System.out.println();

    }

    @Test
    public void searchD102() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD102(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core);
        System.out.println();
    }

    @Test
    public void searchD103() {
        SolrDocument doc = getDoc();
        doc.getFieldNames().stream()
                .forEach(e -> System.out.println(doc.getFieldValue(e)));
        SolrDocumentList solrDocuments = searchDuplicatesAdvance.searchD103(doc,
                "datetime:[2019-02-01T00:00:00Z TO 2019-02-28T00:00:00Z]",
                core);
        System.out.println();

    }

    @Test
    public void searchD104() {
    }


    public SolrDocument getDoc() {

        try {
            return searchLogics.searchDateRange("id: ae5d2e56-6788-45b8-aa50-338a4a51d53f",
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