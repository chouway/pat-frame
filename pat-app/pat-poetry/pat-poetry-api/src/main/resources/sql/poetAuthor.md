
sample
===
* 注释

	select #{use("cols")} from poet_author  where  #{use("condition")}

cols
===
	id,name,desc,baike_id

updateSample
===
	
	id=#{id},name=#{name},desc=#{desc},baike_id=#{baikeId}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(name)){
	 and name=#{name}
	-- @}
	-- @if(!isEmpty(desc)){
	 and desc=#{desc}
	-- @}
	-- @if(!isEmpty(baikeId)){
	 and baike_id=#{baikeId}
	-- @}
	
	