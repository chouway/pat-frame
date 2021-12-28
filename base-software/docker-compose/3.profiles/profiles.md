https://docs.docker.com/compose/profiles/

在 Compose 中使用配置文件
预计阅读时间：4分钟

配置文件允许通过有选择地启用服务来针对各种用途和环境调整 Compose 应用程序模型。这是通过将每个服务分配给零个或多个配置文件来实现的。如果未分配，则始终启动该服务，但如果已分配，则仅在激活配置文件时才启动。

这允许人们在单个docker-compose.yml文件中定义额外的服务，这些服务应该只在特定场景中启动，例如用于调试或开发任务。

将配置文件分配给服务
服务通过带有一系列配置文件名称的profiles属性与配置文件相关联 ：

version: "3.9"
services:
  frontend:
    image: frontend
    profiles: ["frontend"]

  phpmyadmin:
    image: phpmyadmin
    depends_on:
      - db
    profiles:
      - debug

  backend:
    image: backend

  db:
    image: mysql
在这里，服务frontend和phpmyadmin分别分配给配置文件 frontend，debug因此仅在启用它们各自的配置文件时才启动。

没有profiles属性的服务将始终启用，即在这种情况下运行docker-compose up只会启动backend和db。

有效的配置文件名称遵循[a-zA-Z0-9][a-zA-Z0-9_.-]+.

笔记

不应分配应用程序的核心服务，profiles因此它们将始终处于启用状态并自动启动。

启用个人资料
要启用配置文件，请提供--profile 命令行选项或使用COMPOSE_PROFILES环境变量：

$ docker-compose --profile debug up
$ COMPOSE_PROFILES=debug docker-compose up
上面的命令将在debug启用配置文件的情况下启动您的应用程序。使用docker-compose.yml上面的文件，这将启动服务backend, db和phpmyadmin.

可以通过--profile为COMPOSE_PROFILES环境变量传递多个标志或逗号分隔的列表来指定多个配置文件：

$ docker-compose --profile frontend --profile debug up
$ COMPOSE_PROFILES=frontend,debug docker-compose up
自动启用配置文件和依赖项解析
当指定的服务profiles在命令行上明确定位时，其配置文件将自动启用，因此您无需手动启用它们。这可用于一次性服务和调试工具。作为一个例子，考虑这个配置：

version: "3.9"
services:
  backend:
    image: backend

  db:
    image: mysql

  db-migrations:
    image: backend
    command: myapp migrate
    depends_on:
      - db
    profiles:
      - tools
# will only start backend and db
$ docker-compose up -d

# this will run db-migrations (and - if necessary - start db)
# by implicitly enabling profile `tools`
$ docker-compose run db-migrations
但请记住，docker-compose这只会在命令行上自动启用服务的配置文件，而不是任何依赖项。这意味着目标服务的所有服务depends_on必须与它有一个共同的配置文件，始终启用（通过省略profiles）或明确启用匹配的配置文件：

version: "3.9"
services:
  web:
    image: web

  mock-backend:
    image: backend
    profiles: ["dev"]
    depends_on:
      - db

  db:
    image: mysql
    profiles: ["dev"]

  phpmyadmin:
    image: phpmyadmin
    profiles: ["debug"]
    depends_on:
      - db
# will only start "web"
$ docker-compose up -d

# this will start mock-backend (and - if necessary - db)
# by implicitly enabling profile `dev`
$ docker-compose up -d mock-backend

# this will fail because profile "dev" is disabled
$ docker-compose up phpmyadmin
尽管定位phpmyadmin将自动启用其配置文件 - 即 debug- 它不会自动启用db- 即所需的配置文件dev。要解决此问题，您必须将debug配置文件添加到db服务中：

db:
  image: mysql
  profiles: ["debug", "dev"]
或db显式启用配置文件：

# profile "debug" is enabled automatically by targeting phpmyadmin
$ docker-compose --profile dev up phpmyadmin
$ COMPOSE_PROFILES=dev docker-compose up phpmyadmin