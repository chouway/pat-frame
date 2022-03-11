-- 初始化作品推荐词  同步 info表
with temp as(
	select a.id,a.title,b.rel_id from poet_info a left join poet_suggest b on b.key_type = '01' and a.id = b.rel_id
) insert into poet_suggest (keyword,key_type,rel_id,"count",update_ts,es_status,version) select temp.title as "keyword",'01' as "key_type",temp.id as "rel_id",0 as "count",now() as "update_ts",'0' as "es_status",0 as version from temp where temp.rel_id is null;