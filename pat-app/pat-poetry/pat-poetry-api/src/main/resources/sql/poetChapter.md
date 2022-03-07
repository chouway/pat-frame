
sample
===
* 注释

	select #{use("cols")} from poet_chapter  where  #{use("condition")}

cols
===
	id,chapter,index,set_id

updateSample
===
	
	id=#{id},chapter=#{chapter},index=#{index},set_id=#{setId}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(chapter)){
	 and chapter=#{chapter}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	-- @if(!isEmpty(setId)){
	 and set_id=#{setId}
	-- @}
	
	