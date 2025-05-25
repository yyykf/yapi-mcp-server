#!/bin/bash

# 网络连接测试脚本

echo "🔍 网络连接诊断工具"
echo "===================="

# 测试基本网络连接
echo "1. 测试基本网络连接..."
if ping -c 1 8.8.8.8 &> /dev/null; then
    echo "✅ 基本网络连接正常"
else
    echo "❌ 基本网络连接失败"
    exit 1
fi

# 测试 DNS 解析
echo "2. 测试 DNS 解析..."
if nslookup maven.aliyun.com &> /dev/null; then
    echo "✅ DNS 解析正常"
else
    echo "❌ DNS 解析失败"
    echo "💡 建议：检查 DNS 设置或使用其他 DNS 服务器"
fi

# 测试 Maven 中央仓库
echo "3. 测试 Maven 中央仓库..."
if curl -s --connect-timeout 5 https://repo.maven.apache.org/maven2/ &> /dev/null; then
    echo "✅ Maven 中央仓库可访问"
else
    echo "❌ Maven 中央仓库不可访问"
    echo "💡 建议：使用阿里云镜像源"
fi

# 测试阿里云 Maven 镜像
echo "4. 测试阿里云 Maven 镜像..."
if curl -s --connect-timeout 5 https://maven.aliyun.com/repository/central/ &> /dev/null; then
    echo "✅ 阿里云 Maven 镜像可访问"
else
    echo "❌ 阿里云 Maven 镜像不可访问"
    echo "💡 建议：使用本地构建模式"
fi

# 测试 Docker Hub
echo "5. 测试 Docker Hub..."
if curl -s --connect-timeout 5 https://hub.docker.com &> /dev/null; then
    echo "✅ Docker Hub 可访问"
else
    echo "❌ Docker Hub 不可访问"
    echo "💡 建议：配置 Docker 镜像加速器"
fi

echo ""
echo "🎯 建议："
echo "- 如果所有测试都通过，可以使用在线构建模式"
echo "- 如果 Maven 仓库不可访问，建议使用本地构建模式"
echo "- 如果 Docker Hub 不可访问，建议配置 Docker 镜像加速器"
echo ""
echo "📝 使用方法："
echo "- 在线构建：./docker-start.sh 或 docker-start.bat"
echo "- 本地构建：先运行 mvn clean package，再使用启动脚本选择本地构建模式" 