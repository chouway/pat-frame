
sample
===
* 注释

	select #{use("cols")} from poet_suggest  where  #{use("condition")}

cols
===
	id,keyword,key_type,rel_id,count,update_ts,version

updateSample
===
	
	id=#{id},keyword=#{keyword},key_type=#{keyType},rel_id=#{relId},count=#{count},update_ts=#{updateTs},version=#{version}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(keyword)){
	 and keyword=#{keyword}
	-- @}
	-- @if(!isEmpty(keyType)){
	 and key_type=#{keyType}
	-- @}
	-- @if(!isEmpty(relId)){
	 and rel_id=#{relId}
	-- @}
	-- @if(!isEmpty(count)){
	 and count=#{count}
	-- @}
	-- @if(!isEmpty(updateTs)){
	 and update_ts=#{updateTs}
	-- @}
	-- @if(!isEmpty(version)){
	 and version=#{version}
	-- @}
	
	