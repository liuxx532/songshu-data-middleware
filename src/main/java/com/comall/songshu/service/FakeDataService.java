package com.comall.songshu.service;

import org.springframework.stereotype.Service;

/**
 * 封装假数据
 *
 * @author liushengling
 * @create 2017-05-12-10:56
 **/
@Service
public class FakeDataService {

    public String getFakeData(String target) {
        switch (target) {
            // 单个指标
            case "ProductRevenue":
                return "[{\"datapoints\":[[[{\"grossMarginRate\":0.38458660423719604601,\"revenue\":5341887.36,\"exitRate\":0.9681061576067979,\"orderCount\":80640,\"collectionCount\":313,\"paidRate\":0.8166104062390874,\"rank\":1,\"add2CartTimes\":67882,\"uniqueVisitor\":17182,\"goodsCost\":3287469.04,\"categoryName\":\"坚果/炒货\",\"productName\":\"夏威夷果 265g\"},{\"grossMarginRate\":0.42694023325067521118,\"revenue\":4542719.54,\"exitRate\":0.9597258531760051,\"orderCount\":79405,\"collectionCount\":201,\"paidRate\":0.7633010669116088,\"rank\":2,\"add2CartTimes\":49789,\"uniqueVisitor\":14153,\"goodsCost\":2603249.8,\"categoryName\":\"坚果/炒货\",\"productName\":\"开心果 225g\"},{\"grossMarginRate\":0.35367054607778563843,\"revenue\":3953275.26,\"exitRate\":0.9680657506744463,\"orderCount\":83906,\"collectionCount\":277,\"paidRate\":0.9133571742267395,\"rank\":3,\"add2CartTimes\":70409,\"uniqueVisitor\":15939,\"goodsCost\":2555118.24,\"categoryName\":\"坚果/炒货\",\"productName\":\"开口松子原味 218g\"},{\"grossMarginRate\":0.33534962817123212408,\"revenue\":3736480.66,\"exitRate\":0.9673371974087964,\"orderCount\":89738,\"collectionCount\":183,\"paidRate\":1.1241050119331741,\"rank\":4,\"add2CartTimes\":37897,\"uniqueVisitor\":14665,\"goodsCost\":2483453.26,\"categoryName\":\"坚果/炒货\",\"productName\":\"碧根果 225g\"},{\"grossMarginRate\":0.39982576548831248932,\"revenue\":2771046.88,\"exitRate\":0.9669745958429561,\"orderCount\":69183,\"collectionCount\":289,\"paidRate\":0.672459584295612,\"rank\":5,\"add2CartTimes\":66407,\"uniqueVisitor\":17320,\"goodsCost\":1663110.94,\"categoryName\":\"坚果/炒货\",\"productName\":\"手剥巴旦木 235g\"},{\"grossMarginRate\":0.35042116642924635663,\"revenue\":2335169.5,\"exitRate\":0.9696693791612132,\"orderCount\":63331,\"collectionCount\":348,\"paidRate\":0.8525009958459,\"rank\":6,\"add2CartTimes\":63865,\"uniqueVisitor\":17573,\"goodsCost\":1516876.68,\"categoryName\":\"坚果/炒货\",\"productName\":\"【爆卖】炭烧腰果185g\"},{\"grossMarginRate\":0.26392445590270290512,\"revenue\":2166270.64,\"exitRate\":0.9678291579295883,\"orderCount\":21037,\"collectionCount\":388,\"paidRate\":0.38025604237942834,\"rank\":7,\"add2CartTimes\":69312,\"uniqueVisitor\":18122,\"goodsCost\":1594538.84,\"categoryName\":\"坚果/炒货\",\"productName\":\"坚果三兄弟组合\"},{\"grossMarginRate\":0.47287009689967856199,\"revenue\":1183917.24,\"exitRate\":0.9669988834208676,\"orderCount\":32810,\"collectionCount\":590,\"paidRate\":0.6328108845788015,\"rank\":8,\"add2CartTimes\":80104,\"uniqueVisitor\":24181,\"goodsCost\":624078.18,\"categoryName\":\"果干/蜜饯\",\"productName\":\"芒果干 116g\"},{\"grossMarginRate\":0.41146310956449921192,\"revenue\":659057.82,\"exitRate\":0.9681071737251512,\"orderCount\":19576,\"collectionCount\":245,\"paidRate\":0.7860847018150389,\"rank\":9,\"add2CartTimes\":34593,\"uniqueVisitor\":11570,\"goodsCost\":387879.84,\"categoryName\":\"果干/蜜饯\",\"productName\":\"草莓干 106g\"},{\"grossMarginRate\":0.37711034749053599189,\"revenue\":598092.26,\"exitRate\":0.9664398569945797,\"orderCount\":12087,\"collectionCount\":469,\"paidRate\":0.3015415369238458,\"rank\":10,\"add2CartTimes\":87078,\"uniqueVisitor\":26013,\"goodsCost\":372545.48,\"categoryName\":\"坚果/炒货\",\"productName\":\"碧根果 210g\"},{\"grossMarginRate\":0.35289173223233387014,\"revenue\":542072.32,\"exitRate\":0.9641305518652831,\"orderCount\":11952,\"collectionCount\":555,\"paidRate\":0.22699364475243258,\"rank\":11,\"add2CartTimes\":63438,\"uniqueVisitor\":27851,\"goodsCost\":350779.48,\"categoryName\":\"坚果/炒货\",\"productName\":\"夏威夷果 185g\"},{\"grossMarginRate\":0.33166069416735159717,\"revenue\":506839.74,\"exitRate\":0.964696071163825,\"orderCount\":9461,\"collectionCount\":224,\"paidRate\":0.6438102297998517,\"rank\":12,\"add2CartTimes\":28635,\"uniqueVisitor\":10792,\"goodsCost\":338740.92,\"categoryName\":\"肉类/熟食\",\"productName\":\"猪肉脯原味 210g\"},{\"grossMarginRate\":0.59135524578724836047,\"revenue\":367104.48,\"exitRate\":0.968880688806888,\"orderCount\":28554,\"collectionCount\":158,\"paidRate\":0.9170971709717097,\"rank\":13,\"add2CartTimes\":36031,\"uniqueVisitor\":8130,\"goodsCost\":150015.32,\"categoryName\":\"坚果/炒货\",\"productName\":\"紫薯花生 120g\"},{\"grossMarginRate\":0.52690764335514243259,\"revenue\":349248.72,\"exitRate\":0.9534167119811492,\"orderCount\":4474,\"collectionCount\":165,\"paidRate\":0.33578031538879827,\"rank\":14,\"add2CartTimes\":10690,\"uniqueVisitor\":11034,\"goodsCost\":165226.9,\"categoryName\":\"坚果/炒货\",\"productName\":\"枣夹核桃仁 258g\"},{\"grossMarginRate\":0.42280332792462415119,\"revenue\":302203.96,\"exitRate\":0.9690433064383336,\"orderCount\":7497,\"collectionCount\":124,\"paidRate\":0.7684834513420056,\"rank\":15,\"add2CartTimes\":10256,\"uniqueVisitor\":6073,\"goodsCost\":174431.12,\"categoryName\":\"果干/蜜饯\",\"productName\":\"冻干榴莲 36g\"},{\"grossMarginRate\":0.34486273283051660721,\"revenue\":298316.78,\"exitRate\":0.9592487597448618,\"orderCount\":3316,\"collectionCount\":139,\"paidRate\":0.22466335931963147,\"rank\":16,\"add2CartTimes\":11963,\"uniqueVisitor\":8466,\"goodsCost\":195438.44,\"categoryName\":\"肉类/熟食\",\"productName\":\"猪八戒大战牛魔王\"},{\"grossMarginRate\":0.29724061802261652521,\"revenue\":251715.06,\"exitRate\":0.9605120957685593,\"orderCount\":973,\"collectionCount\":525,\"paidRate\":0.026436112727575026,\"rank\":17,\"add2CartTimes\":16261,\"uniqueVisitor\":24058,\"goodsCost\":176895.12,\"categoryName\":\"礼盒/礼品\",\"productName\":\"森林大礼包E套餐 1208g\"},{\"grossMarginRate\":0.5200027562627046484,\"revenue\":243808.4,\"exitRate\":0.9670442842430484,\"orderCount\":7278,\"collectionCount\":204,\"paidRate\":0.5793465031364105,\"rank\":18,\"add2CartTimes\":21871,\"uniqueVisitor\":10681,\"goodsCost\":117027.36,\"categoryName\":\"肉类/熟食\",\"productName\":\"烧烤味牛板筋 120g\"},{\"grossMarginRate\":0.53105056765249050079,\"revenue\":241751.78,\"exitRate\":0.963632648891613,\"orderCount\":4806,\"collectionCount\":142,\"paidRate\":0.392685667586066,\"rank\":19,\"add2CartTimes\":15188,\"uniqueVisitor\":9789,\"goodsCost\":113369.36,\"categoryName\":\"坚果/炒货\",\"productName\":\"琥珀核桃仁 165g\"},{\"grossMarginRate\":0.61223590278324733309,\"revenue\":238948.94,\"exitRate\":0.9690298114400084,\"orderCount\":8165,\"collectionCount\":241,\"paidRate\":0.6836616454229433,\"rank\":20,\"add2CartTimes\":28132,\"uniqueVisitor\":9493,\"goodsCost\":92655.82,\"categoryName\":\"坚果/炒货\",\"productName\":\"美栗甘栗仁 100g\"}],1494584900161],[{\"rank\":\"排名\",\"categoryName\":\"品类\",\"productName\":\"商品名称\",\"revenue\":\"销售额\",\"orderCount\":\"订单量\",\"goodsCost\":\"商品成本\",\"grossMarginRate\":\"毛利率\",\"uniqueVisitor\":\"访客数\",\"add2CartTimes\":\"加购次数\",\"collectionCount\":\"收藏量\",\"paidRate\":\"付费率\",\"exitRate\":\"退出率\"},1494584900161]],\"target\":\"ProductRevenue\",\"columnName\":\"\"}]";
            case "ChannelPageInfo":
                return "[{\"datapoints\":[[[{\"installCount\":28399,\"registerCount\":14465,\"channelName\":\"VIVO\"},{\"installCount\":25079,\"registerCount\":88866,\"channelName\":\"APPSTORE\"},{\"installCount\":24250,\"registerCount\":88361,\"channelName\":\"YINGYONGBAO\"},{\"installCount\":21329,\"registerCount\":10271,\"channelName\":\"XIAOMI\"},{\"installCount\":16355,\"registerCount\":9312,\"channelName\":\"OPPO\"},{\"installCount\":14807,\"registerCount\":7746,\"channelName\":\"HUAWEI\"},{\"installCount\":5967,\"registerCount\":2180,\"channelName\":\"360\"},{\"installCount\":4490,\"registerCount\":1749,\"channelName\":\"WANDOUJIA\"},{\"installCount\":4121,\"registerCount\":2346,\"channelName\":\"BAIDU\"},{\"installCount\":3624,\"registerCount\":1610,\"channelName\":\"MEIZU\"},{\"installCount\":1816,\"registerCount\":1071,\"channelName\":\"LESHI\"},{\"installCount\":1413,\"registerCount\":1097,\"channelName\":\"PPZUSHOU\"},{\"installCount\":1101,\"registerCount\":635,\"channelName\":\"PP\"},{\"installCount\":1049,\"registerCount\":667,\"channelName\":\"SANXING\"},{\"installCount\":905,\"registerCount\":1458,\"channelName\":\"CHANNEL_10\"},{\"installCount\":868,\"registerCount\":0,\"channelName\":\"TEST\"},{\"installCount\":619,\"registerCount\":289,\"channelName\":\"JINLI\"},{\"installCount\":441,\"registerCount\":238,\"channelName\":\"LIANXIANG\"},{\"installCount\":317,\"registerCount\":171,\"channelName\":\"ANZHI\"},{\"installCount\":216,\"registerCount\":86,\"channelName\":\"CHUIZI\"},{\"installCount\":208,\"registerCount\":88,\"channelName\":\"SOUGOU\"},{\"installCount\":196,\"registerCount\":0,\"channelName\":\"PREPRODUCTION\"},{\"installCount\":141,\"registerCount\":70,\"channelName\":\"DIDA\"},{\"installCount\":131,\"registerCount\":114,\"channelName\":\"WH-TSD001\"},{\"installCount\":112,\"registerCount\":82,\"channelName\":\"TASHEQU\"},{\"installCount\":82,\"registerCount\":0,\"channelName\":\"NEWTEST\"},{\"installCount\":76,\"registerCount\":1,\"channelName\":\"PRODUCTION\"},{\"installCount\":56,\"registerCount\":35,\"channelName\":\"YOUYI\"},{\"installCount\":41,\"registerCount\":176,\"channelName\":\"JIFENSHANGCHENG\"},{\"installCount\":41,\"registerCount\":32,\"channelName\":\"MEIYOU\"},{\"installCount\":12,\"registerCount\":28,\"channelName\":\"ACFUN\"},{\"installCount\":7,\"registerCount\":2,\"channelName\":\"LAOBEIMEN\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"ZUJI\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"AOTI\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"PRODUCTIONDEBUG\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"WH-TSD003\"},{\"installCount\":3,\"registerCount\":0,\"channelName\":\"MUMAYI\"},{\"installCount\":3,\"registerCount\":1,\"channelName\":\"OFO\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"BEIJINGXILU\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"TIANTIANGUOYUAN\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"TONGCHENG\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"NANRUIHU\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"RONGHUIGUANGCHANG\"},{\"installCount\":2,\"registerCount\":1,\"channelName\":\"XINSHIDAI\"},{\"installCount\":1,\"registerCount\":0,\"channelName\":\"WH-TSD002\"},{\"installCount\":0,\"registerCount\":201962,\"channelName\":\"WEIXIN\"},{\"installCount\":0,\"registerCount\":98310,\"channelName\":\"WAP\"}],1494814325573],[{\"channelName\":\"渠道名称\",\"installCount\":\"下载量\",\"registerCount\":\"注册用户数\"},1494814325573]],\"target\":\"ChannelPageInfo\",\"columnName\":\"\"}]";
            case "AgeDistribution":
                return "[{\"datapoints\":[[477909,1494583003701]],\"target\":\"0-20岁\",\"columnName\":\"\"},{\"datapoints\":[[29078,1494583003701]],\"target\":\"21-25岁\",\"columnName\":\"\"},{\"datapoints\":[[17200,1494583003701]],\"target\":\"26-30岁\",\"columnName\":\"\"},{\"datapoints\":[[6421,1494583003701]],\"target\":\"31-35岁\",\"columnName\":\"\"},{\"datapoints\":[[5364,1494583003701]],\"target\":\"36岁+\",\"columnName\":\"\"}]";
            default:
            return null;
        }
    }
}
