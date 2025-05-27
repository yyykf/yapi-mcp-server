# Yapi MCP 服务器

本项目使用 Spring Boot Native Image 构建原生可执行文件和最小化的 Docker 镜像。

## 先决条件

*   已安装并正在运行 Docker。
*   已安装 Maven（如果您选择自行构建镜像）。

## 构建 Docker 镜像（可选）

预构建的镜像是 `yukaifan/yapi-mcp-server`。如果您想自己构建镜像，请使用以下命令：

```bash
mvn -Pnative clean spring-boot:build-image
```
此命令将构建原生可执行文件并将其打包到 Docker 镜像中。镜像名称通常由项目的 artifactId 和 version 决定。如果您想要特定的名称（如 `yukaifan/yapi-mcp-server`），您可能需要适当地标记它。

## 运行应用程序

### 1. 下载配置文件：
从以下地址下载 `application-mcp.yml` 配置文件：
[https://raw.githubusercontent.com/yyykf/yapi-mcp-server/refs/heads/master/src/main/resources/application-mcp.yml](https://raw.githubusercontent.com/yyykf/yapi-mcp-server/refs/heads/master/src/main/resources/application-mcp.yml)

将此文件保存到您本地计算机上的一个目录中（例如 `/path/to/your/config/` 或 `D:\Dev\config\`）。

### 2. 自定义配置：
打开下载的 `application-mcp.yml` 并根据您的环境和要求调整设置。此文件包含应用程序正确运行所需的关键设置。

### 3. 运行 Docker 容器：

#### 方法一：使用 `docker run`

在终端中执行以下命令，将 `/path/to/your/application-mcp.yml` 替换为您保存配置文件的实际绝对路径：

```bash
docker run --rm -p 8888:8888 -v /path/to/your/application-mcp.yml:/workspace/application-mcp.yml yukaifan/yapi-mcp-server
```
例如，如果您使用的是 Windows 系统并将文件保存到 `D:\Dev\config\application-mcp.yml`，则命令应为：
```bash
docker run --rm -p 8888:8888 -v D:\Dev\config\application-mcp.yml:/workspace/application-mcp.yml yukaifan/yapi-mcp-server
```
如果您使用的是 Linux 或 macOS 系统并将文件保存到 `/home/user/config/application-mcp.yml`，则命令应为：
```bash
docker run --rm -p 8888:8888 -v /home/user/config/application-mcp.yml:/workspace/application-mcp.yml yukaifan/yapi-mcp-server
```

应用程序将会启动，您应该能够通过 `http://localhost:8888` 访问它。`-v` 标志将您的本地配置文件挂载到容器内的 `/workspace/application-mcp.yml`，这是应用程序所期望的路径。

#### 方法二：使用 `docker-compose`

确保您已经下载了 `application-mcp.yml` 文件并将其与 `docker-compose.yml` 文件放置在同一目录下。然后，在该目录下打开终端并执行以下命令：

```bash
docker-compose up
```
如果您希望在后台运行，可以使用：
```bash
docker-compose up -d
```
这将使用 `docker-compose.yml` 文件中的配置来启动服务。`application-mcp.yml` 文件会自动挂载到容器中。

## 健康检查

您可以通过在浏览器中访问以下端点或使用 `curl` 等工具来检查应用程序的健康状况：

`http://localhost:8888/actuator/health`

此端点应返回一个状态，指示应用程序是否正在正确运行。
