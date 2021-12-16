package com.pat.starter.mq.constant;

/**
 * QueueEnum
 *
 * @author chouway
 * @date 2021.05.10
 */
public enum  QueueEnum {

    Queue_LockMockService_Process(MqConstant.Queue_LockMockService_Process),
    Queue_AibkErrorService_Add(MqConstant.Queue_AibkErrorService_Add),
    Queue_AibkErrorService_Handle(MqConstant.Queue_AibkErrorService_Handle);


    private QueueEnum(String queueName){
        this.queueName = queueName;
    }
    /**
     * queueName  约定
     * 形如： queue.xxx.yyy
     * 为点对点队列模式  ,xxx代表队列接收后处理的接口名称(msgHandleService),yyy为相应的接口方法(msgHandleMethod)。约定该接口方法只能是单体参数 （降低复杂度）
     */
    private String queueName;

    public String getQueueName() {
        return queueName;
    }
}
