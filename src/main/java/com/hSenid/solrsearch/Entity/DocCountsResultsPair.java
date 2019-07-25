package com.hSenid.solrsearch.Entity;

import org.apache.solr.common.SolrDocumentList;

public class DocCountsResultsPair {
    private SolrDocumentList solrDocuments;
    private long[] longs;

    public SolrDocumentList getSolrDocuments() {
        return solrDocuments;
    }

    public void setSolrDocuments(SolrDocumentList solrDocuments) {
        this.solrDocuments = solrDocuments;
    }

    public long[] getLongs() {
        return longs;
    }

    public void setLongs(long[] longs) {
        this.longs = longs;
    }
}
