数据库持续集成 

官网
https://www.liquibase.org/

Liquibase Online Help
https://docs.liquibase.com/home.html?__hstc=128893969.68f1d742898f82d167099a3a2a1630c0.1615949120472.1615949120472.1615949120472.1&__hssc=128893969.2.1615949120473&__hsfp=2344792989&_ga=2.175519915.841453807.1615949082-678313262.1615949082

---
添加个maven 执行操作

Parameter
Working directory: D:/amuse/workplace/chouway/chouway/aibk/aibk-schema
command line: liquibase:update -f pom.xml
profile: test central

General
Before lauch
添加
Build
（目标每次执行时确保执行目录中的配置是最新的，否则执行时还是旧的配置。）
---

使用中文说明
https://segmentfault.com/a/1190000023114216?utm_source=tag-newest
Liquibase 数据库版本管理工具  


liquibase:rollback -Dliquibase.rollbackTag=v0.0.0 -f pom.xml
liquibase:rollback -Dliquibase.rollbackCount=2 -f pom.xml
liquibase:rollback -Dliquibase.rollbackDate="202103-17 11:24:53" -f pom.xml