delete from "public"."pat_config" where id in(1,2,3,4);
INSERT INTO "public"."pat_config"("id", "config_name", "config_code", "config_value", "remark", "seq_no", "parent_id") VALUES (1, '测试: 类型A', 'TEST_TYPE_A', 'a', NULL, 0, NULL);
INSERT INTO "public"."pat_config"("id", "config_name", "config_code", "config_value", "remark", "seq_no", "parent_id") VALUES (2, '测试：类型A_A', 'TEST_CODE_A_A', 'a_a', NULL, 0, 1);
INSERT INTO "public"."pat_config"("id", "config_name", "config_code", "config_value", "remark", "seq_no", "parent_id") VALUES (3, '测试：类型A_A', 'TEST_CODE_A_A', 'a_b', NULL, NULL, 1);
INSERT INTO "public"."pat_config"("id", "config_name", "config_code", "config_value", "remark", "seq_no", "parent_id") VALUES (4, '测试：类型A_A_A', 'TEST_CODE_A_A_A', 'a_a_a', NULL, 0, 2);


-- 自增主键和序列不一致导致的，所以也可以不用重置序列起始值和主键，采取把序列号改成和当前最大主键一致的方式：
select setval('pat_config_id_seq', max(id)) from "public"."pat_config";