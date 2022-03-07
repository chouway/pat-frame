
sample
===
* 注释

	select #{use("cols")} from poet_section  where  #{use("condition")}

cols
===
	id,section,index,chapter_id

updateSample
===
	
	id=#{id},section=#{section},index=#{index},chapter_id=#{chapterId}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(section)){
	 and section=#{section}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	-- @if(!isEmpty(chapterId)){
	 and chapter_id=#{chapterId}
	-- @}
	
	