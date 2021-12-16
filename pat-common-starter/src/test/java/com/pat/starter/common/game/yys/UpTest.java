package com.pat.starter.common.game.yys;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * UpTest
 *
 * @author chouway
 * @date 2021.11.18
 */
@Slf4j
public class UpTest {

    //ssr/p  当期up式神  2  非当期up式神  1   非ssrSP
    public static final String TARGET_SSRSP = "2";

    public static final String OTHER_TARGET_SSRSP = "1";

    public static final String NOT_SSRSP = "0";

    @Test
    public void testUp(){
        this.percent(100);
    }

    @Test
    public void average(){
        int step =5;
        int max = 600;
        int min = 0;
        Map<Integer, BigDecimal> map = new LinkedHashMap<Integer, BigDecimal>();
        for (int i = 1; i < 121; i++) {
            int temV = step * i;
            BigDecimal percent = this.percent(temV);
            map.put(temV,percent);
        }
        log.info("average-->map={}", JSON.toJSONString(map));

    }

    @Test
    public void maxProfit(){
        String jsonStr = "{5:0.01558,10:0.03151,15:0.04657,20:0.06063,25:0.07466,30:0.09069,35:0.10188,40:0.11410,45:0.12830,50:0.14138,55:0.15962,60:0.17245,65:0.19176,70:0.20692,75:0.22124,80:0.23449,85:0.24808,90:0.26249,95:0.27265,100:0.28638,105:0.30144,110:0.31278,115:0.32585,120:0.33953,125:0.35229,130:0.36431,135:0.37631,140:0.38599,145:0.39901,150:0.40562,155:0.41566,160:0.42984,165:0.44114,170:0.44845,175:0.46121,180:0.47228,185:0.47685,190:0.48980,195:0.49837,200:0.51040,205:0.51494,210:0.52804,215:0.53439,220:0.54615,225:0.55537,230:0.56196,235:0.57290,240:0.57934,245:0.59080,250:0.59541,255:0.60650,260:0.61182,265:0.62365,270:0.63085,275:0.63843,280:0.64837,285:0.65699,290:0.66475,295:0.66676,300:0.67866,305:0.68833,310:0.69332,315:0.69957,320:0.70789,325:0.71713,330:0.72461,335:0.72886,340:0.73582,345:0.74117,350:0.74908,355:0.76136,360:0.76367,365:0.77338,370:0.77903,375:0.78477,380:0.79123,385:0.79859,390:0.80218,395:0.81244,400:0.81632,405:0.82409,410:0.83001,415:0.83864,420:0.84232,425:0.84852,430:0.85279,435:0.85868,440:0.86403,445:0.87020,450:0.87640,455:0.87950,460:0.88608,465:0.89294,470:0.89677,475:0.90248,480:0.90760,485:0.91217,490:0.91570,495:0.92060,500:0.92305,505:0.92914,510:0.93195,515:0.93439,520:0.93689,525:0.94081,530:0.94413,535:0.94723,540:0.94972,545:0.95142,550:0.95444,555:0.95555,560:0.95944,565:0.96020,570:0.96224,575:0.96358,580:0.96663,585:0.96742,590:0.96928,595:0.97156,600:1.00000}";


        Map<Integer, BigDecimal> map = JSON.parseObject(jsonStr, LinkedHashMap.class);

        Map.Entry<Integer, BigDecimal> temEntry=null;//比较 出到当期式神概率/抽卡数 最大值
        BigDecimal temBD = BigDecimal.ZERO;
        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
            log.info("maxProfit-->entry.getKey={},entry.getVal={}", entry.getKey(),entry.getValue());
            if(entry.getKey()<50){//50以下不参与比较
                continue;
            }
            BigDecimal result = new BigDecimal(entry.getValue().toString()).divide(new BigDecimal(entry.getKey().toString()),5,RoundingMode.CEILING);
            if (temBD.compareTo(result)<1) {
                temBD = result;
                temEntry = entry;
            }
        }
        log.info("maxProfit-->temEntry={}", JSON.toJSONString(temEntry));

    }

    /**
     *   long count = 1000*1000L;//测试次数
     * testArea-->roll={1:3182,2:2978,3:3213,4:3156,5:3103,6:3026,7:3180,8:3087,9:3057,10:3007,11:3039,12:3015,13:2951,14:3027,15:2977,16:2976,17:2964,18:2943,19:2924,20:2894,21:2911,22:2901,23:2983,24:2858,25:2876,26:2768,27:2836,28:2767,29:2714,30:2852,31:2743,32:2778,33:2767,34:2764,35:2674,36:2662,37:2651,38:2657,39:2682,40:2670,41:2564,42:2581,43:2462,44:2488,45:2529,46:2520,47:2463,48:2386,49:2464,50:2394,51:3713,52:3535,53:3685,54:3523,55:3458,56:3495,57:3327,58:3334,59:3429,60:3357,61:3224,62:3287,63:3215,64:3231,65:3132,66:3108,67:3134,68:3020,69:3128,70:3089,71:2849,72:2899,73:2912,74:2913,75:2888,76:2736,77:2788,78:2824,79:2719,80:2713,81:2704,82:2684,83:2707,84:2631,85:2637,86:2563,87:2503,88:2610,89:2579,90:2500,91:2433,92:2547,93:2312,94:2504,95:2425,96:2347,97:2417,98:2370,99:2321,100:2246,101:3002,102:2898,103:2997,104:2949,105:2910,106:2732,107:2801,108:2838,109:2752,110:2783,111:2724,112:2660,113:2692,114:2735,115:2653,116:2590,117:2538,118:2538,119:2454,120:2520,121:2516,122:2443,123:2422,124:2446,125:2437,126:2402,127:2370,128:2387,129:2322,130:2339,131:2225,132:2217,133:2238,134:2138,135:2201,136:2192,137:2145,138:2158,139:2041,140:2161,141:2032,142:2030,143:1991,144:2008,145:1989,146:1939,147:1957,148:1905,149:1977,150:1885,151:2394,152:2376,153:2416,154:2366,155:2345,156:2264,157:2313,158:2311,159:2234,160:2160,161:2227,162:2217,163:2236,164:2173,165:2134,166:2114,167:2180,168:2136,169:2130,170:2104,171:2093,172:2010,173:1921,174:2077,175:1991,176:2062,177:1982,178:2012,179:1915,180:1938,181:1906,182:1961,183:1865,184:1846,185:1833,186:1966,187:1880,188:1853,189:1819,190:1764,191:1796,192:1868,193:1725,194:1837,195:1741,196:1826,197:1766,198:1750,199:1761,200:1720,201:2087,202:2033,203:2043,204:1985,205:1985,206:1896,207:2047,208:2001,209:1998,210:1991,211:2008,212:1893,213:1944,214:1907,215:1859,216:1852,217:1842,218:1947,219:1819,220:1829,221:1737,222:1831,223:1876,224:1776,225:1699,226:1813,227:1765,228:1766,229:1692,230:1798,231:1693,232:1694,233:1790,234:1666,235:1747,236:1777,237:1687,238:1675,239:1622,240:1684,241:1642,242:1688,243:1623,244:1658,245:1667,246:1650,247:1664,248:1602,249:1563,250:1542,251:1919,252:1786,253:1836,254:1744,255:1776,256:1788,257:1827,258:1740,259:1721,260:1754,261:1745,262:1706,263:1742,264:1673,265:1693,266:1674,267:1727,268:1635,269:1703,270:1744,271:1651,272:1677,273:1603,274:1603,275:1601,276:1594,277:1605,278:1566,279:1583,280:1518,281:1550,282:1581,283:1641,284:1477,285:1464,286:1584,287:1559,288:1465,289:1450,290:1521,291:1477,292:1470,293:1455,294:1502,295:1481,296:1481,297:1447,298:1380,299:1442,300:1394,301:1588,302:1597,303:1652,304:1631,305:1615,306:1661,307:1633,308:1552,309:1540,310:1588,311:1589,312:1507,313:1608,314:1549,315:1514,316:1506,317:1494,318:1438,319:1521,320:1508,321:1470,322:1446,323:1372,324:1466,325:1480,326:1397,327:1474,328:1420,329:1349,330:1358,331:1385,332:1390,333:1397,334:1344,335:1351,336:1379,337:1298,338:1388,339:1330,340:1309,341:1318,342:1324,343:1311,344:1293,345:1295,346:1231,347:1230,348:1260,349:1248,350:1249,351:1590,352:1585,353:1538,354:1549,355:1570,356:1559,357:1558,358:1531,359:1505,360:1448,361:1530,362:1420,363:1433,364:1463,365:1453,366:1365,367:1357,368:1378,369:1398,370:1367,371:1344,372:1408,373:1370,374:1386,375:1356,376:1416,377:1355,378:1284,379:1310,380:1309,381:1304,382:1292,383:1261,384:1248,385:1294,386:1264,387:1222,388:1210,389:1237,390:1266,391:1214,392:1188,393:1189,394:1124,395:1162,396:1151,397:1150,398:1148,399:1151,400:1168,401:1434,402:1353,403:1277,404:1362,405:1321,406:1274,407:1288,408:1345,409:1284,410:1317,411:1303,412:1243,413:1276,414:1320,415:1233,416:1208,417:1190,418:1264,419:1192,420:1164,421:1208,422:1133,423:1187,424:1190,425:1134,426:1134,427:1113,428:1088,429:1188,430:1100,431:1113,432:1039,433:1044,434:969,435:1048,436:1038,437:1042,438:1059,439:1026,440:1020,441:1003,442:1006,443:1001,444:950,445:949,446:931,447:995,448:971,449:906,450:883,451:1249,452:1270,453:1253,454:1209,455:1214,456:1227,457:1150,458:1190,459:1155,460:1163,461:1159,462:1177,463:1104,464:1120,465:1042,466:1104,467:1084,468:1046,469:1010,470:1062,471:1022,472:1018,473:1006,474:977,475:991,476:978,477:949,478:966,479:886,480:1001,481:899,482:908,483:916,484:872,485:937,486:797,487:828,488:854,489:856,490:852,491:783,492:866,493:813,494:859,495:815,496:793,497:808,498:750,499:818,500:796,501:734,502:790,503:807,504:752,505:745,506:723,507:722,508:727,509:658,510:680,511:666,512:708,513:646,514:670,515:618,516:701,517:640,518:687,519:644,520:622,521:604,522:649,523:611,524:626,525:577,526:594,527:592,528:539,529:528,530:590,531:565,532:580,533:603,534:551,535:530,536:539,537:530,538:505,539:504,540:506,541:553,542:485,543:487,544:495,545:464,546:519,547:498,548:501,549:478,550:461,551:431,552:454,553:472,554:479,555:456,556:445,557:473,558:417,559:450,560:435,561:428,562:443,563:368,564:379,565:417,566:418,567:396,568:367,569:379,570:432,571:387,572:378,573:347,574:386,575:376,576:357,577:368,578:353,579:334,580:341,581:344,582:325,583:328,584:335,585:328,586:316,587:303,588:330,589:314,590:305,591:309,592:281,593:301,594:291,595:285,596:278,597:272,598:288,599:288,600:27690}
     * testArea-->comMap={50:141063,100:144704,150:120278,200:101313,250:90052,300:80754,350:71852,400:67377,450:57115,500:49601,550:30203,600:45676}
     */
    @Test
    public void testArea(){
        long count = 1000*1000L;//测试次数
        Map<Integer, Integer> roll = roll(count);
        log.info("testArea-->roll={}", JSON.toJSONString(roll));
        Map<Integer, Integer> comMap = new TreeMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : roll.entrySet()) {
            int newKey = (1 + (entry.getKey()-1) / 50) * 50;
            int newVal = -1;
            if (comMap.containsKey(newKey)) {
                newVal = comMap.get(newKey);
            }
            newVal+= entry.getValue();
            comMap.put(newKey,newVal);
        }
        log.info("testArea-->comMap={}", JSON.toJSONString(comMap));

    }

    private Map<Integer,Integer>  roll(long count){
        boolean isQt = true;

        int maxC  = 600;//限最多600抽
//        int targetGet = 10;//targetGet  目标抽数内获取主选的概率
        Map<Integer, Integer> cache = new TreeMap<Integer,Integer>();
        for (int i = 0; i < count; i++) {
            UpTest.RollInfo rollInfo = new UpTest.RollInfo(isQt);
            int main = 0;
            int sub = 0;
            for (int j = 0; j < maxC; j++) {
                String nextRoll = rollInfo.getNextRoll();
                //判定结束抽卡的规则
                if(TARGET_SSRSP.equals(nextRoll)){
                    rollInfo.setEnd(true);
                }
                if (rollInfo.getEnd()) {
                    break;
                }
            }
            Integer key = rollInfo.getIndex();
            Integer value = 0;
            if(cache.containsKey(key)){
                value = cache.get(key);
            }
            cache.put(key, ++value);
        }
        return cache;
    }

    private BigDecimal percent(Integer targetGet){
        long count = 100*1000L;//测试次数
        Map<Integer, Integer> cache = roll(100*1000L);

        //统计结果 160以内抽出 当期式神的抽卡数
        int targetCount = 0;

        Map<Integer,Integer> targetCache = new HashMap<Integer,Integer>();
        for (Map.Entry<Integer, Integer> entry : cache.entrySet()) {
            Integer key = entry.getKey();
            if(key<=targetGet){
                Integer value = entry.getValue();
                targetCount+= value;
                targetCache.put(key, value);
            }
        }

        log.info("up-->cache={}", JSON.toJSONString(cache));
        log.info("up-->targetCache={}", JSON.toJSONString(targetCache));
        BigDecimal percent = new BigDecimal(targetCount).divide(new BigDecimal(count)).setScale(5, RoundingMode.CEILING);
        log.info("up-->targetGet={},targetCount={},count={},percent={}", targetGet,targetCount,count, percent);
        return percent;
    }


    public class RollInfo implements Serializable {

        private static final long serialVersionUID = 1L;
        //当前已抽卡数
        private Integer index = 0;

        //当前up数   up时概率为   原ssr/sp概率为 1.25%    up增加为2.5倍
        private Integer upNum = 3;
        //三次up用完时  当前抽卡数
        private Integer upEndIndex = 0;

        //本期指定式神出现概率  全图  初始10%   在 500后 抽增至 100%   第600抽必定出现
        private Integer targetQtBase = 10;

        //有up的抽卡概率  3.125%
        private Integer upSsrSpBase = 3125; //125*25
        //无up的抽卡概率  1.25%
        private Integer baseSsrSpBase = 1250; //125*25

        private Integer totalBase = 100000;

        private Boolean isEnd = false;

        /**
         * 是否为全图
         */
        private Boolean isQT = true;

        public RollInfo(boolean isQT){
            this.isQT = isQT;
        }

        public Integer getUpNum() {
            return upNum;
        }

        public Integer getIndex() {
            return index;
        }

        public Integer getUpEndIndex() {
            return upEndIndex;
        }

        public Boolean getEnd() {
            return isEnd;
        }

        public void setEnd(Boolean end) {
            isEnd = end;
        }

        public String getNextRoll(){
            String rollType = rollType();
            ++this.index;
            return rollType;
        }


        /**
         * https://bbs.nga.cn/read.php?tid=29486123&_ff=538&page=e#pid566963746Anchor
         * https://www.wandoujia.com/strategy/7724105/a_16206377658849586767.html
         */
        private String rollType(){
            if(!isEnd&&index==600){//600必出主选
                return TARGET_SSRSP;
            }
            if(upNum>0){//还有up次数
                int randomRoll = new Random().nextInt(totalBase);
                if(randomRoll<upSsrSpBase){//出up了
                    if(this.upNum==1){//最后一次up 记录当前抽卡次数
                        this.upEndIndex = index;
                    }
                    --this.upNum;
                    Integer curUpBase = this.getCurUpBase();//获取当期式神up 基数
                    int randomSsrSp = new Random().nextInt(100);
                    if(randomSsrSp<curUpBase){//出当前式神啦
                        return TARGET_SSRSP;
                    }else {
                        return OTHER_TARGET_SSRSP;
                    }
                }else{
                    return NOT_SSRSP;
                }
            }else{//无up 但扔在抽
                int randomRoll = new Random().nextInt(totalBase);
                if(randomRoll<baseSsrSpBase){
                    Integer curUpBase = this.getCurUpBase();//获取当期式神up 基数
                    int randomSsrSp = new Random().nextInt(100);
                    if(randomSsrSp<curUpBase){//出当前式神啦
                        return TARGET_SSRSP;
                    }else {
                        return OTHER_TARGET_SSRSP;
                    }
                }else{
                    return NOT_SSRSP;
                }
            }

        }


        private Integer getCurUpBase(){
            if(index<0){
                throw new RuntimeException("无效抽卡次数");
            }
            if(isQT){//全图
                if(index<50){
                    return 10;
                }else if(index <100){
                    return 15;
                } else if (index < 150) {
                    return 20;
                } else if (index < 200) {
                    return 25;
                } else if (index < 250) {
                    return 30;
                } else if (index < 300) {
                    return 35;
                } else if (index < 350) {
                    return 40;
                } else if (index < 400) {
                    return 50;
                } else if (index < 450) {
                    return 60;
                } else if (index < 500) {
                    return 80;
                } else {
                    return 80;
                }
            }else{//非全图
                if(index<50){
                    return 3;
                }else if(index <100){
                    return 4;
                } else if (index < 150) {
                    return 5;
                } else if (index < 200) {
                    return 6;
                } else if (index < 250) {
                    return 8;
                } else if (index < 300) {
                    return 10;
                } else if (index < 350) {
                    return 11;
                } else if (index < 400) {
                    return 12;
                } else if (index < 450) {
                    return 13;
                } else if (index < 500) {
                    return 14;
                } else {
                    return 15;
                }
            }
        }
    }
}
