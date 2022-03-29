
sample
===
* 注释

	select #{use("cols")} from pat_resource  where  #{use("condition")}

cols
===
	id,uri,uri_desc,index,role

updateSample
===
	
	id=#{id},uri=#{uri},uri_desc=#{uriDesc},index=#{index},role=#{role}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(uri)){
	 and uri=#{uri}
	-- @}
	-- @if(!isEmpty(uriDesc)){
	 and uri_desc=#{uriDesc}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	-- @if(!isEmpty(role)){
	 and role=#{role}
	-- @}
	
	