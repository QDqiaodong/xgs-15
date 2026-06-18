#!/bin/bash

set -e

echo "======================================"
echo " 启动商业园区物业公共设施维保资产台账系统"
echo "======================================"
echo ""

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

source .env

echo "检查端口占用情况..."

check_port() {
  local port=$1
  local name=$2
  local pid=$(lsof -nP -iTCP:"$port" -sTCP:LISTEN -t 2>/dev/null || true)
  if [ -n "$pid" ]; then
    local cmd=$(ps -p "$pid" -o command= 2>/dev/null || echo "unknown")
    echo "错误: $name 端口 $port 已被占用!"
    echo "占用进程 PID: $pid"
    echo "占用进程命令: $cmd"
    echo "请使用以下命令查看占用进程:"
    echo "  lsof -nP -iTCP:$port -sTCP:LISTEN"
    echo ""
    echo "如需释放端口，请执行:"
    echo "  kill -9 $pid"
    exit 1
  fi
}

check_port "$FRONTEND_PORT" "前端"
check_port "$BACKEND_PORT" "后端"
check_port "$MYSQL_PORT" "MySQL"
check_port "$REDIS_PORT" "Redis"

echo "端口检查通过，开始启动服务..."
echo ""

docker-compose up -d --build

echo ""
echo "等待服务启动..."
sleep 15

echo ""
echo "======================================"
echo " 服务启动完成!"
echo "======================================"
echo ""
echo "前端访问地址: http://localhost:${FRONTEND_PORT}"
echo "后端API地址:  http://localhost:${BACKEND_PORT}"
echo "MySQL端口:    127.0.0.1:${MYSQL_PORT}"
echo "Redis端口:    127.0.0.1:${REDIS_PORT}"
echo ""
echo "验证服务状态:"
echo "--------------------------------------"

sleep 5

if curl -s http://127.0.0.1:${FRONTEND_PORT} > /dev/null 2>&1; then
  echo "✓ 前端服务正常 (http://127.0.0.1:${FRONTEND_PORT})"
else
  echo "✗ 前端服务异常 (http://127.0.0.1:${FRONTEND_PORT})"
fi

if curl -s http://localhost:${FRONTEND_PORT} > /dev/null 2>&1; then
  echo "✓ 前端服务正常 (http://localhost:${FRONTEND_PORT})"
else
  echo "✗ 前端服务异常 (http://localhost:${FRONTEND_PORT})"
fi

if curl -s http://127.0.0.1:${BACKEND_PORT}/api/facilities > /dev/null 2>&1; then
  echo "✓ 后端服务正常 (http://127.0.0.1:${BACKEND_PORT})"
else
  echo "✗ 后端服务异常 (http://127.0.0.1:${BACKEND_PORT})"
fi

echo ""
echo "======================================"