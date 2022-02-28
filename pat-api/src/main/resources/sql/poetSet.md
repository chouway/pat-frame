
sample
===
* 注释

	select #{use("cols")} from poet_set  where  #{use("condition")}

cols
===
	id,name_cn,name_en,set_type,desc,remark,update_ts,version,infos

updateSample
===
	
	id=#{id},name_cn=#{nameCn},name_en=#{nameEn},set_type=#{setType},desc=#{desc},remark=#{remark},update_ts=#{updateTs},version=#{version},infos=#{infos}

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
	-- @if(!isEmpty(desc)){
	 and desc=#{desc}
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
	
	