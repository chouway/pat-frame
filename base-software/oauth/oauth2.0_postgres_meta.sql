/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.40.254 ES docker
 Source Server Type    : PostgreSQL
 Source Server Version : 140002
 Source Host           : 192.168.40.254:15432
 Source Catalog        : poetry
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 140002
 File Encoding         : 65001

 Date: 31/03/2022 17:19:45
*/


-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS "oauth_access_token";
CREATE TABLE "oauth_access_token" (
  "token_id" varchar(256) COLLATE "pg_catalog"."default",
  "token" bytea,
  "authentication_id" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(256) COLLATE "pg_catalog"."default",
  "client_id" varchar(256) COLLATE "pg_catalog"."default",
  "authentication" bytea,
  "refresh_token" varchar(256) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of oauth_access_token
-- ----------------------------
BEGIN;
INSERT INTO "oauth_access_token" VALUES ('4f6e97c10ddc63c6b7718acce54afcf0', E'\\254\\355\\000\\005sr\\000Corg.springframework.security.oauth2.common.DefaultOAuth2AccessToken\\014\\262\\2366\\033$\\372\\316\\002\\000\\006L\\000\\025additionalInformationt\\000\\017Ljava/util/Map;L\\000\\012expirationt\\000\\020Ljava/util/Date;L\\000\\014refreshTokent\\000?Lorg/springframework/security/oauth2/common/OAuth2RefreshToken;L\\000\\005scopet\\000\\017Ljava/util/Set;L\\000\\011tokenTypet\\000\\022Ljava/lang/String;L\\000\\005valueq\\000~\\000\\005xpsr\\000\\027java.util.LinkedHashMap4\\300N\\\\\\020l\\300\\373\\002\\000\\001Z\\000\\013accessOrderxr\\000\\021java.util.HashMap\\005\\007\\332\\301\\303\\026`\\321\\003\\000\\002F\\000\\012loadFactorI\\000\\011thresholdxp?@\\000\\000\\000\\000\\000\\014w\\010\\000\\000\\000\\020\\000\\000\\000\\002t\\000\\007licenset\\000\\003patt\\000\\010usernamet\\000\\004testx\\000sr\\000\\016java.util.Datehj\\201\\001KYt\\031\\003\\000\\000xpw\\010\\000\\000\\001\\177\\341\\276\\021\\321xsr\\000Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\\337Gc\\235\\320\\311\\267\\002\\000\\001L\\000\\012expirationq\\000~\\000\\002xr\\000Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\\341\\016\\012cT\\324^\\002\\000\\001L\\000\\005valueq\\000~\\000\\005xpt\\002\\224eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aC1zZXJ2ZXIiXSwidXNlcl9uYW1lIjoidGVzdCIsInNjb3BlIjpbInNlcnZlciJdLCJhdGkiOiI4NzJmZWZjOC1iNjZiLTRlYTQtODIzZi1hNDRkYzc4NWIxNWQiLCJleHAiOjE2NTEzMDg2MDQsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiMjA1MjZlZWYtZDgxOS00YjQ3LWI1MGUtZDJjYjNmMTQyYmU5IiwiY2xpZW50X2lkIjoidGVzdCJ9.TjMhBZwGseBxqOsulPTa9NEEW1f_lZUtB9_WonXK1nmZtTDx8es4vvGtX5IkvvLo0IN9OsIryK0z3Gbm52GYurJckfFuKHv3CwuHo5G2nvbXG0UG2RIbWncaa0XLCYIo8auyAhBVhYtDRTR3NFv6y-OK8fgFPBNf5WiridxKqWFQLkHZM2aLdgWmfpCYwI6sHhL4x88rxK5Aokkq9LTc055WezZMLurSRbasr8XJV5OxNXLagnCWcYJLYKalUqSrJFr8WbzINEMF5QZQIOtqTql1D3xPA8WnFGvlNU4YWU9aQ7joOSaah3bvrNeD3ww8SbtMg0G8QW636PpPT41Fpgsq\\000~\\000\\016w\\010\\000\\000\\001\\200y\\251\\253\\316xsr\\000%java.util.Collections$UnmodifiableSet\\200\\035\\222\\321\\217\\233\\200U\\002\\000\\000xr\\000,java.util.Collections$UnmodifiableCollection\\031B\\000\\200\\313^\\367\\036\\002\\000\\001L\\000\\001ct\\000\\026Ljava/util/Collection;xpsr\\000\\027java.util.LinkedHashSet\\330l\\327Z\\225\\335*\\036\\002\\000\\000xr\\000\\021java.util.HashSet\\272D\\205\\225\\226\\270\\2674\\003\\000\\000xpw\\014\\000\\000\\000\\002?@\\000\\000\\000\\000\\000\\001t\\000\\006serverxt\\000\\006bearert\\002XeyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aC1zZXJ2ZXIiXSwidXNlcl9uYW1lIjoidGVzdCIsInNjb3BlIjpbInNlcnZlciJdLCJleHAiOjE2NDg3NTk4MDQsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiODcyZmVmYzgtYjY2Yi00ZWE0LTgyM2YtYTQ0ZGM3ODViMTVkIiwiY2xpZW50X2lkIjoidGVzdCJ9.ArR_T-1uaz9WC4nouFbH968MQOuoIL2Ch_bxZP_QnAkch8TTTXnNUtPh5VIqLUCwAjrgUO3eF3I0QUkhN2rw5XhBRIMP4-Bn8mMRX7aPipKpm_L_cBXSk-a51WMEM8pWs_kwfa2i7F2hePeClNAuH6HaQlqpj3raGeD4fna6U-ZgFvVixei2wEKDoNzIv7-QCeSZmEuO9S_f8ILrsiaUBf-PFAS1zzNPmPtlzPT2A89hHeP1UT1w_9bGhHPofMPtVPhPi0m-1jExtgNE7uFMP1YfyPtwfr9e3p4GlXE3fCAIq3DlBk6r-vesHZQXn4c9XEgN2zy3XXNXXjKkg_hEZA', 'e64de0806e22c7d4d838be49cde3adda', 'test', 'test', E'\\254\\355\\000\\005sr\\000Aorg.springframework.security.oauth2.provider.OAuth2Authentication\\275@\\013\\002\\026bR\\023\\002\\000\\002L\\000\\015storedRequestt\\000<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\\000\\022userAuthenticationt\\0002Lorg/springframework/security/core/Authentication;xr\\000Gorg.springframework.security.authentication.AbstractAuthenticationToken\\323\\252(~nGd\\016\\002\\000\\003Z\\000\\015authenticatedL\\000\\013authoritiest\\000\\026Ljava/util/Collection;L\\000\\007detailst\\000\\022Ljava/lang/Object;xp\\000sr\\000&java.util.Collections$UnmodifiableList\\374\\017%1\\265\\354\\216\\020\\002\\000\\001L\\000\\004listt\\000\\020Ljava/util/List;xr\\000,java.util.Collections$UnmodifiableCollection\\031B\\000\\200\\313^\\367\\036\\002\\000\\001L\\000\\001cq\\000~\\000\\004xpsr\\000\\023java.util.ArrayListx\\201\\322\\035\\231\\307a\\235\\003\\000\\001I\\000\\004sizexp\\000\\000\\000\\001w\\004\\000\\000\\000\\001sr\\000Borg.springframework.security.core.authority.SimpleGrantedAuthority\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\001L\\000\\004rolet\\000\\022Ljava/lang/String;xpt\\000\\004USERxq\\000~\\000\\014psr\\000:org.springframework.security.oauth2.provider.OAuth2Request\\000\\000\\000\\000\\000\\000\\000\\001\\002\\000\\007Z\\000\\010approvedL\\000\\013authoritiesq\\000~\\000\\004L\\000\\012extensionst\\000\\017Ljava/util/Map;L\\000\\013redirectUriq\\000~\\000\\016L\\000\\007refresht\\000;Lorg/springframework/security/oauth2/provider/TokenRequest;L\\000\\013resourceIdst\\000\\017Ljava/util/Set;L\\000\\015responseTypesq\\000~\\000\\024xr\\0008org.springframework.security.oauth2.provider.BaseRequest6(z>\\243qi\\275\\002\\000\\003L\\000\\010clientIdq\\000~\\000\\016L\\000\\021requestParametersq\\000~\\000\\022L\\000\\005scopeq\\000~\\000\\024xpt\\000\\004testsr\\000%java.util.Collections$UnmodifiableMap\\361\\245\\250\\376t\\365\\007B\\002\\000\\001L\\000\\001mq\\000~\\000\\022xpsr\\000\\021java.util.HashMap\\005\\007\\332\\301\\303\\026`\\321\\003\\000\\002F\\000\\012loadFactorI\\000\\011thresholdxp?@\\000\\000\\000\\000\\000\\006w\\010\\000\\000\\000\\010\\000\\000\\000\\004t\\000\\012grant_typet\\000\\010passwordt\\000\\011client_idt\\000\\004testt\\000\\005scopet\\000\\006servert\\000\\010usernamet\\000\\004testxsr\\000%java.util.Collections$UnmodifiableSet\\200\\035\\222\\321\\217\\233\\200U\\002\\000\\000xq\\000~\\000\\011sr\\000\\027java.util.LinkedHashSet\\330l\\327Z\\225\\335*\\036\\002\\000\\000xr\\000\\021java.util.HashSet\\272D\\205\\225\\226\\270\\2674\\003\\000\\000xpw\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\001q\\000~\\000!x\\001sq\\000~\\000''w\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\000xsq\\000~\\000\\032?@\\000\\000\\000\\000\\000\\000w\\010\\000\\000\\000\\020\\000\\000\\000\\000xppsq\\000~\\000''w\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\001t\\000\\013auth-serverxsq\\000~\\000''w\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\000xsr\\000Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\002L\\000\\013credentialsq\\000~\\000\\005L\\000\\011principalq\\000~\\000\\005xq\\000~\\000\\003\\001sq\\000~\\000\\007sq\\000~\\000\\013\\000\\000\\000\\001w\\004\\000\\000\\000\\001q\\000~\\000\\017xq\\000~\\0001sr\\000\\027java.util.LinkedHashMap4\\300N\\\\\\020l\\300\\373\\002\\000\\001Z\\000\\013accessOrderxq\\000~\\000\\032?@\\000\\000\\000\\000\\000\\014w\\010\\000\\000\\000\\020\\000\\000\\000\\005q\\000~\\000\\034q\\000~\\000\\035q\\000~\\000 q\\000~\\000!t\\000\\015client_secrett\\000\\004testq\\000~\\000\\036q\\000~\\000\\037q\\000~\\000"q\\000~\\000#x\\000psr\\0002org.springframework.security.core.userdetails.User\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\007Z\\000\\021accountNonExpiredZ\\000\\020accountNonLockedZ\\000\\025credentialsNonExpiredZ\\000\\007enabledL\\000\\013authoritiesq\\000~\\000\\024L\\000\\010passwordq\\000~\\000\\016L\\000\\010usernameq\\000~\\000\\016xp\\001\\001\\001\\001sq\\000~\\000$sr\\000\\021java.util.TreeSet\\335\\230P\\223\\225\\355\\207[\\003\\000\\000xpsr\\000Forg.springframework.security.core.userdetails.User$AuthorityComparator\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\000xpw\\004\\000\\000\\000\\001q\\000~\\000\\017xpt\\000\\004test', 'd378f30394d2309877a7835a1b6c1948');
COMMIT;

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS "oauth_approvals";
CREATE TABLE "oauth_approvals" (
  "userid" varchar(256) COLLATE "pg_catalog"."default",
  "clientid" varchar(256) COLLATE "pg_catalog"."default",
  "scope" varchar(256) COLLATE "pg_catalog"."default",
  "status" varchar(10) COLLATE "pg_catalog"."default",
  "expiresat" timestamp(6),
  "lastmodifiedat" timestamp(6)
)
;

-- ----------------------------
-- Records of oauth_approvals
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS "oauth_client_details";
CREATE TABLE "oauth_client_details" (
  "client_id" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "resource_ids" varchar(256) COLLATE "pg_catalog"."default",
  "client_secret" varchar(256) COLLATE "pg_catalog"."default",
  "scope" varchar(256) COLLATE "pg_catalog"."default",
  "authorized_grant_types" varchar(256) COLLATE "pg_catalog"."default",
  "web_server_redirect_uri" varchar(256) COLLATE "pg_catalog"."default",
  "authorities" varchar(256) COLLATE "pg_catalog"."default",
  "access_token_validity" int4,
  "refresh_token_validity" int4,
  "additional_information" varchar(4096) COLLATE "pg_catalog"."default",
  "autoapprove" varchar(256) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO "oauth_client_details" VALUES ('test', 'auth-server', '$2a$10$OwWprgFmrdgbZvSLJMGV7.hz9yztheFL0XUJhokG.KY6jkCzFXULC', 'server', 'password,refresh_token', 'http://www.baidu.com', NULL, 43200, 2592000, null, '1');
COMMIT;

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS "oauth_client_token";
CREATE TABLE "oauth_client_token" (
  "token_id" varchar(256) COLLATE "pg_catalog"."default",
  "token" bytea,
  "authentication_id" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(256) COLLATE "pg_catalog"."default",
  "client_id" varchar(256) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of oauth_client_token
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS "oauth_code";
CREATE TABLE "oauth_code" (
  "code" varchar(256) COLLATE "pg_catalog"."default",
  "authentication" bytea
)
;

-- ----------------------------
-- Records of oauth_code
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS "oauth_refresh_token";
CREATE TABLE "oauth_refresh_token" (
  "token_id" varchar(256) COLLATE "pg_catalog"."default",
  "token" bytea,
  "authentication" bytea
)
;

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
BEGIN;
INSERT INTO "oauth_refresh_token" VALUES ('d378f30394d2309877a7835a1b6c1948', E'\\254\\355\\000\\005sr\\000Lorg.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken/\\337Gc\\235\\320\\311\\267\\002\\000\\001L\\000\\012expirationt\\000\\020Ljava/util/Date;xr\\000Dorg.springframework.security.oauth2.common.DefaultOAuth2RefreshTokens\\341\\016\\012cT\\324^\\002\\000\\001L\\000\\005valuet\\000\\022Ljava/lang/String;xpt\\002\\224eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiYXV0aC1zZXJ2ZXIiXSwidXNlcl9uYW1lIjoidGVzdCIsInNjb3BlIjpbInNlcnZlciJdLCJhdGkiOiI4NzJmZWZjOC1iNjZiLTRlYTQtODIzZi1hNDRkYzc4NWIxNWQiLCJleHAiOjE2NTEzMDg2MDQsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiMjA1MjZlZWYtZDgxOS00YjQ3LWI1MGUtZDJjYjNmMTQyYmU5IiwiY2xpZW50X2lkIjoidGVzdCJ9.TjMhBZwGseBxqOsulPTa9NEEW1f_lZUtB9_WonXK1nmZtTDx8es4vvGtX5IkvvLo0IN9OsIryK0z3Gbm52GYurJckfFuKHv3CwuHo5G2nvbXG0UG2RIbWncaa0XLCYIo8auyAhBVhYtDRTR3NFv6y-OK8fgFPBNf5WiridxKqWFQLkHZM2aLdgWmfpCYwI6sHhL4x88rxK5Aokkq9LTc055WezZMLurSRbasr8XJV5OxNXLagnCWcYJLYKalUqSrJFr8WbzINEMF5QZQIOtqTql1D3xPA8WnFGvlNU4YWU9aQ7joOSaah3bvrNeD3ww8SbtMg0G8QW636PpPT41Fpgsr\\000\\016java.util.Datehj\\201\\001KYt\\031\\003\\000\\000xpw\\010\\000\\000\\001\\200y\\251\\253\\316x', E'\\254\\355\\000\\005sr\\000Aorg.springframework.security.oauth2.provider.OAuth2Authentication\\275@\\013\\002\\026bR\\023\\002\\000\\002L\\000\\015storedRequestt\\000<Lorg/springframework/security/oauth2/provider/OAuth2Request;L\\000\\022userAuthenticationt\\0002Lorg/springframework/security/core/Authentication;xr\\000Gorg.springframework.security.authentication.AbstractAuthenticationToken\\323\\252(~nGd\\016\\002\\000\\003Z\\000\\015authenticatedL\\000\\013authoritiest\\000\\026Ljava/util/Collection;L\\000\\007detailst\\000\\022Ljava/lang/Object;xp\\000sr\\000&java.util.Collections$UnmodifiableList\\374\\017%1\\265\\354\\216\\020\\002\\000\\001L\\000\\004listt\\000\\020Ljava/util/List;xr\\000,java.util.Collections$UnmodifiableCollection\\031B\\000\\200\\313^\\367\\036\\002\\000\\001L\\000\\001cq\\000~\\000\\004xpsr\\000\\023java.util.ArrayListx\\201\\322\\035\\231\\307a\\235\\003\\000\\001I\\000\\004sizexp\\000\\000\\000\\001w\\004\\000\\000\\000\\001sr\\000Borg.springframework.security.core.authority.SimpleGrantedAuthority\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\001L\\000\\004rolet\\000\\022Ljava/lang/String;xpt\\000\\004USERxq\\000~\\000\\014psr\\000:org.springframework.security.oauth2.provider.OAuth2Request\\000\\000\\000\\000\\000\\000\\000\\001\\002\\000\\007Z\\000\\010approvedL\\000\\013authoritiesq\\000~\\000\\004L\\000\\012extensionst\\000\\017Ljava/util/Map;L\\000\\013redirectUriq\\000~\\000\\016L\\000\\007refresht\\000;Lorg/springframework/security/oauth2/provider/TokenRequest;L\\000\\013resourceIdst\\000\\017Ljava/util/Set;L\\000\\015responseTypesq\\000~\\000\\024xr\\0008org.springframework.security.oauth2.provider.BaseRequest6(z>\\243qi\\275\\002\\000\\003L\\000\\010clientIdq\\000~\\000\\016L\\000\\021requestParametersq\\000~\\000\\022L\\000\\005scopeq\\000~\\000\\024xpt\\000\\004testsr\\000%java.util.Collections$UnmodifiableMap\\361\\245\\250\\376t\\365\\007B\\002\\000\\001L\\000\\001mq\\000~\\000\\022xpsr\\000\\021java.util.HashMap\\005\\007\\332\\301\\303\\026`\\321\\003\\000\\002F\\000\\012loadFactorI\\000\\011thresholdxp?@\\000\\000\\000\\000\\000\\006w\\010\\000\\000\\000\\010\\000\\000\\000\\004t\\000\\012grant_typet\\000\\010passwordt\\000\\011client_idt\\000\\004testt\\000\\005scopet\\000\\006servert\\000\\010usernamet\\000\\004testxsr\\000%java.util.Collections$UnmodifiableSet\\200\\035\\222\\321\\217\\233\\200U\\002\\000\\000xq\\000~\\000\\011sr\\000\\027java.util.LinkedHashSet\\330l\\327Z\\225\\335*\\036\\002\\000\\000xr\\000\\021java.util.HashSet\\272D\\205\\225\\226\\270\\2674\\003\\000\\000xpw\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\001q\\000~\\000!x\\001sq\\000~\\000''w\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\000xsq\\000~\\000\\032?@\\000\\000\\000\\000\\000\\000w\\010\\000\\000\\000\\020\\000\\000\\000\\000xppsq\\000~\\000''w\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\001t\\000\\013auth-serverxsq\\000~\\000''w\\014\\000\\000\\000\\020?@\\000\\000\\000\\000\\000\\000xsr\\000Oorg.springframework.security.authentication.UsernamePasswordAuthenticationToken\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\002L\\000\\013credentialsq\\000~\\000\\005L\\000\\011principalq\\000~\\000\\005xq\\000~\\000\\003\\001sq\\000~\\000\\007sq\\000~\\000\\013\\000\\000\\000\\001w\\004\\000\\000\\000\\001q\\000~\\000\\017xq\\000~\\0001sr\\000\\027java.util.LinkedHashMap4\\300N\\\\\\020l\\300\\373\\002\\000\\001Z\\000\\013accessOrderxq\\000~\\000\\032?@\\000\\000\\000\\000\\000\\014w\\010\\000\\000\\000\\020\\000\\000\\000\\005q\\000~\\000\\034q\\000~\\000\\035q\\000~\\000 q\\000~\\000!t\\000\\015client_secrett\\000\\004testq\\000~\\000\\036q\\000~\\000\\037q\\000~\\000"q\\000~\\000#x\\000psr\\0002org.springframework.security.core.userdetails.User\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\007Z\\000\\021accountNonExpiredZ\\000\\020accountNonLockedZ\\000\\025credentialsNonExpiredZ\\000\\007enabledL\\000\\013authoritiesq\\000~\\000\\024L\\000\\010passwordq\\000~\\000\\016L\\000\\010usernameq\\000~\\000\\016xp\\001\\001\\001\\001sq\\000~\\000$sr\\000\\021java.util.TreeSet\\335\\230P\\223\\225\\355\\207[\\003\\000\\000xpsr\\000Forg.springframework.security.core.userdetails.User$AuthorityComparator\\000\\000\\000\\000\\000\\000\\002\\022\\002\\000\\000xpw\\004\\000\\000\\000\\001q\\000~\\000\\017xpt\\000\\004test');
COMMIT;

-- ----------------------------
-- Primary Key structure for table oauth_access_token
-- ----------------------------
ALTER TABLE "oauth_access_token" ADD CONSTRAINT "oauth_access_token_pkey" PRIMARY KEY ("authentication_id");

-- ----------------------------
-- Primary Key structure for table oauth_client_details
-- ----------------------------
ALTER TABLE "oauth_client_details" ADD CONSTRAINT "oauth_client_details_pkey" PRIMARY KEY ("client_id");

-- ----------------------------
-- Primary Key structure for table oauth_client_token
-- ----------------------------
ALTER TABLE "oauth_client_token" ADD CONSTRAINT "oauth_client_token_pkey" PRIMARY KEY ("authentication_id");
