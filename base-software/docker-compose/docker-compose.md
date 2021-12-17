docker-compose概要
https://docs.docker.com/compose/
Compose 是一个用于定义和运行多容器 Docker 应用程序的工具。借助 Compose，您可以使用 YAML 文件来配置应用程序的服务。然后，使用单个命令，从配置中创建并启动所有服务

Compose 具有用于管理应用程序整个生命周期的命令：

启动、停止和重建服务
查看正在运行的服务的状态
流式传输正在运行的服务的日志输出
对服务运行一次性命令


linux下安装docker-compose
https://docs.docker.com/compose/install/

sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
docker-compose --version

----
https://docs.docker.com/compose/gettingstarted/
docker-compose入门使用