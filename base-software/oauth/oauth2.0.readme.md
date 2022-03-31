oauth2.0_postgres_meta.sql
为postgres 初始化表
如果 采用的是 Redis存储token等 则只需要初始化client_detail即可
推荐使用 redis ，毕竟数据库存储读取还是相对比较耗性能的

oauth2.0_hsql_meta.sql
https://github.com/spring-projects/spring-security-oauth/blob/main/spring-security-oauth2/src/test/resources/schema.sql
建表sql
官网的是HSQL

hsql 的 LONGVARBINARY  数据类型相当于 postgres里的 bytea 数据类型