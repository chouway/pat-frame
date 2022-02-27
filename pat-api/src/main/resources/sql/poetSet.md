
sample
===
* 注释

	select #{use("cols")} from poet_set  where  #{use("condition")}

cols
===
	id,name_cn,name_en,desc,remark,set_type,info_num

updateSample
===
	
	id=#{id},name_cn=#{nameCn},name_en=#{nameEn},desc=#{desc},remark=#{remark},set_type=#{setType},info_num=#{infoNum}

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
	-- @if(!isEmpty(desc)){
	 and desc=#{desc}
	-- @}
	-- @if(!isEmpty(remark)){
	 and remark=#{remark}
	-- @}
	-- @if(!isEmpty(setType)){
	 and set_type=#{setType}
	-- @}
	-- @if(!isEmpty(infoNum)){
	 and info_num=#{infoNum}
	-- @}
	
	