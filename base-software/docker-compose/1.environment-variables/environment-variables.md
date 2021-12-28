https://docs.docker.com/compose/environment-variables/
Compose 中的环境变量


Compose 的多个部分在一种或另一种意义上处理环境变量。此页面可帮助您找到所需的信息。

替换 Compose 文件中的环境变量
可以在 shell 中使用环境变量来填充 Compose 文件中的值：

web:
  image: "webapp:${TAG}"
如果您有多个环境变量，则可以通过将它们添加到名为的默认环境变量文件.env或使用--env-file命令行选项提供环境变量文件的路径来替换它们。

您的配置选项可以包含环境变量。Compose 使用来自docker-compose运行的 shell 环境的变量值。例如，假设 shell 包含POSTGRES_VERSION=9.3并且您提供以下配置：

db:
  image: "postgres:${POSTGRES_VERSION}"
当您docker-compose up使用此配置运行时，ComposePOSTGRES_VERSION在 shell 中查找 环境变量并将其值替换为 in。对于此示例，Compose在运行配置之前解析imageto postgres:9.3。

如果未设置环境变量，Compose 将替换为空字符串。在上面的示例中，如果POSTGRES_VERSION未设置，则该image选项的值为postgres:。

您可以使用.env文件设置环境变量的默认值 ，Compose 会自动在项目目录（您的 Compose 文件的父文件夹）中查找该文件。在 shell 环境中设置的值会覆盖在.env文件中设置的值。

使用 docker stack deploy 时的注意事项

该.env file功能仅在您使用该docker-compose up命令时有效，而不适用于docker stack deploy.

这两个$VARIABLE和${VARIABLE}语法的支持。此外，当使用2.1 文件格式时，可以使用典型的 shell 语法提供内联默认值：

${VARIABLE:-default}评估default是否VARIABLE在环境中未设置或为空。
${VARIABLE-default}default仅当VARIABLE在环境中未设置时才评估为。
同样，以下语法允许您指定必需变量：

${VARIABLE:?err}退出并显示错误消息，其中包含环境中的errif VARIABLE未设置或为空。
${VARIABLE?err}退出并显示一条错误消息，其中包含errif VARIABLE在环境中未设置。
${VARIABLE/foo/bar}不支持其他扩展的 shell 样式功能，例如。

$$当您的配置需要文字美元符号时，您可以使用（双美元符号）。这也可以防止 Compose 插入值，因此 a$$ 允许您引用不想由 Compose 处理的环境变量。

web:
  build: .
  command: "$$VAR_NOT_INTERPOLATED_BY_COMPOSE"
如果您忘记并使用单个美元符号 ( $)，Compose 会将值解释为环境变量并警告您：

“.env”文件
您可以在撰写文件中引用，或用于配置撰写，在任何环境变量设置的默认值环境文件 命名.env。该.env文件路径如下：

以开头+v1.28，.env文件放置在项目目录的底部
可以使用--file选项或COMPOSE_FILE 环境变量显式定义项目目录。否则，它是docker compose执行命令的当前工作目录 ( +1.28)。
对于以前的版本，可能无法.env使用--file或解析文件 COMPOSE_FILE。要解决此问题，建议使用--project-directory，它会覆盖.env文件的路径。这种不一致已+v1.28通过将文件路径限制为项目目录来解决。
 cat .env
 cat docker-compose.yml
{TAG}"
当您运行时docker-compose up，web上面定义的服务使用该图像webapp:v1.5。您可以使用config 命令来验证这一点 ，它将您解析的应用程序配置打印到终端：

 docker-compose config
shell 中的值优先于.env文件中指定的值。

如果您TAG在 shell 中设置为不同的值，则替换中将image 使用该值：

 export TAG=v2.0
 docker-compose config
您可以使用命令行参数覆盖环境文件路径--env-file。

使用“--env-file”选项
通过将文件作为参数传递，您可以将其存储在任何位置并对其进行适当命名，例如，.env.ci, .env.dev, .env.prod。使用以下--env-file选项传递文件路径：

 docker-compose --env-file ./config/.env.dev up 
此文件路径相对于执行 Docker Compose 命令的当前工作目录。

 cat .env
 cat ./config/.env.dev
 cat docker-compose.yml
{TAG}"
该.env文件默认加载：

 docker-compose config 
传递--env-file 参数会覆盖默认文件路径：

 docker-compose --env-file ./config/.env.dev config 
当无效的文件路径作为--env-file参数传递时，Compose 会返回错误：

 docker-compose --env-file ./doesnotexist/.env.dev  config
有关更多信息，请参阅Compose 文件参考中的 变量替换部分。

在容器中设置环境变量
您可以使用'environment' 键在服务的容器中设置环境变量 ，就像使用 docker run -e VARIABLE=VALUE ...：

web:
  environment:
    - DEBUG=1
将环境变量传递给容器
您可以使用'environment' 键将环境变量从 shell 直接传递到服务的容器，方法是 不给它们一个值，就像使用docker run -e VARIABLE ...：

web:
  environment:
    - DEBUG
所述的值DEBUG在容器变量是从值取为在其中撰写运行在壳中的相同变量。

“env_file”配置选项
您可以使用'env_file' 选项将多个环境变量从外部文件传递到服务的容器，就像使用docker run --env-file=FILE ...：

web:
  env_file:
    - web-variables.env
使用 '  -compose run' 设置环境变量
与 类似docker run -e，您可以使用以下命令在一次性容器上设置环境变量docker-compose run -e：

 docker-compose run -e DEBUG=1 web python console.py
您还可以通过不给它一个值来从 shell 传递一个变量：

 docker-compose run -e DEBUG web python console.py
所述的值DEBUG在容器变量是从值取为在其中撰写运行在壳中的相同变量。

当您在多个文件中设置相同的环境变量时，Compose 使用以下优先级来选择要使用的值：

撰写文件
外壳环境变量
环境文件
文件
变量未定义
在下面的示例中，我们在环境文件和 Compose 文件上设置了相同的环境变量：

 cat ./Docker/api/api.env
 cat docker-compose.yml
运行容器时，Compose 文件中定义的环境变量优先。

 docker-compose exec api node
 process.env.NODE_ENV
有任何ARG或ENV在设定Dockerfile仅当有将评估没有多克撰写的条目environment或env_file。

NodeJS 容器的细节

如果您有like package.json条目，那么这会否决您文件中的任何设置 。script:startNODE_ENV=test node server.jsdocker-compose.yml

使用环境变量配置 Compose 
您可以使用多个环境变量来配置 Docker Compose 命令行行为。它们以COMPOSE_or开头DOCKER_，并记录在CLI 环境变量中。

撰写、编排、环境、env 文件