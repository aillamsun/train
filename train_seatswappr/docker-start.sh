#!/bin/bash
docker-registry="registry.cn-hangzhou.aliyuncs.com"
docker-image="viiso/dockerdemo"
package_file="package.tgz"
file_name="dockerdemo.jar"




# 从仓库下载docker镜像
sudo docker pull $docker-registry/$docker-image
# 关闭执行中的docker容器
sudo docker stop `docker ps -q`
# 执行仓库的镜像(tag:latest)
sudo docker run -p $docker-registry/$docker-image
