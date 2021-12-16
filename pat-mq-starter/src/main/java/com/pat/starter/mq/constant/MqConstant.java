package com.pat.starter.mq.constant;

import com.pat.api.exception.BusinessException;
import com.pat.starter.mq.constant.QueueEnum;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

/**
 * MqConstant
 * https://blog.csdn.net/qq_38288606/article/details/115863955
 * Springboot 整合 RabbitMQ「三种模式使用」
 *
 * @author chouway
 * @date 2021.05.10
 */
public class MqConstant {

    private MqConstant(){}

    /**
     * Direct Exchange 直连型交换机 根据消息携带的路由键将消息投递给对应队列。
     */
    public static final String AIBK_DERIECT_EXCHANGE = "aibkDeriectExchange";


     // 点对点队列 str

    public static final String Queue_LockMockService_Process = "queue.lockMockService.process";

    public static final String Queue_AibkErrorService_Add = "queue.aibkErrorService.add";

    public static final String Queue_AibkErrorService_Handle = "queue.aibkErrorService.handle";

    // 点对点队列 end

    /**
     * 队列获取交换机key
     * @param queueEnum
     * @return
     */
    public static String getExchange(QueueEnum queueEnum){
        String queueName = queueEnum.getQueueName();
        if(queueName.indexOf("queue")==0){
            return AIBK_DERIECT_EXCHANGE;
        }
        throw new BusinessException("未定义交换机");
    }

    /**
     * 核验是否配置的队列名称是否预定义
     * @param queueNames
     */
    public static void checkQueue(Set<String> queueNames){
        if(CollectionUtils.isEmpty(queueNames)){
            return;
        }
        Set<String> results = new LinkedHashSet<String>();
        results.addAll(queueNames);
        for (QueueEnum queueEnum : QueueEnum.values()) {
            results.remove(queueEnum.getQueueName());
        }
        if(results.size()!=0){
            throw new BusinessException("请核验未定义的队列:"+queueNames);
        }
    };
}
