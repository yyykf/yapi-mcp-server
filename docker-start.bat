@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo 🚀 启动 Yapi MCP Server...
echo.

REM 检查 Docker 是否安装
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ 错误：Docker 未安装，请先安装 Docker Desktop
    pause
    exit /b 1
)

REM 检查 Docker Compose 是否可用
docker-compose --version >nul 2>&1
if errorlevel 1 (
    docker compose version >nul 2>&1
    if errorlevel 1 (
        echo ❌ 错误：Docker Compose 未安装，请先安装 Docker Compose
        pause
        exit /b 1
    )
    set COMPOSE_CMD=docker compose
) else (
    set COMPOSE_CMD=docker-compose
)

REM 创建日志目录
if not exist "logs" mkdir logs

REM 检查环境变量文件
if not exist "docker.env" (
    echo ⚠️  警告：未找到 docker.env 文件，将使用默认配置
    echo 💡 提示：请复制 docker.env.example 为 docker.env 并修改相应配置
    echo.
)

REM 检查网络连接
echo 🔍 检查网络连接...
ping -n 1 maven.aliyun.com >nul 2>&1
if errorlevel 1 (
    echo ⚠️  警告：无法连接到阿里云 Maven 镜像源
    echo 💡 建议：使用本地构建模式
    echo.
    set /p choice="是否使用本地构建模式？(需要先本地构建 JAR 文件) [y/N]: "
    if /i "!choice!"=="y" (
        set USE_LOCAL_BUILD=true
        set COMPOSE_FILE=docker-compose.local.yml
    ) else (
        echo 继续尝试在线构建...
        set USE_LOCAL_BUILD=false
        set COMPOSE_FILE=docker-compose.yml
    )
) else (
    echo ✅ 网络连接正常
    set USE_LOCAL_BUILD=false
    set COMPOSE_FILE=docker-compose.yml
)

REM 如果使用本地构建，检查 JAR 文件
if "!USE_LOCAL_BUILD!"=="true" (
    echo 📦 使用本地构建模式...
    if not exist "target\*.jar" (
        echo 🔨 本地构建 JAR 文件...
        if exist "mvnw.cmd" (
            call mvnw.cmd clean package -DskipTests
        ) else (
            mvn clean package -DskipTests
        )
        if errorlevel 1 (
            echo ❌ 本地构建失败
            pause
            exit /b 1
        )
    )
) else (
    echo 🌐 使用在线构建模式...
)

REM 构建并启动服务
echo 🔨 构建 Docker 镜像...
%COMPOSE_CMD% -f !COMPOSE_FILE! build
if errorlevel 1 (
    echo ❌ 构建失败
    pause
    exit /b 1
)

echo 🎯 启动服务...
%COMPOSE_CMD% -f !COMPOSE_FILE! up -d
if errorlevel 1 (
    echo ❌ 启动失败
    pause
    exit /b 1
)

echo.
echo ✅ Yapi MCP Server 启动成功！
echo.
echo 📋 服务信息：
echo    - 服务地址: http://localhost:8888
echo    - 健康检查: http://localhost:8888/actuator/health
echo    - 日志目录: ./logs
echo.
echo 📝 常用命令：
echo    - 查看日志: docker logs yapi-mcp-server
echo    - 停止服务: %COMPOSE_CMD% down
echo    - 重启服务: %COMPOSE_CMD% restart
echo.
echo 🎉 享受使用 Yapi MCP Server！
echo.
pause 