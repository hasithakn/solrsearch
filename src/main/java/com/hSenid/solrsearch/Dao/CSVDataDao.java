package com.hSenid.solrsearch.Dao;


import com.hSenid.solrsearch.DbConnection.SOLRDBConnecter;
import com.hSenid.solrsearch.Entity.CSVData;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CSVDataDao {
    final SolrClient client = SOLRDBConnecter.getSolrClient();

    public CSVDataDao() {
    }

    public QueryResponse reader(String q, String fl, String fq, String start, String rows, String df, String sort, String debugQuery, String core) throws IOException, SolrServerException {
        ArrayList<CSVData> result = new ArrayList();
        Map<String, String> queryParamMap = new HashMap();
        if (!q.equals("")) {
            queryParamMap.put("q", q);
        }
        if (!fl.equals("")) {
            queryParamMap.put("fl", fl);
        }
        if (!fq.equals("")) {
            queryParamMap.put("fq", fq);
        }
        if (!rows.equals("")) {
            queryParamMap.put("rows", rows);
        }
        if (!start.equals("")) {
            queryParamMap.put("start", start);
        }
        if (!df.equals("")) {
            queryParamMap.put("df", df);
        }
        if (!sort.equals("")) {
            queryParamMap.put("sort", sort);
        }
        if (!debugQuery.equals("")) {
            queryParamMap.put("debugQuery", debugQuery);
        }
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        QueryResponse response = this.client.query(core, queryParams);

        return response;
    }

    public QueryResponse readerDismax(String q, String fl, String fq, String start, String rows, String df, String sort, String debugQuery, String mm, String core) throws IOException, SolrServerException {
        ArrayList<CSVData> result = new ArrayList();
        Map<String, String> queryParamMap = new HashMap();
        queryParamMap.put("defType", "dismax");
        if (!q.equals("")) {
            queryParamMap.put("q", q);
        }
        if (!fl.equals("")) {
            queryParamMap.put("fl", fl);
        }
        if (!mm.equals("")) {
            queryParamMap.put("mm", mm);
        }
        if (!fq.equals("")) {
            queryParamMap.put("fq", fq);
        }
        if (!rows.equals("")) {
            queryParamMap.put("rows", rows);
        }
        if (!start.equals("")) {
            queryParamMap.put("start", start);
        }
        if (!df.equals("")) {
            queryParamMap.put("df", df);
        }
        if (!sort.equals("")) {
            queryParamMap.put("sort", sort);
        }
        if (!debugQuery.equals("")) {
            queryParamMap.put("debugQuery", debugQuery);
        }
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        QueryResponse response = this.client.query(core, queryParams);
        return response;
    }


    public void indexList(ArrayList<CSVData> csvDataArrayList, String core) throws IOException, SolrServerException {
        ArrayList<SolrInputDocument> solrInputDocuments = new ArrayList();
        Iterator var4 = csvDataArrayList.iterator();

        while (var4.hasNext()) {
            CSVData csvData = (CSVData) var4.next();
            SolrInputDocument doc = this.getSolrInputFields(csvData);
            solrInputDocuments.add(doc);
        }

        this.client.add(core, solrInputDocuments);
    }

    public void index(CSVData csvData, String core) throws IOException, SolrServerException {
        SolrInputDocument doc = this.getSolrInputFields(csvData);
        this.client.add(core, doc, 100000);
    }

    public void commitdata(String core) throws IOException, SolrServerException {
        this.client.commit(core);
    }

    private SolrInputDocument getSolrInputFields(CSVData csvData) {
        SolrInputDocument doc = new SolrInputDocument(new String[0]);
        doc.addField("app_id", csvData.getApp_id());
        doc.addField("sms", csvData.getSms());
        doc.addField("datetime", csvData.getDatetime());
        doc.addField("timestamp", csvData.getTimestamp());
        return doc;
    }
}
