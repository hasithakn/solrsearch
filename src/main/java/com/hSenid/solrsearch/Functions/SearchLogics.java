package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Dao.CSVDataDao;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;

public class SearchLogics {
    public QueryResponse searchDateRange(String q, String dateRangeAS_fq, String df, int start, int rows, String core) throws IOException, SolrServerException {
        CSVDataDao csvDataDao = new CSVDataDao();
//        QueryResponse reader = csvDataDao.reader(q, "score,*", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "on", core);
        QueryResponse reader = csvDataDao.reader(q, "", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "off", core);
        return reader;
    }

    public QueryResponse searchDateRangeDismax(String q, String dateRangeAS_fq, String df, int start, int rows, int mm, String core) throws IOException, SolrServerException {
        CSVDataDao csvDataDao = new CSVDataDao();
        QueryResponse reader = csvDataDao.readerDismax(q, "", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "on", Integer.toString(mm), core);
        return reader;
    }
}
