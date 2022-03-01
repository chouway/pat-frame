
sample
===
* 注释

	select #{use("cols")} from poet_content  where  #{use("condition")}

cols
===
	id,paragraph,index,info_id

updateSample
===
	
	id=#{id},paragraph=#{paragraph},index=#{index},info_id=#{infoId}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(paragraph)){
	 and paragraph=#{paragraph}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	-- @if(!isEmpty(infoId)){
	 and info_id=#{infoId}
	-- @}
	
getContent
===
    select string_agg(temp.paragraph,'') from 
    (
    SELECT paragraph FROM "poet_content" where info_id = #{infoId} order by index asc
    ) temp	