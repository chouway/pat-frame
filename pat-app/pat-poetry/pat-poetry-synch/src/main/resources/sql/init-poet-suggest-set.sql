-- 初始化作品集推荐词  同步 set表
with temp as(
	select a.id,a.name_cn,b.rel_id from poet_set a left join poet_suggest b on b.key_type = '0a' and a.id = b.rel_id order by "index" asc
) insert into poet_suggest (keyword,key_type,rel_id,"count",update_ts,es_status,version) select temp.name_cn as "keyword",'0a' as "key_type",temp.id as "rel_id",0 as "count",now() as "update_ts",'0' as "es_status",0 as version from temp where temp.rel_id is null;