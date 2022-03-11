-- 初始化诗句 标题推荐词  同步 author表
with temp as(
	select a.id,a.title,b.rel_id,c.name "author" from poet_info a left join poet_suggest b on b.key_type = '02' and a.id = b.rel_id inner join poet_author c on a.author_id = c.id
)insert into poet_suggest (keyword,key_type,rel_id,"count",update_ts,es_status,version) select temp.author || ' 《' || temp.title || '》' as "keyword",'02' as "key_type",temp.id as "rel_id",0 as "count",now() as "update_ts",'0' as "es_status",0 as version from temp where temp.rel_id is null;

