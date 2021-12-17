docker引擎概要
https://docs.docker.com/engine/
Docker Engine 是一种开源容器化技术，用于构建和容器化您的应用程序。Docker Engine 充当客户端 - 服务器应用程序，具有：

具有长时间运行的守护进程的服务器dockerd。
API 指定程序可以用来与 Docker 守护程序对话和指示的接口。
命令行界面 (CLI) 客户端docker。
CLI 使用Docker API通过脚本或直接 CLI 命令来控制 Docker 守护程序或与 Docker 守护程序交互。许多其他 Docker 应用程序使用底层 API 和 CLI。守护进程创建和管理 Docker 对象，例如图像、容器、网络和卷。

centos下安装docker说明
https://docs.docker.com/engine/install/centos/

1.安装　yum-utils工具, 设置 docker-ce 软件包仓库 并可用
yum install -y yum-utils


yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

2. 安装 docker 最新引擎
yum install docker-ce docker-ce-cli containerd.io


3. 启动docker
systemctl start docker

4. 验证dcoker引擎是否正解安装 （通过docker运行hello-world镜像）
docker run hello-world

---
centos下设置docker自动重启
https://blog.csdn.net/k393393/article/details/107002675

sudo systemctl enable docker
---
docker 进程 
ps -ef | grep docker


---

---
centos下设置docker自动重启
https://blog.csdn.net/k393393/article/details/107002675

sudo systemctl enable docker
---

docker 进程 ps -ef | grep docker

===
进入容器
docker exec -it aibk_rabbitmq /bin/sh
判断是否工作在docker环境
方式一：判断根目录下 .dockerenv 文件
docker环境下：ls -alh /.dockerenv , 非docker环境，没有这个.dockerenv文件的
方式二：查询系统进程的cgroup信息
cat /proc/1/cgroup
判断响应内容即可，主要看name和devices信息，目前来说最靠谱的方式
===