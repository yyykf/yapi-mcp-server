#!/bin/bash

# Yapi MCP Server Docker 启动脚本

set -e

echo "🚀 启动 Yapi MCP Server..."

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ 错误：Docker 未安装，请先安装 Docker"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo "❌ 错误：Docker Compose 未安装，请先安装 Docker Compose"
    exit 1
fi

# 创建日志目录
mkdir -p logs

# 检查环境变量文件
if [ ! -f "docker.env" ]; then
    echo "⚠️  警告：未找到 docker.env 文件，将使用默认配置"
    echo "💡 提示：请复制 docker.env.example 为 docker.env 并修改相应配置"
    echo ""
fi

# 检查网络连接
echo "🔍 检查网络连接..."
if ! ping -c 1 maven.aliyun.com &> /dev/null; then
    echo "⚠️  警告：无法连接到阿里云 Maven 镜像源"
    echo "💡 建议：使用本地构建模式"
    
    # 询问用户是否使用本地构建
    read -p "是否使用本地构建模式？(需要先本地构建 JAR 文件) [y/N]: " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        USE_LOCAL_BUILD=true
    else
        echo "继续尝试在线构建..."
        USE_LOCAL_BUILD=false
    fi
else
    echo "✅ 网络连接正常"
    USE_LOCAL_BUILD=false
fi

# 根据选择使用不同的构建方式
if [ "$USE_LOCAL_BUILD" = true ]; then
    echo "📦 使用本地构建模式..."
    
    # 检查是否存在 JAR 文件
    if [ ! -f target/*.jar ]; then
        echo "🔨 本地构建 JAR 文件..."
        if command -v mvn &> /dev/null; then
            mvn clean package -DskipTests
        elif [ -f "./mvnw" ]; then
            ./mvnw clean package -DskipTests
        else
            echo "❌ 错误：未找到 Maven 或 Maven Wrapper"
            echo "请先安装 Maven 或确保 mvnw 文件存在"
            exit 1
        fi
    fi
    
    # 使用本地构建的 Docker Compose 文件
    COMPOSE_FILE="docker-compose.local.yml"
else
    echo "🌐 使用在线构建模式..."
    COMPOSE_FILE="docker-compose.yml"
fi

# 构建并启动服务
echo "🔨 构建 Docker 镜像..."
if command -v docker-compose &> /dev/null; then
    docker-compose -f $COMPOSE_FILE build
    echo "🎯 启动服务..."
    docker-compose -f $COMPOSE_FILE up -d
else
    docker compose -f $COMPOSE_FILE build
    echo "🎯 启动服务..."
    docker compose -f $COMPOSE_FILE up -d
fi

echo ""
echo "✅ Yapi MCP Server 启动成功！"
echo ""
echo "📋 服务信息："
echo "   - 服务地址: http://localhost:8888"
echo "   - 健康检查: http://localhost:8888/actuator/health"
echo "   - 日志目录: ./logs"
echo ""
echo "📝 常用命令："
echo "   - 查看日志: docker logs yapi-mcp-server"
echo "   - 停止服务: docker-compose down 或 docker compose down"
echo "   - 重启服务: docker-compose restart 或 docker compose restart"
echo ""
echo "🎉 享受使用 Yapi MCP Server！" 