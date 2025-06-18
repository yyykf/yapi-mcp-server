# Yapi MCP 服务器

一个用于 Yapi API 管理平台的模型上下文协议（MCP）服务器。本项目提供了 AI 助手与 Yapi 之间的无缝集成，让您可以通过自然语言查询 API 文档、搜索接口并管理 API 信息。

## 功能特性

本 MCP 服务器提供以下工具来与 Yapi 交互：

| 工具 | 描述 |
|------|------|
| `listProjects` | 列出所有已配置的 Yapi 项目及其 ID 和名称 |
| `listCategories` | 获取指定 Yapi 项目的接口分类 |
| `listCatInterfaces` | 列出指定分类下的接口 |
| `searchInterfaces` | 根据项目名称、关键字或路径搜索 API 接口 |
| `getInterfaceDetail` | 获取指定 API 接口的详细信息 |
| `refreshCache` | 手动刷新 Yapi 缓存 |
| `clearProjectCache` | 清空指定项目的缓存 |
| `getCacheStats` | 获取缓存统计信息 |

## 运行环境要求

选择以下任一环境：

- **Java 环境**：JDK 21+ 和 Maven 3.6+
- **Docker 环境**：Docker 和 Docker Compose

## 快速开始

### 方法一：使用 Java 运行

```bash
# 克隆仓库
git clone https://github.com/yyykf/yapi-mcp-server.git
cd yapi-mcp-server

# 配置 Yapi 地址和项目 token
vim src/main/resources/application-mcp.yml

# 编译项目
mvn clean package

# 运行应用
java -jar target/yapi-mcp-server-0.0.1-SNAPSHOT.jar
```

### 方法二：使用 Docker 运行

```bash
# 克隆仓库
git clone https://github.com/yyykf/yapi-mcp-server.git
cd yapi-mcp-server

# 配置 Yapi 地址和项目 token
vim src/main/resources/application-mcp.yml

# 使用 Docker Compose 运行
docker-compose up -d
```

## 配置说明

在运行应用程序之前，您需要在 `src/main/resources/application-mcp.yml` 中配置 Yapi 连接设置：

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          yapiClient:
            url: http://yapi.com # 请替换为你的Yapi服务地址
yapi:
  project-tokens:
    123456: "your-project-token-1"  # 项目 ID: Token
    789012: "your-project-token-2"  # 根据需要添加更多项目
```

## 构建多架构 Docker 镜像

如果您需要手动构建和推送多架构 Docker 镜像，请使用提供的脚本：

```bash
./build-and-push.sh <版本号> <docker用户名> <docker密码>
```

示例：
```bash
./build-and-push.sh 1.0.0 myusername mypassword
```

此脚本将为 `amd64` 和 `arm64` 架构构建镜像，并创建多架构清单。

## 项目文档

如需了解更详细的项目文档和深入分析，您可以使用 DeepWiki 探索本项目：

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/yyykf/yapi-mcp-server)

## 与 Cursor 集成

### 方法一：一键安装

点击下方按钮自动在 Cursor 中安装 MCP 服务器：

[![Install MCP Server](https://cursor.com/deeplink/mcp-install-dark.svg)](https://cursor.com/install-mcp?name=yapi&config=eyJ1cmwiOiJodHRwOi8vMTI3LjAuMC4xOjg4ODgvc3NlIn0%3D)

### 方法二：手动配置

将以下配置添加到您的 Cursor MCP 设置文件（`mcp.json`）中：

```json
{
  "mcpServers": {
    "yapi": {
      "url": "http://127.0.0.1:8888/sse"
    }
  }
}
```

## 健康检查

应用程序运行后，您可以检查其健康状态：

```bash
curl http://localhost:8888/actuator/health
```

或在浏览器中访问 `http://localhost:8888/actuator/health`。

## 使用示例

在 Cursor 中设置 MCP 服务器后，您可以使用自然语言与您的 Yapi 实例交互：

- "显示所有可用的项目"
- "列出项目 123456 中的所有 API 分类"
- "搜索登录相关的 API"
- "获取接口 ID 789 的详细信息"
- "缓存状态如何？"

## 许可证

本项目基于 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。
