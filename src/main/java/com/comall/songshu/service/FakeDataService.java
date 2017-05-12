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
                return "[{\"datapoints\":[[[{\"grossMarginRate\":0.43307846531434640538,\"revenue\":5798807.84,\"exitRate\":1,\"orderCount\":87310,\"collectionCount\":313,\"paidRate\":0.9190212817121558,\"rank\":1,\"add2CartTimes\":67882,\"uniqueVisitor\":16634,\"goodsCost\":3287469.04,\"categoryName\":\"坚果/炒货\",\"productName\":\"夏威夷果 265g\"},{\"grossMarginRate\":0.46202922521576354656,\"revenue\":4839017.14,\"exitRate\":1,\"orderCount\":84526,\"collectionCount\":201,\"paidRate\":0.8293455054111757,\"rank\":2,\"add2CartTimes\":49789,\"uniqueVisitor\":13583,\"goodsCost\":2603249.8,\"categoryName\":\"坚果/炒货\",\"productName\":\"开心果 225g\"},{\"grossMarginRate\":0.39980558603563497791,\"revenue\":4137748.04,\"exitRate\":1,\"orderCount\":96154,\"collectionCount\":183,\"paidRate\":1.235654870999577,\"rank\":3,\"add2CartTimes\":37897,\"uniqueVisitor\":14186,\"goodsCost\":2483453.26,\"categoryName\":\"坚果/炒货\",\"productName\":\"碧根果 225g\"},{\"grossMarginRate\":0.37315390038640440155,\"revenue\":4076149.22,\"exitRate\":1,\"orderCount\":85754,\"collectionCount\":277,\"paidRate\":0.993648736228127,\"rank\":4,\"add2CartTimes\":70409,\"uniqueVisitor\":15430,\"goodsCost\":2555118.24,\"categoryName\":\"坚果/炒货\",\"productName\":\"开口松子原味 218g\"},{\"grossMarginRate\":0.41521357637362776079,\"revenue\":2843962.98,\"exitRate\":1,\"orderCount\":70651,\"collectionCount\":289,\"paidRate\":0.7274898495342728,\"rank\":5,\"add2CartTimes\":66407,\"uniqueVisitor\":16748,\"goodsCost\":1663110.94,\"categoryName\":\"坚果/炒货\",\"productName\":\"手剥巴旦木 235g\"},{\"grossMarginRate\":0.351382280724348211,\"revenue\":2458364.6,\"exitRate\":1,\"orderCount\":24127,\"collectionCount\":388,\"paidRate\":0.4390786247790638,\"rank\":6,\"add2CartTimes\":69312,\"uniqueVisitor\":17539,\"goodsCost\":1594538.84,\"categoryName\":\"坚果/炒货\",\"productName\":\"坚果三兄弟组合\"},{\"grossMarginRate\":0.37399195328136061914,\"revenue\":2423094.54,\"exitRate\":1,\"orderCount\":65194,\"collectionCount\":348,\"paidRate\":0.9308098591549295,\"rank\":7,\"add2CartTimes\":63865,\"uniqueVisitor\":17040,\"goodsCost\":1516876.68,\"categoryName\":\"坚果/炒货\",\"productName\":\"【爆卖】炭烧腰果185g\"},{\"grossMarginRate\":0.51265377302803147019,\"revenue\":1280564.3,\"exitRate\":1,\"orderCount\":35384,\"collectionCount\":590,\"paidRate\":0.7127400248043451,\"rank\":8,\"add2CartTimes\":80104,\"uniqueVisitor\":23383,\"goodsCost\":624078.18,\"categoryName\":\"果干/蜜饯\",\"productName\":\"芒果干 116g\"},{\"grossMarginRate\":0.44091394262726506484,\"revenue\":693774.84,\"exitRate\":1,\"orderCount\":20719,\"collectionCount\":245,\"paidRate\":0.8667083296134274,\"rank\":9,\"add2CartTimes\":34593,\"uniqueVisitor\":11201,\"goodsCost\":387879.84,\"categoryName\":\"果干/蜜饯\",\"productName\":\"草莓干 106g\"},{\"grossMarginRate\":0.41935463559030973968,\"revenue\":641605.88,\"exitRate\":1,\"orderCount\":13041,\"collectionCount\":469,\"paidRate\":0.3302704852824185,\"rank\":10,\"add2CartTimes\":87078,\"uniqueVisitor\":25140,\"goodsCost\":372545.48,\"categoryName\":\"坚果/炒货\",\"productName\":\"碧根果 210g\"},{\"grossMarginRate\":0.41215273379884131006,\"revenue\":596718.74,\"exitRate\":1,\"orderCount\":13283,\"collectionCount\":555,\"paidRate\":0.25346342916728737,\"rank\":11,\"add2CartTimes\":63438,\"uniqueVisitor\":26852,\"goodsCost\":350779.48,\"categoryName\":\"坚果/炒货\",\"productName\":\"夏威夷果 185g\"},{\"grossMarginRate\":0.37498347236718176543,\"revenue\":541971.14,\"exitRate\":1,\"orderCount\":10231,\"collectionCount\":224,\"paidRate\":0.7143406012871002,\"rank\":12,\"add2CartTimes\":28635,\"uniqueVisitor\":10411,\"goodsCost\":338740.92,\"categoryName\":\"肉类/熟食\",\"productName\":\"猪肉脯原味 210g\"},{\"grossMarginRate\":0.6030067841858288954,\"revenue\":377878.8,\"exitRate\":1,\"orderCount\":29239,\"collectionCount\":158,\"paidRate\":0.9768947568871398,\"rank\":13,\"add2CartTimes\":36031,\"uniqueVisitor\":7877,\"goodsCost\":150015.32,\"categoryName\":\"坚果/炒货\",\"productName\":\"紫薯花生 120g\"},{\"grossMarginRate\":0.56101976890762888787,\"revenue\":376388.02,\"exitRate\":1,\"orderCount\":4833,\"collectionCount\":165,\"paidRate\":0.3806083650190114,\"rank\":14,\"add2CartTimes\":10690,\"uniqueVisitor\":10520,\"goodsCost\":165226.9,\"categoryName\":\"坚果/炒货\",\"productName\":\"枣夹核桃仁 258g\"},{\"grossMarginRate\":0.4753061902462624943,\"revenue\":332443.64,\"exitRate\":1,\"orderCount\":8258,\"collectionCount\":124,\"paidRate\":0.8829226847918437,\"rank\":15,\"add2CartTimes\":10256,\"uniqueVisitor\":5885,\"goodsCost\":174431.12,\"categoryName\":\"果干/蜜饯\",\"productName\":\"冻干榴莲 36g\"},{\"grossMarginRate\":0.35740632603406326034,\"revenue\":304140,\"exitRate\":1,\"orderCount\":3384,\"collectionCount\":139,\"paidRate\":0.24011821204285186,\"rank\":16,\"add2CartTimes\":11963,\"uniqueVisitor\":8121,\"goodsCost\":195438.44,\"categoryName\":\"肉类/熟食\",\"productName\":\"猪八戒大战牛魔王\"},{\"grossMarginRate\":0.55862240012998360043,\"revenue\":265141.14,\"exitRate\":1,\"orderCount\":7992,\"collectionCount\":204,\"paidRate\":0.655339335850518,\"rank\":17,\"add2CartTimes\":21871,\"uniqueVisitor\":10329,\"goodsCost\":117027.36,\"categoryName\":\"肉类/熟食\",\"productName\":\"烧烤味牛板筋 120g\"},{\"grossMarginRate\":0.56870994889847891506,\"revenue\":262861.06,\"exitRate\":1,\"orderCount\":5309,\"collectionCount\":142,\"paidRate\":0.45096999893989187,\"rank\":18,\"add2CartTimes\":15188,\"uniqueVisitor\":9433,\"goodsCost\":113369.36,\"categoryName\":\"坚果/炒货\",\"productName\":\"琥珀核桃仁 165g\"},{\"grossMarginRate\":0.48562680578313811093,\"revenue\":259063.5,\"exitRate\":1,\"orderCount\":2152,\"collectionCount\":78,\"paidRate\":0.25580634609093883,\"rank\":19,\"add2CartTimes\":7253,\"uniqueVisitor\":6114,\"goodsCost\":133255.32,\"categoryName\":\"坚果/炒货\",\"productName\":\"纯粹丨手剥松子125g\"},{\"grossMarginRate\":0.63430881168445923097,\"revenue\":253371.76,\"exitRate\":1,\"orderCount\":8686,\"collectionCount\":241,\"paidRate\":0.7506250679421677,\"rank\":20,\"add2CartTimes\":28132,\"uniqueVisitor\":9199,\"goodsCost\":92655.82,\"categoryName\":\"坚果/炒货\",\"productName\":\"美栗甘栗仁 100g\"}],1494558580785],[{\"rank\":\"排名\",\"categoryName\":\"品类\",\"productName\":\"商品名称\",\"revenue\":\"销售额\",\"orderCount\":\"订单量\",\"goodsCost\":\"商品成本\",\"grossMarginRate\":\"毛利率\",\"uniqueVisitor\":\"访客数\",\"add2CartTimes\":\"加购次数\",\"collectionCount\":\"收藏量\",\"paidRate\":\"付费率\",\"exitRate\":\"退出率\"},1494558580785]],\"target\":\"ProductRevenue\",\"columnName\":\"\"}]";

            case "ChannelPageInfo":
                return "[{\"datapoints\":[[[{\"installCount\":37000,\"registerCount\":89083,\"channelName\":\"yingyongbao\"},{\"installCount\":28400,\"registerCount\":14465,\"channelName\":\"vivo\"},{\"installCount\":25079,\"registerCount\":89308,\"channelName\":\"ios\"},{\"installCount\":21329,\"registerCount\":10271,\"channelName\":\"xiaomi\"},{\"installCount\":16355,\"registerCount\":9312,\"channelName\":\"oppo\"},{\"installCount\":14807,\"registerCount\":7746,\"channelName\":\"huawei\"},{\"installCount\":5967,\"registerCount\":2180,\"channelName\":\"360\"},{\"installCount\":4490,\"registerCount\":1749,\"channelName\":\"wandoujia\"},{\"installCount\":4121,\"registerCount\":2346,\"channelName\":\"baidu\"},{\"installCount\":3624,\"registerCount\":1610,\"channelName\":\"meizu\"},{\"installCount\":1816,\"registerCount\":1071,\"channelName\":\"leshi\"},{\"installCount\":1413,\"registerCount\":1097,\"channelName\":\"ppzushou\"},{\"installCount\":1101,\"registerCount\":635,\"channelName\":\"pp\"},{\"installCount\":1049,\"registerCount\":667,\"channelName\":\"sanxing\"},{\"installCount\":905,\"registerCount\":1458,\"channelName\":\"channel_10\"},{\"installCount\":868,\"registerCount\":0,\"channelName\":\"test\"},{\"installCount\":619,\"registerCount\":289,\"channelName\":\"jinli\"},{\"installCount\":441,\"registerCount\":238,\"channelName\":\"lianxiang\"},{\"installCount\":317,\"registerCount\":171,\"channelName\":\"anzhi\"},{\"installCount\":216,\"registerCount\":86,\"channelName\":\"chuizi\"},{\"installCount\":208,\"registerCount\":88,\"channelName\":\"sougou\"},{\"installCount\":196,\"registerCount\":0,\"channelName\":\"preproduction\"},{\"installCount\":141,\"registerCount\":70,\"channelName\":\"dida\"},{\"installCount\":131,\"registerCount\":114,\"channelName\":\"WH-TSD001\"},{\"installCount\":112,\"registerCount\":82,\"channelName\":\"tashequ\"},{\"installCount\":82,\"registerCount\":0,\"channelName\":\"newtest\"},{\"installCount\":76,\"registerCount\":1,\"channelName\":\"production\"},{\"installCount\":56,\"registerCount\":35,\"channelName\":\"youyi\"},{\"installCount\":41,\"registerCount\":176,\"channelName\":\"jifenshangcheng\"},{\"installCount\":41,\"registerCount\":32,\"channelName\":\"meiyou\"},{\"installCount\":12,\"registerCount\":28,\"channelName\":\"acfun\"},{\"installCount\":7,\"registerCount\":2,\"channelName\":\"laobeimen\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"zuji\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"productiondebug\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"aoti\"},{\"installCount\":5,\"registerCount\":0,\"channelName\":\"WH-TSD003\"},{\"installCount\":3,\"registerCount\":0,\"channelName\":\"mumayi\"},{\"installCount\":3,\"registerCount\":1,\"channelName\":\"ofo\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"ronghuiguangchang\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"tiantianguoyuan\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"tongcheng\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"beijingxilu\"},{\"installCount\":2,\"registerCount\":0,\"channelName\":\"nanruihu\"},{\"installCount\":2,\"registerCount\":1,\"channelName\":\"xinshidai\"},{\"installCount\":1,\"registerCount\":0,\"channelName\":\"WH-TSD002\"}],1494579678376],[{\"channelName\":\"渠道名称\",\"installCount\":\"下载量\",\"registerCount\":\"注册用户数\"},1494579678376]],\"target\":\"ChannelPageInfo\",\"columnName\":\"\"}]";

            default:
            return null;
        }
    }
}
