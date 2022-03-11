-- 重新更新
with temp as(
	select a.id,a.title,b.rel_id,c.name "author",b.id "target_id" from poet_info a inner join poet_suggest b on b.key_type = '02' and a.id = b.rel_id inner join poet_author c on a.author_id = c.id
) update poet_suggest as target set keyword = temp.author || ' 《' || temp.title || '》' from temp inner join poet_suggest target2 on temp.target_id = target2.id  where target.id = target2.id