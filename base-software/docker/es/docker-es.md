Elasticsearch 指南 [7.16] » 设置 Elasticsearch » 安装 Elasticsearch » 使用 Docker 安装 Elasticsearch
·
使用 Docker 安装 Elasticsearch

Elasticsearch 也可以作为 Docker 镜像使用。www.docker.elastic.co上提供了所有已发布的 Docker 映像和标签的 列表。源文件位于 Github中。

该软件包包含免费和订阅功能。 开始 30 天试用以试用所有功能。

拉取图像编辑
docker pull获取 Elasticsearch for Docker 就像对 Elastic Docker 注册表发出命令一样简单。
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.16.3

使用 Docker 启动单节点集群编辑
要启动单节点 Elasticsearch 集群进行开发或测试，请指定 单节点发现以绕过引导检查：
docker run -p 127.0.0.1:9200:9200 -p 127.0.0.1:9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.16.3

使用 Docker Compose 启动多节点集群编辑
要在 Docker 中启动并运行三节点 Elasticsearch 集群，您可以使用 Docker Compose：

创建一个docker-compose.yml文件：

version: '2.2'
services:
  es01:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.3
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - data01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - elastic
  es02:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.3
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - data02:/usr/share/elasticsearch/data
    networks:
      - elastic
  es03:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.16.3
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - data03:/usr/share/elasticsearch/data
    networks:
      - elastic

volumes:
  data01:
    driver: local
  data02:
    driver: local
  data03:
    driver: local

networks:
  elastic:
    driver: bridge

此示例docker-compose.yml文件使用ES_JAVA_OPTS 环境变量手动将堆大小设置为 512MB。我们不建议ES_JAVA_OPTS在生产中使用。请参阅手动设置堆大小。
这个示例 Docker Compose 文件启动了一个三节点 Elasticsearch 集群。Node通过 Docker 网络es01监听localhost:9200并es02与之es03交谈。es01

请注意，此配置在所有网络接口上公开端口 9200，并且考虑到 Dockeriptables在 Linux 上的操作方式，这意味着您的 Elasticsearch 集群可以公开访问，可能会忽略任何防火墙设置。如果您不想公开端口 9200 而是使用反向代理，请在 docker-compose.yml 文件中9200:9200替换为。127.0.0.1:9200:9200Elasticsearch 将只能从主机本身访问。

Docker 命名为 volumes 、和存储节点数据目录，以便数据在重新启动后保持不变data01。如果它们尚不存在，请在启动集群时创建它们。data02data03docker-compose

确保为 Docker 引擎分配了至少 4GiB 的内存。在 Docker Desktop 中，您可以在 Preference (macOS) 或 Settings (Windows) 的 Advanced 选项卡上配置资源使用情况。

Run docker-compose to bring up the cluster:

docker-compose up
Submit a _cat/nodes request to see that the nodes are up and running:

curl -X GET "localhost:9200/_cat/nodes?v=true&pretty"    