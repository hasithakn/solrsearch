package com.hSenid.solrsearch.Functions;

import org.apache.solr.common.SolrDocument;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFunctions {
    public static String timestampToISO(Date date) {
        try {
            String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String isoTS = sdf.format(date);
            return isoTS;
        } catch (Exception e) {
            if (date != null) {
                return date.toString();
            } else {
                return "";
            }

        }
    }

    public static String getFilterDate(String timeFilter1, String timeFilter2, String timeTemp) {
        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(timeTemp);
        temp.append(timeFilter1);
        temp.append(" TO ");
        temp.append(timeTemp + timeFilter2 + " ]");
        return temp.toString();
    }

    public static String addTimeFilter(SolrDocument entrie, String timeFilter1, String timeFilter2) {
        String timeTemp = timestampToISO((Date) entrie.getFieldValue("datetime"));
        return getFilterDate(timeFilter1, timeFilter2, timeTemp);
    }

    public static String addTimeFilterDateXTillDocTime(SolrDocument entrie, String x) {
        String timeTemp = timestampToISO((Date) entrie.getFieldValue("datetime"));
        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(x);
        temp.append(" TO ");
        temp.append(timeTemp + " ]");
        return temp.toString();
    }

    public static String getDateTimeWithXY(String x, String y) {
        StringBuffer temp = new StringBuffer();
        temp.append("datetime: [");
        temp.append(x);
        temp.append(" TO ");
        temp.append(y + " ]");
        return temp.toString();
    }
}
