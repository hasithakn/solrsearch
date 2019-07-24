package com.hSenid.solrsearch.Functions;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.io.IOException;

public class SearchDuplicates {

    public Long[] searchForDuplicates(SolrDocument a, String dateRangeToSearchINDup, String CORE, SearchLogics searchLogics) {
        Long[] longs = new Long[4];
        //-1 for exception
        //0 for not found
        //>0 for matches

        long D101 = searchForDuplicatesD101(
                a,
                dateRangeToSearchINDup,
                CORE,
                searchLogics);
        if (D101 <= 0) {
            long D102 = searchForDuplicatesD102(
                    a,
                    dateRangeToSearchINDup,
                    CORE,
                    searchLogics);
            if (D102 <= 0) {
                long[] D103andD104 = searchForDuplicatesD103andD104(
                        a,
                        dateRangeToSearchINDup,
                        CORE,
                        searchLogics);
                longs[0] = D101;
                longs[1] = D102;
                longs[2] = D103andD104[0];
                longs[3] = D103andD104[1];
            } else {
                longs[0] = D101;
                longs[1] = D102;
                longs[2] = -1L;
                longs[3] = -1L;
            }
        } else {
            longs[0] = D101;
            longs[1] = -1L;
            longs[2] = -1L;
            longs[3] = -1L;
        }
        return longs;
    }

    public long searchForDuplicatesD101(SolrDocument a, String dateRange, String core, SearchLogics searchLogics) {

        String sms = a.getFieldValue("sms").toString();
        String app_id = a.getFieldValue("app_id").toString();
        dateRange = dateRange + " AND app_id:" + app_id;
        sms = sms.replaceAll("\"", "\\\\\"");
        String q = "\"" + sms + "\"";
        long numFound = -1;
        try {
            QueryResponse searchDocs = searchLogics.searchDateRange(q,
                    dateRange,
                    "sms",
                    0,
                    1,
                    core);
            numFound = searchDocs.getResults().getNumFound();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (HttpSolrClient.RemoteSolrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numFound;
    }

    public long searchForDuplicatesD102(SolrDocument a, String dateRange, String core, SearchLogics searchLogics) {
        String sms = a.getFieldValue("sms").toString();
        sms = sms.replaceAll("\"", "\\\\\"");
        String app_id = a.getFieldValue("app_id").toString();
        dateRange = dateRange + " AND app_id:" + app_id;
        String q = "\"" + sms + "\"";
        QueryResponse searchDocs = null;
        long numFound = -1;
        try {
            searchDocs = searchLogics.searchDateRange(q,
                    dateRange,
                    "smsNoPunctuations",
                    0,
                    1,
                    core);
            numFound = searchDocs.getResults().getNumFound();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (HttpSolrClient.RemoteSolrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numFound;
    }

    public long[] searchForDuplicatesD103andD104(SolrDocument a, String dateRange, String core, SearchLogics searchLogics) {
        long[] ret = new long[2];
        ret[0] = -1;
        ret[1] = -1;
        String sms = a.getFieldValue("sms").toString();
        sms = sms.replaceAll(":", " ");
        sms = sms.replaceAll("\"", " ");
        String app_id = a.getFieldValue("app_id").toString();
        dateRange = dateRange + " AND app_id:" + app_id;
        String q = "( " + sms + " )";
        QueryResponse searchDocs = null;
        int length = sms.split(" ").length;
        long numFoundD103 = -1;
        try {
            searchDocs = searchLogics.searchDateRangeDismax(q,
                    dateRange,
                    "smsEN",
                    0,
                    1,
                    length,
                    core);
            numFoundD103 = searchDocs.getResults().getNumFound();

            if (numFoundD103 <= 0) {
                String parsedquery_toString = searchDocs.getDebugMap().get("parsedquery_toString").toString();
                int totTerms = 0;
                try {
                    totTerms = getTotTerms(length, parsedquery_toString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                long numFoundD104 = -1;
                searchDocs = searchLogics.searchDateRangeDismax(q,
                        dateRange,
                        "smsEN",
                        0,
                        1,
                        totTerms,
                        core);
                numFoundD104 = searchDocs.getResults().getNumFound();
                ret[0] = numFoundD103;
                ret[1] = numFoundD104;
            } else {
                ret[0] = numFoundD103;
                ret[1] = -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (HttpSolrClient.RemoteSolrException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private int getTotTerms(int length, String parsedquery_toString) {
        int totTerms = -1;
        if (length > 1) {
            try {
                int i = parsedquery_toString.indexOf("~", 0);
                int j = parsedquery_toString.indexOf(")", i);
                String terms = parsedquery_toString.substring(i + 1, j);
                totTerms = Integer.parseInt(terms);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                throw e;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }

            if (totTerms == -1) {
                totTerms = length - 1;
            } else {
                totTerms = totTerms - 1;
            }
            return totTerms;
        } else {
            return 0;
        }
    }
}
