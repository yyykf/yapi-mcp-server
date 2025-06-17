#!/bin/bash

set -e

# 参数解析
VERSION=$1
DOCKER_USER=$2
DOCKER_PASS=$3

if [ -z "$VERSION" ] || [ -z "$DOCKER_USER" ] || [ -z "$DOCKER_PASS" ]; then
  echo "❗ 用法: $0 <version> <docker-username> <docker-password>"
  exit 1
fi

IMAGE_NAME="yukaifan/yapi-mcp-server"
ARCHES=("amd64" "arm64")

# 分别构建并推送各架构镜像
for ARCH in "${ARCHES[@]}"; do
  echo "🚀 构建并推送: $IMAGE_NAME:${VERSION}-${ARCH} (linux/${ARCH})"

  mvn -Pnative clean spring-boot:build-image \
    -Ddocker.image.name=${IMAGE_NAME}:${VERSION}-${ARCH} \
    -Dspring-boot.build-image.publish=true \
    -Ddocker.image.platform=linux/${ARCH} \
    -Ddocker.publishRegistry.username=${DOCKER_USER} \
    -Ddocker.publishRegistry.password=${DOCKER_PASS}
done

# 创建并推送 multi-arch manifest
echo "🔗 创建并推送 multi-arch manifest: $IMAGE_NAME:${VERSION}"

docker manifest create ${IMAGE_NAME}:${VERSION} \
  ${IMAGE_NAME}:${VERSION}-amd64 \
  ${IMAGE_NAME}:${VERSION}-arm64

docker manifest push ${IMAGE_NAME}:${VERSION}

# 可选 latest
echo "🎯 创建并推送 latest multi-arch manifest"

docker manifest create ${IMAGE_NAME}:latest \
  ${IMAGE_NAME}:${VERSION}-amd64 \
  ${IMAGE_NAME}:${VERSION}-arm64

docker manifest push ${IMAGE_NAME}:latest

echo "✅ 完成: $IMAGE_NAME:${VERSION} + latest 已推送多架构镜像"
