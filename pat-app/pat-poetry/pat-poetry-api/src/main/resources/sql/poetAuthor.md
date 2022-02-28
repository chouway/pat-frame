
sample
===
* 注释

	select #{use("cols")} from poet_author  where  #{use("condition")}

cols
===
	id,name,baike_id,describe

updateSample
===
	
	id=#{id},name=#{name},baike_id=#{baikeId},describe=#{describe}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(name)){
	 and name=#{name}
	-- @}
	-- @if(!isEmpty(baikeId)){
	 and baike_id=#{baikeId}
	-- @}
	-- @if(!isEmpty(describe)){
	 and describe=#{describe}
	-- @}
	
	