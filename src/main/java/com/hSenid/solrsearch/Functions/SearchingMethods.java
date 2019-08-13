package com.hSenid.solrsearch.Functions;

import com.hSenid.solrsearch.Entity.DocCountsResultsPair;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

public class SearchingMethods {

    public SearchDuplicatesAdvance SDA;

    public SearchingMethods(SearchDuplicatesAdvance SDA) {
        this.SDA = SDA;
    }

    public DocCountsResultsPair searchWithinTimePeriod(
            SolrDocument a,
            String timeFilter1,
            String timeFilter2,
            String CORE
    ) {
        String datetime = TimeFunctions.addTimeFilter(a, timeFilter1, timeFilter2);
        return searchForDuplicatesWithLongArray(a, datetime, CORE);
    }

    public QueryResponse searchForDuplicates(SolrDocument a, String datetime, String core) {

        QueryResponse solrDocuments = SDA.searchD101(a, datetime, core);
        if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
            solrDocuments = SDA.searchD102(a, datetime, core);
            if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
                solrDocuments = SDA.searchD103(a, datetime, core);
                if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
                    solrDocuments = SDA.searchD104(a, datetime, core);
                }
            }
        }
        return solrDocuments;
    }

    public QueryResponse searchForDuplicatesD101D102(SolrDocument a, String datetime, String core) {

        QueryResponse solrDocuments = SDA.searchD101(a, datetime, core);
        if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
            solrDocuments = SDA.searchD102(a, datetime, core);
        }
        return solrDocuments;
    }

    public DocCountsResultsPair searchForDuplicatesWithLongArray(SolrDocument a, String datetime, String core) {

        DocCountsResultsPair docCountsResultsPair = new DocCountsResultsPair();
        long[] longs = new long[4];
        QueryResponse solrDocuments = SDA.searchD101(a, datetime, core);
        longs[0] = solrDocuments.getResults().getNumFound();
        longs[1] = -1;
        longs[2] = -1;
        longs[3] = -1;
        if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
            solrDocuments = SDA.searchD102(a, datetime, core);
            longs[1] = solrDocuments.getResults().getNumFound();
            if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
                solrDocuments = SDA.searchD103(a, datetime, core);
                longs[2] = solrDocuments.getResults().getNumFound();
                if (solrDocuments == null | solrDocuments.getResults().isEmpty()) {
                    solrDocuments = SDA.searchD104(a, datetime, core);
                    longs[3] = solrDocuments.getResults().getNumFound();
                }
            }
        }
        docCountsResultsPair.setLongs(longs);
        docCountsResultsPair.setSolrDocuments(solrDocuments.getResults());
        return docCountsResultsPair;
    }
}
