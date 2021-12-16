package com.pat.starter.common.util;

import com.pat.api.exception.BusinessException;
import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * AibkCompressUtil
 *
 * @author chouway
 * @date 2021.04.25
 */
@Slf4j
public class PatCompressUtil {
    /**
     * clean + 压缩
     * @param rawText
     * @return
     */
    public static byte[] compress(String rawText){
        if(!StringUtils.hasText(rawText)){
            throw new RuntimeException("没有文本内容");
        }
        //去掉脚本script 去掉注释
        String cleanRawText = rawText;
        cleanRawText = cleanRawText.replaceAll("<script[^>]*?>.*?</script>|<!--.*?>","");
        //多个空白以单个空格替换
        cleanRawText = cleanRawText.replaceAll("[\\s]{2,}"," ");
        //标签内无内容的删除掉  排除a标签和img给去掉。
        cleanRawText = cleanRawText.replaceAll("<(?!a|img|image)(\\w*+)[^>]*?></\\1>","");

        byte[] sourceBytes = cleanRawText.getBytes(StandardCharsets.UTF_8);
        return LZFEncoder.encode(sourceBytes);
    }

    /**
     * 解压缩
     * @param compressed
     * @return
     */
    public static String uncompressed(byte[] compressed){
        byte[] uncompressed = null;
        try{
            uncompressed = LZFDecoder.decode(compressed);
        }catch(Exception e){
            throw new BusinessException("解压缩失败",e);
        }
        return new String(uncompressed,StandardCharsets.UTF_8);
    }
}
