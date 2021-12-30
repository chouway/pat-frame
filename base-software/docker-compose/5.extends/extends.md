https://docs.docker.com/compose/extends/
在文件和项目之间共享 Compose 配置
预计阅读时间：10分钟

Compose 支持两种共享通用配置的方法：

使用多个 Compose 文件扩展整个 Compose 文件
使用该extends字段扩展单个服务（对于最高 2.1 的 Compose 文件版本）
多个撰写文件
使用多个 Compose 文件使您可以针对不同的环境或不同的工作流程自定义 Compose 应用程序。

理解多个 Compose 文件
默认情况下，Compose 读取两个文件，一个文件docker-compose.yml和一个可选 docker-compose.override.yml文件。按照惯例，docker-compose.yml 包含您的基本配置。顾名思义，覆盖文件可以包含现有服务或全新服务的配置覆盖。

如果在两个文件中都定义了服务，Compose 会使用添加和覆盖配置中描述的规则合并 配置。

要使用多个覆盖文件或具有不同名称的覆盖文件，您可以使用该-f选项来指定文件列表。Compose 按照在命令行中指定的顺序合并文件。有关使用的更多信息，请参阅 docker-compose命令参考-f。

当您使用多个配置文件时，您必须确保文件中的所有路径都相对于基本 Compose 文件（用 指定的第一个 Compose 文件-f）。这是必需的，因为覆盖文件不必是有效的 Compose 文件。覆盖文件可以包含配置的小片段。跟踪服务的哪个片段与哪个路径相关是困难和混乱的，因此为了使路径更易于理解，必须相对于基本文件定义所有路径。

示例用例
在本节中，多个 Compose 文件有两个常见用例：针对不同环境更改 Compose 应用程序，以及针对 Compose 应用程序运行管理任务。

不同的环境
多个文件的一个常见用例是为类似生产的环境（可能是生产、暂存或 CI）更改开发 Compose 应用程序。为了支持这些差异，您可以将 Compose 配置拆分为几个不同的文件：

从定义服务规范配置的基本文件开始。

docker-compose.yml

web:
  image: example/my_web_app:latest
  depends_on:
    - db
    - cache

db:
  image: postgres:latest

cache:
  image: redis:latest
在这个例子中，开发配置向主机公开了一些端口，将我们的代码挂载为一个卷，并构建 Web 映像。

docker-compose.override.yml

web:
  build: .
  volumes:
    - '.:/code'
  ports:
    - 8883:80
  environment:
    DEBUG: 'true'

db:
  command: '-d'
  ports:
    - 5432:5432

cache:
  ports:
    - 6379:6379
当您运行时，docker-compose up它会自动读取覆盖。

现在，在生产环境中使用这个 Compose 应用程序会很好。因此，创建另一个覆盖文件（可能存储在不同的 git 存储库中或由不同的团队管理）。

docker-compose.prod.yml

web:
  ports:
    - 80:80
  environment:
    PRODUCTION: 'true'

cache:
  environment:
    TTL: '500'
要使用此生产 Compose 文件进行部署，您可以运行

 docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
该部署使用配置中的所有三个服务 docker-compose.yml和docker-compose.prod.yml（但不是在开发配置docker-compose.override.yml）。

见生产有关撰写生产更多的信息。

行政任务
另一个常见用例是针对 Compose 应用程序中的一个或多个服务运行临时或管理任务。此示例演示了运行数据库备份。

从docker-compose.yml 开始。

web:
  image: example/my_web_app:latest
  depends_on:
    - db

db:
  image: postgres:latest
在docker-compose.admin.yml 中添加一个新服务来运行数据库导出或备份。

    dbadmin:
      build: database_admin/
      depends_on:
        - db
要启动正常环境运行docker-compose up -d。要运行数据库备份，还包括docker-compose.admin.yml。

 docker-compose -f docker-compose.yml -f docker-compose.admin.yml \
  run dbadmin db-backup
延伸服务
笔记

该extends关键字在 Compose 文件版本 2.1 之前的早期 Compose 文件格式中受支持（请参阅在 v2 中扩展），但在 Compose 版本 3.x 中不受支持。请参阅第3 版 添加和删​​除密钥的摘要，以及有关如何升级的信息。请参阅moby/moby#31101以遵循有关extends在未来版本中以某种形式添加支持的可能性的讨论线程。该extends关键字已包含在 docker-compose 1.27 及更高版本中。

Docker Compose 的extends关键字可以在不同的文件，甚至完全不同的项目之间共享通用配置。如果您有多个重复使用一组通用配置选项的服务，则扩展服务很有用。使用extends您可以在一个地方定义一组通用的服务选项并从任何地方引用它。

请记住，volumes_from和depends_on使用服务之间从不共享extends。这些异常的存在是为了避免隐式依赖；你总是volumes_from在本地定义。这确保在读取当前文件时服务之间的依赖关系清晰可见。在本地定义这些还可以确保对引用文件的更改不会破坏任何内容。

了解扩展配置
在 中定义任何服务时docker-compose.yml，您可以声明您正在扩展另一个服务，如下所示：

services:
  web:
    extends:
      file: common-services.yml
      service: webapp
这指示 Compose 重新使用文件中webapp定义的服务的配置common-services.yml。假设common-services.yml 看起来像这样：

services:
  webapp:
    build: .
    ports:
      - "8000:8000"
    volumes:
      - "/data"
在这种情况下，你会得到完全相同的结果，如果你写了 docker-compose.yml与同build，ports和volumes配置值直属定义web。

您可以进一步在本地定义（或重新定义）配置 docker-compose.yml：

services:
  web:
    extends:
      file: common-services.yml
      service: webapp
    environment:
      - DEBUG=1
    cpu_shares: 5

  important_web:
    extends: web
    cpu_shares: 10
您还可以编写其他服务并将您的web服务链接到它们：

services:
  web:
    extends:
      file: common-services.yml
      service: webapp
    environment:
      - DEBUG=1
    cpu_shares: 5
    depends_on:
      - db
  db:
    image: postgres
示例用例
当您有多个具有通用配置的服务时，扩展单个服务非常有用。下面的示例是具有两个服务的 Compose 应用程序：一个 Web 应用程序和一个队列工作器。这两种服务使用相同的代码库并共享许多配置选项。

在common.yml 中，我们定义了通用配置：

services:
  app:
    build: .
    environment:
      CONFIG_FILE_PATH: /code/config
      API_KEY: xxxyyy
    cpu_shares: 5
在docker-compose.yml 中，我们定义了使用通用配置的具体服务：

services:
  webapp:
    extends:
      file: common.yml
      service: app
    command: /code/run_web_app
    ports:
      - 8080:8080
    depends_on:
      - queue
      - db

  queue_worker:
    extends:
      file: common.yml
      service: app
    command: /code/run_worker
    depends_on:
      - queue
添加和覆盖配置
Compose 将配置从原始服务复制到本地服务。如果在原始服务和本地服务中都定义了配置选项，则本地值将替换或扩展原始值。

对于image, commandor等单值选项，mem_limit新值替换旧值。

原始服务：

services:
  myservice:
    # ...
    command: python app.py
本地服务：

services:
  myservice:
    # ...
    command: python otherapp.py
结果：

services:
  myservice:
    # ...
    command: python otherapp.py
对于多值的选项 ports，expose，external_links，dns， dns_search，和tmpfs，撰写串接两组的值：

原始服务：

services:
  myservice:
    # ...
    expose:
      - "3000"
本地服务：

services:
  myservice:
    # ...
    expose:
      - "4000"
      - "5000"
结果：

services:
  myservice:
    # ...
    expose:
      - "3000"
      - "4000"
      - "5000"
在environment, labels, volumes, 和的情况下devices，Compose“合并”条目与本地定义的值优先。对于 environmentand labels，环境变量或标签名称决定使用哪个值：

原始服务：

services:
  myservice:
    # ...
    environment:
      - FOO=original
      - BAR=original
本地服务：

services:
  myservice:
    # ...
    environment:
      - BAR=local
      - BAZ=local
结果

services:
  myservice:
    # ...
    environment:
      - FOO=original
      - BAR=local
      - BAZ=local
条目volumes和devices正在使用的安装在容器路径合并：

原始服务：

services:
  myservice:
    # ...
    volumes:
      - ./original:/foo
      - ./original:/bar
本地服务：

services:
  myservice:
    # ...
    volumes:
      - ./local:/bar
      - ./local:/baz
结果：

services:
  myservice:
    # ...
    volumes:
      - ./original:/foo
      - ./local:/bar
      - ./local:/baz