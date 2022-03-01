
sample
===
* 注释

	select #{use("cols")} from poet_author  where  #{use("condition")}

cols
===
	id,name,describe

updateSample
===
	
	id=#{id},name=#{name},describe=#{describe}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(name)){
	 and name=#{name}
	-- @}
	-- @if(!isEmpty(describe)){
	 and describe=#{describe}
	-- @}
	
	