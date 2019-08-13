package com.hSenid.solrsearch.Dao;


import com.hSenid.solrsearch.DbConnection.SOLRDBConnecter;
import com.hSenid.solrsearch.Entity.SolrEntity;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolrEntityDao {

    final SolrClient client = SOLRDBConnecter.getSolrClient();

    public void indexList(List<SolrEntity> solrEntities, String core) throws IOException, SolrServerException {
        List<SolrInputDocument> solrInputDocuments = new ArrayList();

        solrEntities.forEach(e -> {
            SolrInputDocument doc = this.getSolrInputFields(e);
            solrInputDocuments.add(doc);
        });
        this.client.add(core, solrInputDocuments);
    }

    public void index(SolrEntity solrEntity, String core) throws IOException, SolrServerException {
        SolrInputDocument doc = this.getSolrInputFields(solrEntity);
        this.client.add(core, doc, 100000);
    }

    public void commitdata(String core) throws IOException, SolrServerException {
        this.client.commit(core);
    }

    private SolrInputDocument getSolrInputFields(SolrEntity solrEntity) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("app_id", solrEntity.getApp_id());
        doc.addField("sms", solrEntity.getSms());
        doc.addField("datetime", solrEntity.getDatetime());
        doc.addField("timestamp", solrEntity.getTimestamp());
        doc.addField("termCount", solrEntity.getTermCount());
        return doc;
    }

}
