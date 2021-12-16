/*
 Navicat Premium Data Transfer

 Source Server         : 121.36.251.64 hw chouway
 Source Server Type    : PostgreSQL
 Source Server Version : 130002
 Source Host           : 121.36.251.64:5432
 Source Catalog        : pat
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130002
 File Encoding         : 65001

 Date: 12/05/2021 16:53:37
*/


-- ----------------------------
-- Table structure for pat_error
-- ----------------------------
DROP TABLE IF EXISTS "public"."pat_error";
CREATE TABLE "public"."pat_error" (
  "id" int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
),
  "project" varchar(64) COLLATE "pg_catalog"."default",
  "service" varchar(64) COLLATE "pg_catalog"."default",
  "method" varchar(64) COLLATE "pg_catalog"."default",
  "param_clazz" varchar(255) COLLATE "pg_catalog"."default",
  "param_json" text COLLATE "pg_catalog"."default",
  "exception" varchar(64) COLLATE "pg_catalog"."default",
  "error_code" varchar(64) COLLATE "pg_catalog"."default",
  "message" text COLLATE "pg_catalog"."default",
  "stack_trace" text COLLATE "pg_catalog"."default",
  "log_id" varchar(32) COLLATE "pg_catalog"."default",
  "ip" varchar(32) COLLATE "pg_catalog"."default",
  "status" char(1) COLLATE "pg_catalog"."default",
  "handle_result_json" text COLLATE "pg_catalog"."default",
  "handle_error_id" int8,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_ts" timestamp(6),
  "update_ts" timestamp(6),
  CONSTRAINT "pat_error_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."pat_error"
  OWNER TO "postgres";

COMMENT ON COLUMN "public"."pat_error"."project" IS '项目';

COMMENT ON COLUMN "public"."pat_error"."service" IS '服务';

COMMENT ON COLUMN "public"."pat_error"."method" IS '方法';

COMMENT ON COLUMN "public"."pat_error"."param_clazz" IS '参数类信息';

COMMENT ON COLUMN "public"."pat_error"."param_json" IS '参数信息';

COMMENT ON COLUMN "public"."pat_error"."exception" IS '异常类信息';

COMMENT ON COLUMN "public"."pat_error"."error_code" IS '错误码';

COMMENT ON COLUMN "public"."pat_error"."message" IS '错误信息';

COMMENT ON COLUMN "public"."pat_error"."stack_trace" IS '错误堆栈信息';

COMMENT ON COLUMN "public"."pat_error"."log_id" IS '日志uid';

COMMENT ON COLUMN "public"."pat_error"."ip" IS '本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个';

COMMENT ON COLUMN "public"."pat_error"."status" IS '状态： 初始化 0 ,  成功处理 1 , 失败处理 2';

COMMENT ON COLUMN "public"."pat_error"."handle_result_json" IS '处理成功的结果数据';

COMMENT ON COLUMN "public"."pat_error"."handle_error_id" IS '处理失败的errorId';


COMMENT ON COLUMN "public"."pat_error"."remark" IS '备注';

COMMENT ON COLUMN "public"."pat_error"."create_ts" IS '创建时间';

COMMENT ON COLUMN "public"."pat_error"."update_ts" IS '更新时间';

COMMENT ON TABLE "public"."pat_error" IS '异常信息';