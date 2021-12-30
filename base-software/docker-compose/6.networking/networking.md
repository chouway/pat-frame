https://docs.docker.com/compose/networking/
Compose 中的网络
预计阅读时间：6分钟

此页面适用于 Compose 文件格式版本 2及更高版本。Compose 文件版本 1（已弃用）不支持网络功能。

默认情况下，Compose为您的应用程序设置单个 网络。服务的每个容器都加入默认网络，并且可以被该网络上的其他容器访问，并且可以在与容器名称相同的主机名处被它们发现。

笔记

您的应用程序网络的名称基于“项目名称”，该名称基于其所在目录的名称。您可以使用--project-name标志 或COMPOSE_PROJECT_NAME环境变量覆盖项目名称。

例如，假设您的应用程序位于名为 的目录中myapp，您的docker-compose.yml外观如下所示：

version: "3.9"
services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres
    ports:
      - "8001:5432"
运行时docker-compose up，会发生以下情况：

myapp_default创建了一个名为的网络。
使用web的配置创建容器。它myapp_default以 的名义加入网络 web。
使用db的配置创建容器。它myapp_default以 的名义加入网络 db。
在 v2.1+ 中，覆盖网络总是 attachable

从 Compose 文件格式 2.1 开始，覆盖网络始终创建为 attachable，并且这是不可配置的。这意味着独立容器可以连接到覆盖网络。

在 Compose 文件格式 3.x 中，您可以选择将该attachable属性设置为false.

每个容器现在都可以查找主机名web或db取回相应容器的 IP 地址。例如，web的应用程序代码可以连接到 URLpostgres://db:5432并开始使用 Postgres 数据库。

要注意区分是很重要的HOST_PORT和CONTAINER_PORT。在上面的例子中，对于db，HOST_PORT是8001和容器端口是 5432（postgres默认）。网络服务到服务通信使用CONTAINER_PORT. 当HOST_PORT定义，服务以及虫群外部访问。

在web容器中，您的连接字符串 todb看起来像 postgres://db:5432，而在主机上，连接字符串看起来像postgres://{DOCKER_IP}:8001。

更新容器
如果您对服务进行配置更改并运行docker-compose up以更新它，旧容器将被删除，新容器以不同的 IP 地址但同名加入网络。正在运行的容器可以查找该名称并连接到新地址，但旧地址停止工作。

如果任何容器具有与旧容器的连接，则它们将被关闭。容器有责任检测这种情况，再次查找名称并重新连接。

链接
链接允许您定义额外的别名，通过这些别名可以从另一个服务访问服务。它们不需要启用服务进行通信 - 默认情况下，任何服务都可以以该服务的名称访问任何其他服务。在以下示例中，db可从web主机名db和 访问database：

version: "3.9"
services:

  web:
    build: .
    links:
      - "db:database"
  db:
    image: postgres
有关更多信息，请参阅链接参考。

多主机网络
在启用了Swarm 模式的 Docker 引擎上部署 Compose 应用程序时，您可以使用内置overlay驱动程序来启用多主机通信。

请参阅Swarm 模式部分，了解如何设置 Swarm 集群，以及多主机网络入门 以了解多主机覆盖网络。

指定自定义网络
您可以使用顶级networks密钥指定您自己的网络，而不是仅使用默认的应用程序网络。这使您可以创建更复杂的拓扑并指定自定义网络驱动程序和选项。您还可以使用它来将服务连接到不受 Compose 管理的外部创建的网络。

每个服务都可以使用服务级别 networks密钥指定要连接的网络，服务级别密钥是引用顶级 networks密钥下条目的名称列表。

这是一个定义两个自定义网络的 Compose 文件示例。该proxy服务是从分离的db服务，因为它们不共享一个共同的网络-只app可以跟两者。

version: "3.9"

services:
  proxy:
    build: ./proxy
    networks:
      - frontend
  app:
    build: ./app
    networks:
      - frontend
      - backend
  db:
    image: postgres
    networks:
      - backend

networks:
  frontend:
    # Use a custom driver
    driver: custom-driver-1
  backend:
    # Use a custom driver which takes special options
    driver: custom-driver-2
    driver_opts:
      foo: "1"
      bar: "2"
通过为每个连接的网络设置ipv4_address 和/或 ipv6_address，可以使用静态 IP 地址配置网络。

也可以给网络一个自定义名称（从 3.5 版开始）：

version: "3.9"
services:
  # ...
networks:
  frontend:
    name: custom_frontend
    driver: custom-driver-1
有关可用网络配置选项的完整详细信息，请参阅以下参考资料：

顶级networks密钥
服务级别networks密钥
配置默认网络
除了（或同时）指定您自己的网络之外，您还可以通过在networksnamed下定义一个条目来更改应用程序范围的默认网络的设置default：

version: "3.9"
services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres

networks:
  default:
    # Use a custom driver
    driver: custom-driver-1
使用预先存在的网络
如果您希望容器加入预先存在的网络，请使用以下external选项：

services:
  # ...
networks:
  default:
    external: true
    name: my-pre-existing-network
[projectname]_defaultCompose不会尝试创建名为 的网络，而是查找名为的网络my-pre-existing-network并将您的应用程序的容器连接到该网络。

文档, docs , docker , compose ,编排,容器,网络