# Yapi MCP Server

A Model Context Protocol (MCP) Server for Yapi API management platform. This project provides seamless integration between AI assistants and Yapi, allowing you to query API documentation, search interfaces, and manage API information through natural language.

For the Chinese version of this document, please see [README_zh.md](./README_zh.md).

## Features

This MCP server provides the following tools for interacting with Yapi:

| Tool | Description |
|------|-------------|
| `listProjects` | List all configured Yapi projects with their IDs and names |
| `listCategories` | Get interface categories for a specific Yapi project |
| `listCatInterfaces` | List interfaces within a specific category |
| `searchInterfaces` | Search API interfaces by project name, keyword, or path |
| `getInterfaceDetail` | Get detailed information about a specific API interface |
| `refreshCache` | Manually refresh the Yapi cache |
| `clearProjectCache` | Clear cache for a specific project |
| `getCacheStats` | Get cache statistics information |

## Prerequisites

Choose one of the following environments:

- **Java Environment**: JDK 21+ and Maven 3.6+
- **Docker Environment**: Docker and Docker Compose

## Quick Start

### Method 1: Run with Java

```bash
# Clone the repository
git clone https://github.com/yyykf/yapi-mcp-server.git
cd yapi-mcp-server

# Configure Yapi URL and project tokens
vim src/main/resources/application-mcp.yml

# Build the project
mvn clean package

# Run the application
java -jar target/yapi-mcp-server-0.0.1-SNAPSHOT.jar
```

### Method 2: Run with Docker

```bash
# Clone the repository
git clone https://github.com/yyykf/yapi-mcp-server.git
cd yapi-mcp-server

# Configure Yapi URL and project tokens
vim src/main/resources/application-mcp.yml

# Run with Docker Compose
docker-compose up -d
```

## Configuration

Before running the application, you need to configure your Yapi connection settings in `src/main/resources/application-mcp.yml`:

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          yapiClient:
            url: http://yapi.com # Replace with your Yapi URL
yapi:
  project-tokens:
    123456: "your-project-token-1"  # Project ID: Token
    789012: "your-project-token-2"  # Add more projects as needed
```

## Building Multi-Architecture Docker Images

If you need to build and push multi-architecture Docker images manually, use the provided script:

```bash
./build-and-push.sh <version> <docker-username> <docker-password>
```

Example:
```bash
./build-and-push.sh 1.0.0 myusername mypassword
```

This script will build images for both `amd64` and `arm64` architectures and create a multi-architecture manifest.

## Documentation

For more detailed project documentation and insights, you can explore the project using DeepWiki:

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/yyykf/yapi-mcp-server)

## Integration with Cursor

### Method 1: One-Click Install

Click the button below to automatically install the MCP server in Cursor:

[![Install MCP Server](https://cursor.com/deeplink/mcp-install-dark.svg)](https://cursor.com/install-mcp?name=yapi&config=eyJ1cmwiOiJodHRwOi8vMTI3LjAuMC4xOjg4ODgvc3NlIn0%3D)

### Method 2: Manual Configuration

Add the following configuration to your Cursor MCP settings file (`mcp.json`):

```json
{
  "mcpServers": {
    "yapi": {
      "url": "http://127.0.0.1:8888/sse"
    }
  }
}
```

## Health Check

Once the application is running, you can check its health status:

```bash
curl http://localhost:8888/actuator/health
```

Or visit `http://localhost:8888/actuator/health` in your browser.

## Usage Examples

After setting up the MCP server in Cursor, you can use natural language to interact with your Yapi instance:

- "Show me all available projects"
- "List all API categories in project 123456"
- "Search for login APIs"
- "Get details of interface ID 789"
- "What's the cache status?"

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
