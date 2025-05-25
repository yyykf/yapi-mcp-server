# 多阶段构建 Dockerfile
# 第一阶段：构建阶段
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# 设置工作目录
WORKDIR /app

# 复制 Maven 配置文件
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
COPY docker/settings.xml /root/.m2/settings.xml

# 创建 Maven 本地仓库目录
RUN mkdir -p /root/.m2/repository

# 下载依赖（利用 Docker 缓存层，使用阿里云镜像源）
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml || \
    mvn dependency:resolve -B -s /root/.m2/settings.xml || \
    echo "依赖下载完成（可能有部分失败，但不影响构建）"

# 复制源代码
COPY src ./src

# 构建应用
RUN mvn clean package -DskipTests -B -s /root/.m2/settings.xml

# 第二阶段：运行阶段
FROM eclipse-temurin:21-jre-alpine

# 设置时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 创建应用用户
RUN addgroup -g 1000 appuser && \
    adduser -D -s /bin/sh -u 1000 -G appuser appuser

# 设置工作目录
WORKDIR /app

# 创建日志目录
RUN mkdir -p /app/logs && \
    chown -R appuser:appuser /app

# 从构建阶段复制 JAR 文件
COPY --from=builder /app/target/*.jar app.jar

# 更改文件所有者
RUN chown appuser:appuser app.jar

# 切换到非 root 用户
USER appuser

# 暴露端口
EXPOSE 8888

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom --enable-preview"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8888/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 