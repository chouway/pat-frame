
sample
===
* 注释

	select #{use("cols")} from pat_error  where  #{use("condition")}

cols
===
	id,project,service,method,param_clazz,param_json,exception,error_code,message,stack_trace,log_id,ip,status,handle_result_json,handle_error_id,remark,create_ts,update_ts

updateSample
===
	
	id=#{id},project=#{project},service=#{service},method=#{method},param_clazz=#{paramClazz},param_json=#{paramJson},exception=#{exception},error_code=#{errorCode},message=#{message},stack_trace=#{stackTrace},log_id=#{logId},ip=#{ip},status=#{status},handle_result_json=#{handleResultJson},handle_error_id=#{handleErrorId},remark=#{remark},create_ts=#{createTs},update_ts=#{updateTs}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(project)){
	 and project=#{project}
	-- @}
	-- @if(!isEmpty(service)){
	 and service=#{service}
	-- @}
	-- @if(!isEmpty(method)){
	 and method=#{method}
	-- @}
	-- @if(!isEmpty(paramClazz)){
	 and param_clazz=#{paramClazz}
	-- @}
	-- @if(!isEmpty(paramJson)){
	 and param_json=#{paramJson}
	-- @}
	-- @if(!isEmpty(exception)){
	 and exception=#{exception}
	-- @}
	-- @if(!isEmpty(errorCode)){
	 and error_code=#{errorCode}
	-- @}
	-- @if(!isEmpty(message)){
	 and message=#{message}
	-- @}
	-- @if(!isEmpty(stackTrace)){
	 and stack_trace=#{stackTrace}
	-- @}
	-- @if(!isEmpty(logId)){
	 and log_id=#{logId}
	-- @}
	-- @if(!isEmpty(ip)){
	 and ip=#{ip}
	-- @}
	-- @if(!isEmpty(status)){
	 and status=#{status}
	-- @}
	-- @if(!isEmpty(handleResultJson)){
	 and handle_result_json=#{handleResultJson}
	-- @}
	-- @if(!isEmpty(handleErrorId)){
	 and handle_error_id=#{handleErrorId}
	-- @}
	-- @if(!isEmpty(remark)){
	 and remark=#{remark}
	-- @}
	-- @if(!isEmpty(createTs)){
	 and create_ts=#{createTs}
	-- @}
	-- @if(!isEmpty(updateTs)){
	 and update_ts=#{updateTs}
	-- @}
	
	