# Yapi MCP Server Docker 部署指南

本指南将帮助您使用 Docker 快速部署和运行 Yapi MCP Server，无需安装 Java 运行环境。

## 📋 前置要求

- Docker 20.10+ 
- Docker Compose 2.0+ (或 docker-compose 1.29+)

### 安装 Docker

#### Windows
1. 下载并安装 [Docker Desktop for Windows](https://www.docker.com/products/docker-desktop/)
2. 启动 Docker Desktop

#### macOS
1. 下载并安装 [Docker Desktop for Mac](https://www.docker.com/products/docker-desktop/)
2. 启动 Docker Desktop

#### Linux (Ubuntu/Debian)
```bash
# 更新包索引
sudo apt-get update

# 安装 Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 启动 Docker 服务
sudo systemctl start docker
sudo systemctl enable docker

# 将当前用户添加到 docker 组（可选）
sudo usermod -aG docker $USER
```

## 🚀 快速启动

### 方法一：使用启动脚本（推荐）

#### Windows
```cmd
# 双击运行或在命令行执行
docker-start.bat
```

#### Linux/macOS
```bash
# 给脚本执行权限
chmod +x docker-start.sh

# 运行脚本
./docker-start.sh
```

### 方法二：手动启动

1. **配置环境变量（可选）**
   ```bash
   # 复制配置文件模板
   cp docker.env.example docker.env
   
   # 编辑配置文件
   nano docker.env  # 或使用其他编辑器
   ```

2. **构建并启动服务**
   ```bash
   # 使用 Docker Compose V2
   docker compose up -d --build
   
   # 或使用旧版本
   docker-compose up -d --build
   ```

## ⚙️ 配置说明

### 环境变量配置

复制 `docker.env.example` 为 `docker.env` 并修改以下配置：

```bash
# Yapi 服务地址
YAPI_BASE_URL=http://your-yapi-server:port

# 项目 Token 配置
YAPI_PROJECT_TOKENS_3723=your_actual_token_1
YAPI_PROJECT_TOKENS_67890=your_actual_token_2

# JVM 配置（可选）
JAVA_OPTS=-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom
```

### 端口配置

默认端口为 `8888`，如需修改请编辑 `docker-compose.yml`：

```yaml
ports:
  - "你的端口:8888"
```

## 📊 服务管理

### 查看服务状态
```bash
docker ps
# 或
docker compose ps
```

### 查看日志
```bash
# 查看实时日志
docker logs -f yapi-mcp-server

# 查看最近 100 行日志
docker logs --tail 100 yapi-mcp-server
```

### 停止服务
```bash
docker compose down
# 或
docker-compose down
```

### 重启服务
```bash
docker compose restart
# 或
docker-compose restart
```

### 更新服务
```bash
# 重新构建并启动
docker compose up -d --build
```

## 🔍 健康检查

服务启动后，可以通过以下方式检查服务状态：

- **应用健康检查**: http://localhost:8888/actuator/health
- **服务信息**: http://localhost:8888/actuator/info
- **指标信息**: http://localhost:8888/actuator/metrics

## 📁 目录结构

```
yapi-mcp-server/
├── Dockerfile                 # Docker 镜像构建文件
├── docker-compose.yml         # Docker Compose 配置
├── .dockerignore              # Docker 忽略文件
├── docker.env.example         # 环境变量配置模板
├── docker-start.sh            # Linux/macOS 启动脚本
├── docker-start.bat           # Windows 启动脚本
├── logs/                      # 日志目录（自动创建）
└── src/                       # 源代码目录
```

## 🐛 故障排除

### 网络连接问题

如果遇到 Maven 依赖下载失败的问题，可以使用以下方法：

#### 方法一：网络诊断
```bash
# 运行网络诊断工具
./test-network.sh
```

#### 方法二：使用本地构建模式
```bash
# 1. 先在本地构建 JAR 文件
mvn clean package -DskipTests
# 或使用 Maven Wrapper
./mvnw clean package -DskipTests

# 2. 使用本地构建的 Docker Compose 文件
docker compose -f docker-compose.local.yml up -d --build
```

#### 方法三：配置代理（如果在企业网络环境）
在 `docker/settings.xml` 中添加代理配置：
```xml
<proxies>
  <proxy>
    <id>http-proxy</id>
    <active>true</active>
    <protocol>http</protocol>
    <host>your-proxy-host</host>
    <port>your-proxy-port</port>
  </proxy>
</proxies>
```

### 常见问题

1. **端口被占用**
   ```bash
   # 检查端口占用
   netstat -tulpn | grep 8888
   
   # 修改 docker-compose.yml 中的端口映射
   ```

2. **Docker 构建失败**
   ```bash
   # 清理 Docker 缓存
   docker system prune -a
   
   # 重新构建
   docker compose build --no-cache
   ```

3. **Maven 依赖下载失败**
   - 使用阿里云镜像源（已在 Dockerfile 中配置）
   - 检查网络连接：`./test-network.sh`
   - 使用本地构建模式：`docker compose -f docker-compose.local.yml up -d`

4. **服务无法访问 Yapi**
   - 检查 `YAPI_BASE_URL` 配置是否正确
   - 确保 Docker 容器能够访问 Yapi 服务器
   - 检查防火墙设置

5. **内存不足**
   ```bash
   # 调整 JVM 内存配置
   JAVA_OPTS=-Xms128m -Xmx256m
   ```

### 查看详细日志
```bash
# 查看容器内部日志文件
docker exec -it yapi-mcp-server tail -f /app/logs/yapi-mcp-server.log

# 进入容器调试
docker exec -it yapi-mcp-server sh
```

## 🔧 高级配置

### 自定义配置文件

如需使用自定义配置文件，可以通过挂载卷的方式：

```yaml
volumes:
  - ./config/application-custom.yml:/app/config/application-custom.yml:ro
```

然后设置环境变量：
```bash
SPRING_PROFILES_ACTIVE=docker,custom
```

### 数据持久化

如果需要持久化缓存或其他数据：

```yaml
volumes:
  - ./data:/app/data
  - ./logs:/app/logs
```

### 网络配置

如需与其他服务通信，可以使用自定义网络：

```yaml
networks:
  yapi-network:
    external: true
```

## 📞 技术支持

如果遇到问题，请：

1. 查看日志文件：`./logs/yapi-mcp-server.log`
2. 检查服务健康状态：http://localhost:8888/actuator/health
3. 提交 Issue 到项目仓库

## 🎉 完成

现在您可以通过 http://localhost:8888 访问 Yapi MCP Server 了！

服务启动后，您可以使用 MCP 客户端连接到此服务器，享受 Yapi API 文档查询功能。 