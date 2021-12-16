package com.pat.starter.common.leecode.problems;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * LP3Test
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 * 3. 无重复字符的最长子串
 *
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 * @author chouway
 * @date 2021.09.03
 */
public class LP3Test {

    @Test
    public void testA(){
        String s = "abcbcaebb";
        Assert.assertEquals(4,lengthOfLongestSubstring(s));
    }

    /**
     *   作者：guanpengchn
     *   链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/solution/hua-jie-suan-fa-3-wu-zhong-fu-zi-fu-de-zui-chang-z/
     *   来源：力扣（LeetCode）
     *
     *  1. start不动，end向后移动
     *  2. 当end遇到重复字符，start应该放在上一个重复字符的位置的后一位，同时记录最长的长度
     *  3. 怎样判断是否遇到重复字符，且怎么知道上一个重复字符的位置？--用哈希字典的key来判断是否重复，用value来记录该字符的下一个不重复的位置。
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int end = 0, start = 0; end < n; end++) {
            char alpha = s.charAt(end);
            if (map.containsKey(alpha)) {
                start = Math.max(map.get(alpha), start);
            }
            ans = Math.max(ans, end - start + 1);
            map.put(s.charAt(end), end + 1);
        }
        return ans;
    }

}
