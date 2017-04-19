package com.comall.songshu.web.rest.util;

/**
 * Created by lgx on 17/4/12.
 */
public class Target {
    private String target;
    private Long[][] datapoints = new Long[1][2];
    private String columnName;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long[][] getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(Long[] array) {
        this.datapoints[0] = array;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }



//    @Override
//    public String toString() {
//        return
//    }
}
