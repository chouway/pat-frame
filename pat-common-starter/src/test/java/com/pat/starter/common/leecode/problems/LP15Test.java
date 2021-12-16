package com.pat.starter.common.leecode.problems;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.*;

/**
 * LP15sum
 * 15. 三数之和
 * https://leetcode-cn.com/problems/3sum/
 * <p>
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组。
 *
 * @author chouway
 * @date 2021.09.03
 */
@Slf4j
public class LP15Test {

    @Test
    public void testA() {
        int[] nums = new int[]{1, 2, -1, -2, 0, 1};
        List<List<Integer>> lists = threeSum(nums);
        log.info("testA-->lists={}", JSON.toJSONString(lists));

    }


    @Test
    public void testB(){
        int[] nums = new int[]{1, 2, -1, -2, 0, 1};
        List<List<Integer>> lists = xSumY(nums, 3, -1);
        log.info("testB-->lists={}", JSON.toJSONString(lists));

    }

    /**
     * 实现了 从数组nums中 完全找出x个数的合为y的不重复组合
     *
     * 核心是计算组合序列
     *
     * @param nums
     * @param x
     * @param y
     * @return
     */
    public List<List<Integer>> xSumY(int[] nums, int x, int y) {
        log.info("xSumY-->nums={},x={},y={}", nums,x,y);
        int length = nums.length;
        if (x < 1 || nums == null || length < 1 || length < x) {
            return new ArrayList<List<Integer>>();
        }
        //顺序排列 nums 方便后序去重
        Arrays.sort(nums);
        //去重set
        Set<String> temSet = new HashSet<String>();
        //目标集合
        List<List<Integer>> list = new ArrayList<List<Integer>>();

        List<Integer[]> combX = this.getCombX(nums.length, x);
        for (Integer[] comb : combX) {
            int total = 0;
            Integer[] temV = new Integer[x];
            for (int i = 0; i < comb.length; i++) {
                temV[i] = nums[comb[i]];
                total += temV[i];
            }
            log.info("xSumY-->total={},combX={},temX={}",total,JSON.toJSONString(comb), JSON.toJSONString(temV));
            if(total == y){
                String ukey = StringUtils.join(temV, "_");
                if(!temSet.contains(ukey)){
                    temSet.add(ukey);
                    list.add(Arrays.asList(temV));
                }
            }
        }
        return list;
    };

    @Test
    public void getCombX(){

        long start_time = System.currentTimeMillis();
        List<Integer[]> combX = getCombX(10, 3);// C 40 5 = 658008
        log.info("getCombX-->combX.size()={}", combX.size());
        log.info("getCombX-->combX={}", JSON.toJSONString(combX));
        long end_time = System.currentTimeMillis();log.info(":spend time-->{}ms", end_time - start_time);


    }




    /**
     * 组合排序
     * 优解：
     * 比如你想在5个数中选3个数，那可以设一数组a[3],初始化为a[3] = {1,2,3};// 表示5个数中选1，2，3这三个数。 然后对数组最后一个元素做加1运算，注意进位，并时刻保持a[i]<a[i+1], 且a[2] <= 5，直到a[3] ={3,4,5};就可以求出全部组合数
     * 例：
     * {1,2,3}
     * {1,2,4}
     * {1,2,5}
     * {1,3,4}
     * {1,3,5}
     * {1,4,5}
     * {2,3,4}
     * {2,3,5}
     * {2,4,5}
     * {3,4,5}
     *
     * {1,2,3,4}
     * {1,2,3,5}
     * {1,2,4,5}
     * {1,3,4,5}
     * {2,3,4,5}
     *
     * {1,2,3,4}
     * {1,2,3,5}
     * {1,2,3,6}
     * {1,2,4,5}
     * {1,2,4,6}
     * {1,2,5,6}
     * {1,3,4,5}
     * {1,3,4,6}
     * {1,3,5,6}
     * {1,4,5,6}
     * {2,3,4,5}
     * {2,3,4,6}
     * {2,3,5,6}
     * {2,4,5,6}
     * {3,4,5,6}
     */
    public List<Integer[]> getCombX(int length,int x){
        if(x<0||length<0){
            return null;
        }
        if(x>length){
            return null;
        }
        List<Integer[]> targetL = new ArrayList<Integer[]>();
        Integer[] temX = new Integer[x];
        for (int i = 0; i < x; i++) {
            temX[i] = i+1;
        }

        boolean isContinue = true;
        do{
            targetL.add(Arrays.copyOf(temX,x));
            if(temX[0]==length-x+1){
                isContinue = false;
            }
            for (int i = x; i > 0; i--) {
                if(temX[i-1]!= length-x+i) {//判断  temX[i-1] 是否为最后的序列
                    temX[i-1]++;//非终值 + 1
                    //i-1位以上的都顺序增加
                    int addT=0;
                    for (int j = i; j < x; j++) {//
                        temX[j] = temX[i-1] + ++addT;;
                    }
                    break;
                }else{//进位
                    continue;
                }
            }

        }while(isContinue);
        return targetL;
    }


    public List<List<Integer>> threeSum(int[] nums) {
        Set<String> temSet = new HashSet<String>();
        String SPLIT = "_";
        int length = nums.length;
        Integer a;
        Integer b;
        Integer c;
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        Arrays.sort(nums);
        for (int i = 0; i < length; i++) {
            a = nums[i];
            for (int j = i + 1; j < length; j++) {
                b = nums[j];
                for (int k = j + 1; k < length; k++) {
                    c = nums[k];
                    if (a + b + c == 0) {
                        String ukey = a + SPLIT + b + SPLIT + c;
                        if (!temSet.contains(ukey)) {
                            temSet.add(ukey);
                            List<Integer> listT = new ArrayList<Integer>();
                            listT.add(a);
                            listT.add(b);
                            listT.add(c);
                            list.add(listT);
                        }
                    }
                }
            }
        }
        return list;
    }

}
