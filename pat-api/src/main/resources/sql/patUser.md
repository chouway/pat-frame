
sample
===
* 注释

	select #{use("cols")} from pat_user  where  #{use("condition")}

cols
===
	id,nick_name,status,password,pwd_fail_count,login_ts,role,create_ts,update_ts,user_name,pwd_fail_day,email,phone

updateSample
===
	
	id=#{id},nick_name=#{nickName},status=#{status},password=#{password},pwd_fail_count=#{pwdFailCount},login_ts=#{loginTs},role=#{role},create_ts=#{createTs},update_ts=#{updateTs},user_name=#{userName},pwd_fail_day=#{pwdFailDay},email=#{email},phone=#{phone}

condition
===

	1 = 1  
	-- @if(!isEmpty(id)){
	 and id=#{id}
	-- @}
	-- @if(!isEmpty(nickName)){
	 and nick_name=#{nickName}
	-- @}
	-- @if(!isEmpty(status)){
	 and status=#{status}
	-- @}
	-- @if(!isEmpty(password)){
	 and password=#{password}
	-- @}
	-- @if(!isEmpty(pwdFailCount)){
	 and pwd_fail_count=#{pwdFailCount}
	-- @}
	-- @if(!isEmpty(loginTs)){
	 and login_ts=#{loginTs}
	-- @}
	-- @if(!isEmpty(role)){
	 and role=#{role}
	-- @}
	-- @if(!isEmpty(createTs)){
	 and create_ts=#{createTs}
	-- @}
	-- @if(!isEmpty(updateTs)){
	 and update_ts=#{updateTs}
	-- @}
	-- @if(!isEmpty(userName)){
	 and user_name=#{userName}
	-- @}
	-- @if(!isEmpty(pwdFailDay)){
	 and pwd_fail_day=#{pwdFailDay}
	-- @}
	-- @if(!isEmpty(email)){
	 and email=#{email}
	-- @}
	-- @if(!isEmpty(phone)){
	 and phone=#{phone}
	-- @}
	
	