package com.comall.songshu.web.rest.util;

import com.comall.songshu.web.rest.vm.TopStat;


/**
 * Created by wdc on 2017/4/14.
 */
public class AssembleUtil {

    /**
     * 封装TopStat
     * @param value
     * @param chainValue
     * @return
     */
    public static TopStat assemblerTopStat(Double value, Double chainValue) {

        if (value <=0|| chainValue <=0){
            return new TopStat(value,0.0);

        }else {
            //比例
            double percent =Math.abs(value-chainValue)/chainValue;
            int flag= ServiceUtil.getInstance().getChainIndexFlag(value,chainValue);
            return new TopStat(value, percent, flag);
        }

    }


}
