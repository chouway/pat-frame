https://docs.docker.com/compose/env-file/
在文件中声明默认环境变量

Compose 支持在名为.env放置在项目目录中的环境文件中声明默认环境变量。如果 Docker Compose 版本早于1.28，.env则从执行命令的当前工作目录加载文件，如果使用--project-directory选项显式设置，则从项目目录加载文件。这种不一致已经+v1.28通过将默认.env文件路径限制为项目目录来解决。您可以使用--env-file命令行选项覆盖默认值 .env并指定自定义环境文件的路径。

项目目录按优先顺序指定：

--project-directory 旗帜
第一个--file标志的文件夹
当前目录
语法规则
以下语法规则适用于该.env文件：

Compose 期望env文件中的每一行都符合VAR=VAL格式。
以 开头的行#作为注释处理并被忽略。
空行被忽略。
没有对引号进行特殊处理。这意味着 它们是 VAL 的一部分。
编写文件和 CLI 变量
您在此处定义的环境变量用于 Compose 文件中的变量替换，也可用于定义以下 CLI 变量：

COMPOSE_API_VERSION
COMPOSE_CONVERT_WINDOWS_PATHS
COMPOSE_FILE
COMPOSE_HTTP_TIMEOUT
COMPOSE_PROFILES
COMPOSE_PROJECT_NAME
COMPOSE_TLS_VERSION
DOCKER_CERT_PATH
DOCKER_HOST
DOCKER_TLS_VERIFY
笔记

运行时环境中存在的值始终会覆盖.env文件中定义的值。同样，通过命令行参数传递的值也优先。
.env文件中定义的环境变量在容器内不会自动可见。要设置容器适用的环境变量，请遵循Compose中的环境变量主题中的指南，该主题 描述了如何将 shell 环境变量传递到容器、在 Compose 文件中定义环境变量等。