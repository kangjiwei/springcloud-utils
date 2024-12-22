# 使用官方的 Java 8 环境作为基础镜像
FROM openjdk:8-jdk

# 在容器中创建一个目录来存放我们的应用
WORKDIR /app

# 将 Maven target 目录下的 jar 文件复制到容器中的 app 目录下
COPY target/*.jar /app

# 配置容器需要的环境变量
ENV JAVA_OPTS=""

# 容器启动时执行的命令
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar *.jar" ]

EXPOSE 1000