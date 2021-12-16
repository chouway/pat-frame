
sample
===
* 注释

	select #{use("cols")} from pat_config  where  #{use("condition")}

cols
===
	id,config_name,config_code,config_value,remark,seq_no,parent_id

updateSample
===
	
	id=#{id},config_name=#{configName},config_code=#{configCode},config_value=#{configValue},remark=#{remark},seq_no=#{seqNo},parent_id=#{parentId}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(configName)){
	 and config_name=#{configName}
	-- @}
	-- @if(!isEmpty(configCode)){
	 and config_code=#{configCode}
	-- @}
	-- @if(!isEmpty(configValue)){
	 and config_value=#{configValue}
	-- @}
	-- @if(!isEmpty(remark)){
	 and remark=#{remark}
	-- @}
	-- @if(!isEmpty(seqNo)){
	 and seq_no=#{seqNo}
	-- @}
	-- @if(!isEmpty(parentId)){
	 and parent_id=#{parentId}
	-- @}
	
uniqueByTypeAndCode
===
    
    select * from pat_config where config_code = #{configCode} and parent_id in (
        select id from pat_config where parent_id is null and config_code=#{typeCode} order by seq_no asc nulls first limit 1 
    ) limit 1 


getTreeById
===

    WITH RECURSIVE temp as(
        select * from pat_config where id = #{id}
        union all
        (select a.* from pat_config a inner join temp on temp.id = a.parent_id)
    )
    select * from temp limit 999