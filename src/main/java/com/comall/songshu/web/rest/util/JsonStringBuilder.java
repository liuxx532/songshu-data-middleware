package com.comall.songshu.web.rest.util;

import com.comall.songshu.web.rest.vm.TopStat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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


    /**
     *
     * @return
     *
     * [{"target":"当前","datapoints":[[5951,1488297549990],[12185,1488327309989],[9112,1488357069988],[5492,1488386829987],[10086,1488416589986],[7850,1488446349985],[4670,1488476109984],[9484,1488505869983],[7807,1488535629982],[6813,1488565389981],[8798,1488595149980],[7827,1488624909979],[5776,1488654669978],[6569,1488684429977],[5115,1488714189976],[7483,1488743949975],[9420,1488773709974],[6038,1488803469973],[6745,1488833229972],[15157,1488862989971],[11081,1488892749970],[24107,1488922509969],[9884,1488952269968],[3950,1488982029967],[6701,1489011789966],[7081,1489041549965],[5685,1489071309964],[21055,1489101069963],[40808,1489130829962],[8765,1489160589961],[18308,1489190349960],[18224,1489220109959],[8193,1489249869958],[19021,1489279629957],[16622,1489309389956],[9740,1489339149955],[26576,1489368909954],[17044,1489398669953],[11845,1489428429952],[23705,1489458189951],[19451,1489487949950],[14414,1489517709949],[53083,1489547469948],[21421,1489577229947],[19189,1489606989946],[25567,1489636749945],[16881,1489666509944],[16343,1489696269943],[20769,1489726029942],[11399,1489755789941],[13400,1489785549940],[17495,1489815309939],[10170,1489845069938],[13967,1489874829937],[18200,1489904589936],[7655,1489934349935],[19192,1489964109934],[19735,1489993869933],[8367,1490023629932],[30056,1490053389931],[37592,1490083149930],[9409,1490112909929],[25125,1490142669928],[23316,1490172429927],[9963,1490202189926],[26561,1490231949925],[15259,1490261709924],[10947,1490291469923],[30291,1490321229922],[27477,1490350989921],[21915,1490380749920],[27599,1490410509919],[27452,1490440269918],[23540,1490470029917],[24549,1490499789916],[15310,1490529549915],[43086,1490559309914],[25639,1490589069913],[12652,1490618829912],[16444,1490648589911],[55005,1490678349910],[0,1490708109909],[0,1490737869908],[0,1490767629907],[7829,1490797389906],[24565,1490827149905],[32455,1490856909904],[25524,1490886669903],[36455,1490916429902],[27077,1490946189901],[68,1490975949900]]
     */
    public static  String buildTrendJsonString(List<Object[]> currentTrend,List<Object[]> chainTrend) {

        if (currentTrend != null && chainTrend != null) {

            List<Timestamp> timeList = new ArrayList<>();

            StringBuilder sb = new StringBuilder(1024);
            sb.append("[{\"target\":\"当前\",\"datapoints\":[");
            for (Object[] objects : currentTrend) {
                Timestamp endTime = (Timestamp) objects[1];
                timeList.add(endTime);
                sb.append("[").append(objects[2]).append(',').append(endTime.getTime()).append("]").append(',');
            }
            //出去最后一个 ',';
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]").append(',').append("\"columnName\":\"\"").append("}").append(',');


            sb.append("{\"target\":\"环比\",\"datapoints\":[");

            for (int i = 0; i < chainTrend.size(); i++) {
                Timestamp endTime = (Timestamp) currentTrend.get(i)[1];
                sb.append("[").append(chainTrend.get(i)[2]).append(',').append(endTime.getTime()).append("]").append(',');
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append("]").append(',').append("\"columnName\":\"").append("\"").append("}]");

            return sb.toString();
        }

        return null;
    }

    public static  String buildTrendJsonStringForLongType(List<Object[]> currentTrend,List<Object[]> chainTrend) {

        if (currentTrend != null && chainTrend != null) {

            StringBuilder sb = new StringBuilder(1024);
            sb.append("[{\"target\":\"当前\",\"datapoints\":[");
            for (Object[] objects : currentTrend) {
                Timestamp endTime = (Timestamp) objects[1];

                // If result is null, return ZERO
                BigInteger result = Optional.ofNullable((BigInteger) objects[2]).orElse(BigInteger.ZERO);

                sb.append("[").append(result).append(',').append(endTime.getTime()).append("]").append(',');
            }
            //出去最后一个 ',';
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]").append(',').append("\"columnName\":\"\"").append("}").append(',');


            sb.append("{\"target\":\"当前\",\"datapoints\":[");

            for (Object[] objects : chainTrend) {
                Timestamp endTime = (Timestamp) objects[1];

                // If result is null, return ZERO
                BigInteger result = Optional.ofNullable((BigInteger) objects[2]).orElse(BigInteger.ZERO);

                sb.append("[").append(result).append(',').append(endTime.getTime()).append("]").append(',');
                sb.append("[").append(result).append(',').append(endTime.getTime()).append("]").append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]").append(',').append("\"columnName\":\"").append("\"").append("}]");

            return sb.toString();
        }

        return null;
    }


    // [{"target": "坚果", "datapoints": [[25, 1492484041446]],"columnName": ""},{"target": "坚果", "datapoints": [[25, 1492484041446]],"columnName": ""},]
    public static  String buildRankJsonString(List<Object[]> rank) {

        if (rank != null) {

            StringBuilder sb = new StringBuilder(1024);

            sb.append("[");

            for (Object[] r : rank) {
                String name = (String) r[0];
                BigDecimal amount = Optional.ofNullable((BigDecimal) r[1]).orElse(BigDecimal.ZERO);
                sb.append("{\"target\": \"").append(name).append("\", \"datapoints\": [[").append(amount).append(",")
                    .append(System.currentTimeMillis()).append("]],\"columnName\": \"\"},");
            }
            sb.deleteCharAt(sb.length() - 1); // omit
            sb.append("]");

            return sb.toString();
        }

        return null;
    }

    // 首单，非首单占比
    public static String buildOrderedConsumerCountJsonString(Double order, Double notOrder) {
        StringBuilder sb = new StringBuilder(1024);
        sb.append("[{\"target\":\"首单\",\"datapoints\":[[").append(order).append(',')
            .append(System.currentTimeMillis()).append("]],\"columnName\":\"\"},{\"target\":\"非首单\",\"datapoints\":[[")
            .append(notOrder).append(',').append(System.currentTimeMillis()).append("]],\"columnName\":\"\"}]");

        return sb.toString();
    }


    // 注册用户占比

    public static  String buildPieJsonString(String platform,List<Integer> list){
        StringBuilder sb = new StringBuilder(1024);
        if (list.size() >0 ) {
            String[] names = {"android", "ios", "wechar", "wap", "others"};
            sb.append("[");


            for (int i = 0; i < names.length; i++) {
                sb.append("{\"target\":").append(names[i]).append(",\"datapoints\":[[").append((list.get(i))).append(',').append(System.currentTimeMillis()).append("]],\"columnName\":\"\"}").append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");

        }
        return sb.toString();
    }
}
