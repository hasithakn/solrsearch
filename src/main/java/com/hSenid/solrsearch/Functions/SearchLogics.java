package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Dao.CSVDataDao;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;

public class SearchLogics {

    public CSVDataDao csvDataDao = new CSVDataDao();

    public QueryResponse searchDateRangeGetAll(
            String q,
            String dateRangeAS_fq,
            String df,
            String core
    ) throws IOException, SolrServerException {

        int start = 0;
        final int batch = 1;

        QueryResponse reader = searchDateRange(q, dateRangeAS_fq, df, 0, batch, core);

//        int noOfDocs = (int) reader.getResults().getNumFound();
//        int noOfTimes = (noOfDocs / batch);
//
//        if (noOfDocs > batch) {
//            for (int i = 0; i < noOfTimes; i++) {
//                start += batch;
//
//                QueryResponse response = searchDateRange(q, dateRangeAS_fq, df, start, batch, core);
//
//                if (response.getResults() != null) {
//                    response.getResults().stream().forEach(e -> reader.getResults().add(e));
//                }
//            }
//        }

        return reader;
    }

    public QueryResponse searchDateRangeGetAllDismax(
            String q,
            String dateRangeAS_fq,
            String df,
            String mm,
            String core
    ) throws IOException, SolrServerException {

        int start = 0;
        final int batch = 1;
        QueryResponse reader = searchDateRangeDismax(q, dateRangeAS_fq, df, 0, batch, mm, core);
//        int noOfDocs = (int) reader.getResults().getNumFound();
//        int noOfTimes = (noOfDocs / batch);
//
//        if (noOfDocs > batch) {
//            for (int i = 0; i < noOfTimes; i++) {
//                start += batch;
//                QueryResponse response = searchDateRangeDismax(q, dateRangeAS_fq, df, start, batch, mm, core);
//                if (response.getResults() != null) {
//                    response.getResults().stream().forEach(e -> reader.getResults().add(e));
//                }
//            }
//        }

        return reader;
    }

    public QueryResponse searchDateRange(String q, String dateRangeAS_fq, String df, int start, int rows, String core) throws IOException, SolrServerException {
        return csvDataDao.reader(q, "", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "off", core);
    }

    public QueryResponse searchDateRangeDismax(String q, String dateRangeAS_fq, String df, int start, int rows, String mm, String core) throws IOException, SolrServerException {
        return csvDataDao.readerDismax(q, "", dateRangeAS_fq, Integer.toString(start), Integer.toString(rows), df, "", "on", mm, core);

    }
}
