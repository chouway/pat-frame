虚拟机；方便构建linux环境下项目所需的基础组件

果核剥壳搜vmware
https://www.ghxi.com/?s=vmware

VMware Workstation Pro v16.2.1 官方版+激活密钥
https://www.ghxi.com/vmware15.html

虚拟机装载 ISO (Centos7.9)

or 百度网盘下载

链接: https://pan.baidu.com/s/1HreH46R5ZSY5bmEQFRQnOg 提取码: k283 复制这段内容后打开百度网盘手机App，操作更方便哦
--------------


vmware加载系统软盘时
设置网络为桥接模式

管理员
root / vmware123


# 查看当前网卡
ip addr

# 修改网卡
find  / -name ifcfg-xxx  找到并编辑当前网卡信息
vi ifcfg-ens33

#指定ip地址
BOOTPROTO=static
IPADDR=192.168.40.53
NETMASK=255.255.255.0
GATEWAY=192.168.40.1
DNS1=8.8.8.8

# 重启网络
service network restart


# 重启网络
service network restart

# 测试网络
ping www.baidu.com

# yum 查看指令依赖听安装包
yum whatprovides ifconfig

# yun 安装 net-tools 
yum install net-tools

# yun 安装 vim
yum install vim

开启ssh

# 查看是否已安 ssh
yum list installed|grep openssh-server

# 查看ssh 是否启用
ps -ef|grep ssh


# 修改sshd服务配置
cd /etc/ssh
vim ssh_config

开启 密码登录
PasswordAuthentication yes

开启 端口监听
Port 22

# 重启ssh
serivce sshd restart  (或者 systemctl restart sshd)

Linux中如何启动、重启、停止、重载服务以及检查服务（如 httpd.service）状态 
# systemctl start httpd.service
# systemctl restart httpd.service
# systemctl stop httpd.service
# systemctl reload httpd.service
# systemctl status httpd.service

#查看端口
netstat -an|grep 22


#防火墙开放端口 22


#firewall
添加
firewall-cmd --zone=public --add-port=22/tcp --permanent    （--permanent永久生效，没有此参数重启后失效）
重新载入
firewall-cmd --reload
查看
firewall-cmd --zone=public --query-port=22/tcp
firewall-cmd --permanent --zone=public --list-ports
删除
firewall-cmd --zone=public --remove-port=22/tcp --permanent

远程sshx进行学习应用
添加远程会话  配置 ip port  用户/密码 进行登录


#Linux 查看磁盘空间
Linux 查看磁盘空间可以使用 df 和 du 命令。
https://www.runoob.com/w3cnote/linux-view-disk-space.html

df
df 以磁盘分区为单位查看文件系统，可以获取硬盘被占用了多少空间，目前还剩下多少空间等信息。

例如，我们使用df -h命令来查看磁盘信息， -h 选项为根据大小适当显示：
显示内容参数说明：

Filesystem：文件系统
Size： 分区大小
Used： 已使用容量
Avail： 还可以使用的容量
Use%： 已用百分比
Mounted on： 挂载点　
相关命令：
df -hl：查看磁盘剩余空间
df -h：查看每个根路径的分区大小
du -sh [目录名]：返回该目录的大小
du -sm [文件夹]：返回该文件夹总M数
du -h [目录名]：查看指定文件夹下的所有文件大小（包含子文件夹）


du
du 的英文原义为 disk usage，含义为显示磁盘空间的使用情况，用于查看当前目录的总大小。

例如查看当前目录的大小：
 du -sh
 
 du 命令用于查看当前目录的总大小：
 # du -sh
 605M  
 
 
 
 显示指定文件所占空间：
 # du log2012.log 
 300     log2012.log
 
 du -sh * 查看当前目录的文件大小
 
 du 命令用于查看当前目录的总大小：
 -s：对每个Names参数只给出占用的数据块总数。
 -a：递归地显示指定目录中各文件及子目录中各文件占用的数据块数。若既不指定-s，也不指定-a，则只显示Names中的每一个目录及其中的各子目录所占的磁盘块数。
 -b：以字节为单位列出磁盘空间使用情况（系统默认以k字节为单位）。
 -k：以1024字节为单位列出磁盘空间使用情况。
 -c：最后再加上一个总计（系统默认设置）。
 -l：计算所有的文件大小，对硬链接文件，则计算多次。
 -x：跳过在不同文件系统上的目录不予统计。
 -h：以K，M，G为单位，提高信息的可读性。