# 一、环境搭建

## 1.1 安装Docker

虚拟机分配内存大于等于2G。

教程地址：https://www.runoob.com/docker/centos-docker-install.html

## 1.2 安装HBase

启动docker，使用```docker pull boostport/hbase-phoenix-all-in-one ```下载镜像

使用如下命令创建并启动docker容器

```shell
docker run -itd --name hbase \
 --hostname hbase \
 -p 2181:2181 \
 -p 16000:16000 \
 -p 16010:16010 \
 -p 16020:16020 \
 -p 16030:16030 \
boostport/hbase-phoenix-all-in-one
```

## 1.3 安装Kafka

下载镜像：```docker pull spotify/kafka``` 

创建并启动镜像：

```shell
  docker run -itd \
 --name kafka \
 --hostname kafka \
 -p 9092:9092 spotify/kafka
```



# 二、项目介绍

## 2.1 项目git地址

github：https://github.com/Hayaking/spring-cloud-heat

![image-20200418155255199](xx.assets/image-20200418155255199.png)

## 2.2  项目模块

后端：

![image-20200418155321623](xx.assets/image-20200418155321623.png)

- common：公共模块，放实体类的地方
- config：配置中心，统一其它存放模块的配置文件
- eureka：注册中心，其他模块启动时会注册到这里
- zuul：网关，前端请求网关，网管负责将请求转发给其它模块
- security：安全模块，负责登录、登出、鉴权
- spark：负责HBase和Kafka的操作
- 。。。。其它业务模块带扩展

前端：

​	先不做。。。



## 2.3 数据库表

- MySQL：
heat_data.sql

![image-20200418163138386](xx.assets/image-20200418163138386.png)

- HBase：

进入hbase容器：```docker exec -it hbase /bin/bash```

找到Phoenix的安装目录: ```cd /opt/phoenix/bin```

进入shell：```./sqline.py 127.0.0.1:2181```

执行以下SQL：

```sql
CREATE TABLE HeatData (
    id INTEGER PRIMARY KEY,
    consumerId DOUBLE,
    temperature DOUBLE,
    pressure DOUBLE,
    flow DOUBLE,
    createDate BIGINT
);
```

