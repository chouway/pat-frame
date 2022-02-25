
sample
===
* 注释

	select #{use("cols")} from poet_set  where  #{use("condition")}

cols
===
	id,name_cn,name_en,desc,remark,infoNum

updateSample
===
	
	id=#{id},name_cn=#{nameCn},name_en=#{nameEn},desc=#{desc},remark=#{remark},infoNum=#{infonum}

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
	-- @if(!isEmpty(infonum)){
	 and infoNum=#{infonum}
	-- @}
	
	