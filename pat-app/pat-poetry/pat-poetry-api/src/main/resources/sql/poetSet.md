
sample
===
* 注释

	select #{use("cols")} from poet_set  where  #{use("condition")}

cols
===
	id,name_cn,name_en,set_type,remark,update_ts,version,infos,describe,index

updateSample
===
	
	id=#{id},name_cn=#{nameCn},name_en=#{nameEn},set_type=#{setType},remark=#{remark},update_ts=#{updateTs},version=#{version},infos=#{infos},describe=#{describe},index=#{index}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(nameCn)){
	 and name_cn=#{nameCn}
	-- @}
	-- @if(!isEmpty(nameEn)){
	 and name_en=#{nameEn}
	-- @}
	-- @if(!isEmpty(setType)){
	 and set_type=#{setType}
	-- @}
	-- @if(!isEmpty(remark)){
	 and remark=#{remark}
	-- @}
	-- @if(!isEmpty(updateTs)){
	 and update_ts=#{updateTs}
	-- @}
	-- @if(!isEmpty(version)){
	 and version=#{version}
	-- @}
	-- @if(!isEmpty(infos)){
	 and infos=#{infos}
	-- @}
	-- @if(!isEmpty(describe)){
	 and describe=#{describe}
	-- @}
	-- @if(!isEmpty(index)){
	 and index=#{index}
	-- @}
	
	