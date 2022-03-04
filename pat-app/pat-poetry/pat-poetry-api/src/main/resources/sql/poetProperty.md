
sample
===
* 注释

	select #{use("cols")} from poet_property  where  #{use("condition")}

cols
===
	id,key,value,rel_type,rel_id,status,remark,index,baike_id

updateSample
===
	
	id=#{id},key=#{key},value=#{value},rel_type=#{relType},rel_id=#{relId},status=#{status},remark=#{remark},index=#{index},baike_id=#{baikeId}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(key)){
	 and key=#{key}
	-- @}
	-- @if(!isEmpty(value)){
	 and value=#{value}
	-- @}
	-- @if(!isEmpty(relType)){
	 and rel_type=#{relType}
	-- @}
	-- @if(!isEmpty(relId)){
	 and rel_id=#{relId}
	-- @}
	-- @if(!isEmpty(status)){
	 and status=#{status}
	-- @}
	-- @if(!isEmpty(remark)){
	 and remark=#{remark}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	-- @if(!isEmpty(baikeId)){
	 and baike_id=#{baikeId}
	-- @}
	
	