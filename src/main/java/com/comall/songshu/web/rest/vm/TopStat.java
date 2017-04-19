package com.comall.songshu.web.rest.vm;

/**
 * Created by wdc on 2017/4/12.
 * dashboard index top ， 第一个值  ， 第一个值的环比
 */
public class TopStat {

    private double firstValue;
    private double ringsRation;
    private int flag = 0;

    public TopStat(double firstValue, double ringsRation) {
        this.firstValue = firstValue;
        this.ringsRation = ringsRation;
    }

    public TopStat(double firstValue, double ringsRation, int flag) {
        this.firstValue = firstValue;
        this.ringsRation = ringsRation;
        this.flag = flag;
    }

    public double getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(double firstValue) {
        this.firstValue = firstValue;
    }

    public double getRingsRation() {
        return ringsRation;
    }

    public void setRingsRation(double ringsRation) {
        this.ringsRation = ringsRation;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "TopStat{" +
                "firstValue=" + firstValue +
                ", ringsRation=" + ringsRation +
                ", flag=" + flag +
                '}';
    }
}
