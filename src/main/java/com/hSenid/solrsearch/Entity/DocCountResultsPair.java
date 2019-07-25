package com.hSenid.solrsearch.Entity;

import org.apache.solr.common.SolrDocumentList;

public class DocCountResultsPair {
    private SolrDocumentList solrDocuments;
    private long docCount;

    public SolrDocumentList getSolrDocuments() {
        return solrDocuments;
    }

    public void setSolrDocuments(SolrDocumentList solrDocuments) {
        this.solrDocuments = solrDocuments;
    }

    public long getDocCount() {
        return docCount;
    }

    public void setDocCount(long docCount) {
        this.docCount = docCount;
    }
}
