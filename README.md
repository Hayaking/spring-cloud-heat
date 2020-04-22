# 一、环境搭建

## 阿里云上部署好了HBase和Kafka



# 二、项目介绍

## 2.1 项目git地址

github：https://github.com/Hayaking/spring-cloud-heat

![image-20200418155255199](https://github.com/Hayaking/spring-cloud-heat/blob/master/img/image-20200418155255199.png)

## 2.2  项目模块

后端：

![image-20200418155321623](https://github.com/Hayaking/spring-cloud-heat/blob/master/img/image-20200418155321623.png)

- common：公共模块，放实体类的地方
- config：配置中心，统一其它存放模块的配置文件
- eureka：注册中心，其他模块启动时会注册到这里
- zuul：网关，前端请求网关，网管负责将请求转发给其它模块
- security：安全模块，负责登录、登出、鉴权
- spark：负责HBase和Kafka的操作
- user-service：用户管理服务
- consumer-service：热力消费者、这店管理服务

前端：

​	先不做。。。



## 2.3 数据库表

- MySQL：
heat_data.sql

![image-20200418163138386](https://github.com/Hayaking/spring-cloud-heat/blob/master/img/image-20200418163138386.png)

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

