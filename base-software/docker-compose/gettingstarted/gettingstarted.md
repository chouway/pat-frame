https://docs.docker.com/compose/gettingstarted/
开始使用 Docker Compose

构建一个在 Docker Compose 上运行的简单 Python Web 应用程序。该应用程序使用 Flask 框架并在 Redis 中维护一个命中计数器

先决条件
确保您已经安装了Docker Engine 和Docker Compose。您不需要安装 Python 或 Redis，因为两者都由 Docker 镜像提供。

第 1 步：设置
定义应用程序依赖项。

1. 为项目创建一个目录：

 mkdir composetest
 cd composetest
 
2.  创建app.py文件 文件内容如下：
    ---
    import time
    
    import redis
    from flask import Flask
    
    app = Flask(__name__)
    cache = redis.Redis(host='redis', port=6379)
    
    def get_hit_count():
        retries = 5
        while True:
            try:
                return cache.incr('hits')
            except redis.exceptions.ConnectionError as exc:
                if retries == 0:
                    raise exc
                retries -= 1
                time.sleep(0.5)
    
    @app.route('/')
    def hello():
        count = get_hit_count()
        return 'Hello World! I have been seen {} times.\n'.format(count)
    ---
    在此示例中，redis是应用程序网络上的 redis 容器的主机名。我们使用 Redis 的默认端口，6379.

3. 创建requirements.txt文件，文件内容如下：（里面记录当前程序的所有依赖包及其精确版本号）
---
flask
redis
---
第 2 步：创建一个 Dockerfile
在此步骤中，您将编写一个用于构建 Docker 映像的 Dockerfile。该图像包含 Python 应用程序所需的所有依赖项，包括 Python 本身。
在您的项目目录中，创建一个名为Dockerfile并粘贴以下内容的文件：    

---
# syntax=docker/dockerfile:1
FROM python:3.7-alpine
WORKDIR /code
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
EXPOSE 5000
COPY . .
CMD ["flask", "run"]
---
这告诉 Docker：

从 Python 3.7 映像开始构建映像。
将工作目录设置为/code.
设置flask命令使用的环境变量。
安装 gcc 和其他依赖项
复制requirements.txt并安装 Python 依赖项。
将元数据添加到图像以描述容器正在侦听端口 5000
将.项目中的当前目录复制到.镜像中的workdir 。
将容器的默认命令设置为flask run.

第 3 步：在 Compose 文件中定义服务
docker-compose.yml在您的项目目录中创建一个名为的文件并粘贴以下内容：
---
version: "3.9"
services:
  web:
    build: .
    ports:
      - "5000:5000"
  redis:
    image: "redis:alpine"
---
这个 Compose 文件定义了两个服务：web和redis.

网络服务
该web服务使用从Dockerfile当前目录中构建的映像。然后它将容器和主机绑定到暴露的端口5000. 此示例服务使用 Flask Web 服务器的默认端口5000.

Redis服务
该redis服务使用 从 Docker Hub 注册表中提取的公共Redis映像。

第 4 步：使用 Compose 构建并运行您的应用程序
1.从您的项目目录，通过运行启动您的应用程序docker-compose up。
  Compose 会拉取一个 Redis 镜像，为您的代码构建一个镜像，并启动您定义的服务。在这种情况下，代码会在构建时静态复制到映像中。
  
  注： Docker容器内无法解析域名：Temporary failure in name resolution
  解决方式参见： https://blog.csdn.net/qq_43743460/article/details/105648139?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.opensearchhbase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1.opensearchhbase
  
2. 在浏览器中输入 http://服务地址:5000/ 以查看应用程序正在运行。
 如果您在 Linux、Mac 的 Docker 桌面或 Windows 的 Docker 桌面上本地使用 Docker，那么 Web 应用程序现在应该在 Docker 守护程序主机上侦听端口 5000。将 Web 浏览器指向 http://localhost:5000 以查找Hello World消息。如果这不能解决，您也可以尝试 http://127.0.0.1:5000。
 
 如果您在 Mac 或 Windows 上使用 Docker Machine，请使用docker-machine ip MACHINE_VM获取 Docker 主机的 IP 地址。然后，http://MACHINE_VM_IP:5000在浏览器中打开 。
 
 您应该在浏览器中看到一条消息：
    Hello World! I have been seen 1 times.
    
3. 刷新页面，内容的数字应该递增，    
    Hello World! I have been seen 2 times.

4. 切换到另一个终端窗口，然后键入docker image ls以列出本地图像。
   此时列出图像应返回redis和web。
   您可以使用docker inspect <tag or id>.

5. 停止应用程序，方法是docker-compose down 在第二个终端的项目目录中运行，或者在启动应用程序的原始终端中按 CTRL+C。

第 5 步：编辑 Compose 文件以添加绑定挂载
编辑docker-compose.yml在项目目录添加 绑定安装的web服务：
---
version: "3.9"
services:
  web:
    build: .
    ports:
      - "5000:5000"
    volumes:
      - .:/code
    environment:
      FLASK_ENV: development
  redis:
    image: "redis:alpine"
---
新volumes密钥将主机上的项目目录（当前目录）挂载到/code容器内部，允许您即时修改代码，而无需重新构建映像。该environment键设置 FLASK_ENV环境变量，它告诉flask run在开发模式下运行，并重新加载更改代码。这种模式应该只在开发中使用。

第 6 步：使用 Compose 重新构建并运行应用程序
从您的项目目录中，键入docker-compose up以使用更新的 Compose 文件构建应用程序，然后运行它。
Hello World再次在 Web 浏览器中检查消息，然后刷新以查看计数增量。

共享文件夹、卷和绑定安装

如果您的项目在Users目录 ( cd ~) 之外，那么您需要共享您正在使用的 Dockerfile 和卷的驱动器或位置。如果您收到表明未找到应用程序文件、卷安装被拒绝或服务无法启动的运行时错误，请尝试启用文件或驱动器共享。对于位于C:\Users(Windows) 或/Users(Mac)之外的项目，卷挂载需要共享驱动器，并且对于使用Linux 容器的Docker Desktop for Windows 上的任何项目都需要共享驱动器 。有关更多信息，请参阅Docker for Mac 上的文件共享，以及有关如何在容器中管理数据的一般示例 。

如果您在较旧的 Windows 操作系统上使用 Oracle VirtualBox，您可能会遇到此VB 故障单 中所述的共享文件夹问题。较新的 Windows 系统满足Docker Desktop for Windows的要求，不需要 VirtualBox。

第 7 步：更新应用程序
由于应用程序代码现在使用卷挂载到容器中，因此您可以对其代码进行更改并立即查看更改，而无需重新构建映像。

更改问候语app.py并保存。例如，将Hello World! 消息更改为Hello from Docker!：

return 'Hello from Docker! I have been seen {} times.\n'.format(count)
在浏览器中刷新应用程序。应该更新问候语，并且计数器应该仍在增加。

第 8 步：尝试一些其他命令
如果您想在后台运行您的服务，您可以将-d标志（用于“分离”模式）传递给docker-compose up并用于docker-compose ps查看当前正在运行的内容：

 docker-compose up -d
 docker-compose ps

该docker-compose run命令允许您为您的服务运行一次性命令。例如，要查看web服务可用的环境变量 ：

 docker-compose run web env
 
 请参阅docker-compose --help以查看其他可用命令。您还可以为bash 和 zsh shell安装命令完成，这也会显示可用的命令。
 
 如果您使用 Compose 启动 Compose docker-compose up -d，请在完成服务后停止服务：
 
  docker-compose stop
 您可以使用以下down 命令将所有内容都关闭，完全删除容器。传递--volumes给 Redis 容器使用的数据卷：
 
  docker-compose down --volumes
 至此，您已经了解了 Compose 工作原理的基础知识。