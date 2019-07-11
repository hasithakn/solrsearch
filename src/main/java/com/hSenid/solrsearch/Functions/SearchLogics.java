package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Dao.CSVDataDao;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;

public class SearchLogics {
    public QueryResponse searchDateRange(String q, String dateRangeAS_fq, int start, int rows, String core) {
        CSVDataDao csvDataDao = new CSVDataDao();
        try {
            QueryResponse reader = csvDataDao.reader(q, "score,*", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), "", "", "on", core);
            return reader;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
