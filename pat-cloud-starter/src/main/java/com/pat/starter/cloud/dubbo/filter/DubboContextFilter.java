package com.pat.starter.cloud.dubbo.filter;

import com.pat.starter.cloud.dubbo.constant.DubboConstant;
import com.pat.starter.common.util.PatLogIdUtils;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;

/**
 * DubboContextFilter
 * 服务  消费方都会引用这个过滤器  以便透传logId
 * <p>
 * 目前logId由 error时产生
 *
 * @author chouway
 * @date 2021.05.16
 */
public class DubboContextFilter implements Filter {

    /**
     * eg A 调用 B   B 调用 C
     * C抛出异常  生成了logId
     *
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String logId = RpcContext.getContext().getAttachment(DubboConstant.LOG_ID);
        if ( StringUtils.hasText(logId)) {
            // *) 从RpcContext里获取logId并保存
            PatLogIdUtils.setLogId(logId);
        } else {
            logId = PatLogIdUtils.generateLogId();
            // *) 交互前重新设置logId, 避免信息丢失
            RpcContext.getContext().setAttachment(DubboConstant.LOG_ID, logId);
        }
        return invoker.invoke(invocation);
    }
}
