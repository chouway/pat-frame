
sample
===
* 注释

	select #{use("cols")} from poet_info  where  #{use("condition")}

cols
===
	id,title,subtitle,remark,index,author_id,chapter_id,section_id,ext_id,set_id,update_ts,version,es_status

updateSample
===
	
	id=#{id},title=#{title},subtitle=#{subtitle},remark=#{remark},index=#{index},author_id=#{authorId},chapter_id=#{chapterId},section_id=#{sectionId},ext_id=#{extId},set_id=#{setId},update_ts=#{updateTs},version=#{version},es_status=#{esStatus}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(title)){
	 and title=#{title}
	-- @}
	-- @if(!isEmpty(subtitle)){
	 and subtitle=#{subtitle}
	-- @}
	-- @if(!isEmpty(remark)){
	 and remark=#{remark}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	-- @if(!isEmpty(authorId)){
	 and author_id=#{authorId}
	-- @}
	-- @if(!isEmpty(chapterId)){
	 and chapter_id=#{chapterId}
	-- @}
	-- @if(!isEmpty(sectionId)){
	 and section_id=#{sectionId}
	-- @}
	-- @if(!isEmpty(extId)){
	 and ext_id=#{extId}
	-- @}
	-- @if(!isEmpty(setId)){
	 and set_id=#{setId}
	-- @}
	-- @if(!isEmpty(updateTs)){
	 and update_ts=#{updateTs}
	-- @}
	-- @if(!isEmpty(version)){
	 and version=#{version}
	-- @}
	-- @if(!isEmpty(esStatus)){
	 and es_status=#{esStatus}
	-- @}
	
getTitleAndAuthorById
===
select a.title,b.name as "author" from poet_info a inner join poet_author b on a.author_id = b.id
where a.id = #{infoId}	


getPoetInfoBO
===
select a.id, a.title, a.subtitle, b.name as "author", c.baike_url, c.baike_title, c.baike_desc from poet_info a 
inner join poet_author b on a.author_id = b.id
left join poet_baike c on rel_type = '00' and a.id = c.rel_id 