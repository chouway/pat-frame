
sample
===
* 注释

	select #{use("cols")} from poet_baike  where  #{use("condition")}

cols
===
	id,baike_type,rel_type,rel_id,baike_url,baike_title,baike_desc,baike_search_title,baike_search_key,status,similar

updateSample
===
	
	id=#{id},baike_type=#{baikeType},rel_type=#{relType},rel_id=#{relId},baike_url=#{baikeUrl},baike_title=#{baikeTitle},baike_desc=#{baikeDesc},baike_search_title=#{baikeSearchTitle},baike_search_key=#{baikeSearchKey},status=#{status},similar=#{similar}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(baikeType)){
	 and baike_type=#{baikeType}
	-- @}
	-- @if(!isEmpty(relType)){
	 and rel_type=#{relType}
	-- @}
	-- @if(!isEmpty(relId)){
	 and rel_id=#{relId}
	-- @}
	-- @if(!isEmpty(baikeUrl)){
	 and baike_url=#{baikeUrl}
	-- @}
	-- @if(!isEmpty(baikeTitle)){
	 and baike_title=#{baikeTitle}
	-- @}
	-- @if(!isEmpty(baikeDesc)){
	 and baike_desc=#{baikeDesc}
	-- @}
	-- @if(!isEmpty(baikeSearchTitle)){
	 and baike_search_title=#{baikeSearchTitle}
	-- @}
	-- @if(!isEmpty(baikeSearchKey)){
	 and baike_search_key=#{baikeSearchKey}
	-- @}
	-- @if(!isEmpty(status)){
	 and status=#{status}
	-- @}
	-- @if(!isEmpty(similar)){
	 and similar=#{similar}
	-- @}
	