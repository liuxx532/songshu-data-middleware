package com.comall.songshu.web.rest.util;

import com.comall.songshu.web.rest.vm.TopStat;


/**
 * Created by wdc on 2017/4/14.
 */
public class JsonStringBuilder {

    public static String buildTargetJsonString(String target, TopStat topStat, String columnName) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("[{\"target\":\"").append(target).append("\",\"datapoints\":[[{\"firstValue\":")
                .append( topStat.getFirstValue())
                .append(",\"ringsRation\": ").append(topStat.getRingsRation()).append(",\"flag\":")
                .append(topStat.getFlag()).append("},").append(System.currentTimeMillis())
                .append("]],\"columnName\":\"\"}]");
        return sb.toString();
    }

    public static String buildFinishedGroupJsonString(long completedGroupCount, long completedTimestamp, long cancelledGroupCount, long cancelledTimestamp) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("[{\"target\":\"成团\",\"datapoints\":[[").append(completedGroupCount).append(',')
                .append(completedTimestamp).append("]],\"columnName\":\"\"},{\"target\":\"失效\",\"datapoints\":[[")
                .append(cancelledGroupCount).append(',').append(cancelledTimestamp).append("]],\"columnName\":\"\"}]");

        return sb.toString();
    }
}
