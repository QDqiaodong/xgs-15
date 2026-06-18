# 商业园区物业公共设施维保资产台账系统

## 项目简介

本系统是一个商业园区物业公共设施维保资产台账管理系统，用于管理电梯、消防设备、水泵、照明、监控等公共设施，记录月度巡检、故障维修、配件更换、特种设备年检台账，满足消防、特种设备年检监管要求。

## 技术栈

- **前端**: Vue3 + Vite + Element Plus
- **后端**: Spring Boot 3.3 + JDK 17 + Spring Data JPA
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.2
- **容器化**: Docker + Docker Compose

## 核心功能模块

### 1. 园区公共设施建档
- 录入电梯、消防柜、水泵、监控编号、安装楼栋、法定年检周期
- 设施分类管理

### 2. 月度例行巡检登记
- 按楼栋分区登记设施外观、运行状态、安全部件巡检结果
- 标记异常项
- 巡检清单批量勾选录入

### 3. 设施故障维修台账
- 记录设备故障现象、维修日期、更换配件、完工验收记录

### 4. 特种设备年检归档
- 登记电梯、压力容器年检执行日期、检测机构、年检合格归档记录
- 年检周期自动提醒

## 项目结构

```
xgs-15/
├── .env                    # 环境变量配置（端口、密码等）
├── .dockerignore           # Docker构建忽略文件
├── docker-compose.yml      # Docker Compose编排配置
├── start.sh                # 一键启动脚本
├── README.md               # 项目说明文档
├── backend/                # 后端Spring Boot项目
│   ├── Dockerfile          # 后端Docker构建配置
│   ├── pom.xml             # Maven依赖配置
│   └── src/main/           # 后端源码
└── frontend/               # 前端Vue3项目
    ├── Dockerfile          # 前端Docker构建配置
    ├── nginx.conf          # Nginx配置
    ├── package.json        # npm依赖配置
    ├── package-lock.json   # npm依赖锁定文件
    ├── vite.config.js      # Vite配置
    └── src/                # 前端源码
```

## 环境要求

- Docker 20.10+
- Docker Compose 2.0+

## 快速开始

### 1. 启动项目

```bash
# 方式一：使用启动脚本（推荐，含端口冲突检测）
chmod +x start.sh
./start.sh

# 方式二：直接使用Docker Compose
docker-compose up -d --build
```

### 2. 访问地址

| 服务 | 地址 |
|------|------|
| 前端系统 | http://localhost:18115 |
| 后端API | http://localhost:19115 |
| MySQL | 127.0.0.1:33115 |
| Redis | 127.0.0.1:63115 |

### 3. 停止服务

```bash
docker-compose down
```

## 端口配置

所有端口配置在 `.env` 文件中统一管理，修改后重新启动即可生效：

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| FRONTEND_PORT | 18115 | 前端访问端口 |
| BACKEND_PORT | 19115 | 后端API端口 |
| MYSQL_PORT | 33115 | MySQL数据库端口 |
| REDIS_PORT | 63115 | Redis缓存端口 |

### 端口绑定规则

- 所有服务端口仅绑定 `127.0.0.1`，不暴露到外网
- 端口映射格式: `127.0.0.1:${PORT}:内部端口`
- `http://localhost:PORT` 和 `http://127.0.0.1:PORT` 访问结果一致

## Docker优化说明

### 分层缓存机制

1. **前端**: 先 COPY `package.json`/`package-lock.json`，执行 `npm ci`，再 COPY 源码执行 `npm run build`
2. **后端**: 先 COPY `pom.xml`，执行 `mvn dependency:go-offline`，再 COPY `src` 编译

### 国内镜像源

- **npm**: 腾讯云镜像 (https://mirrors.cloud.tencent.com/npm/)
- **Maven**: 阿里云镜像 (https://maven.aliyun.com/repository/public)
- **Docker基础镜像**: 通过 `DOCKER_REGISTRY` 环境变量统一管理

### 构建缓存策略

- 首次构建：全量下载依赖包
- 后续构建：
  - 若 `pom.xml`/`package.json`/`package-lock.json` 无变更，复用构建缓存
  - 仅业务源代码修改时，只执行重新编译，不触发依赖下载

### .dockerignore 排除规则

排除 `node_modules`、`dist`、`target`、日志、临时文件、IDE配置等，减少构建上下文传输量。

## 数据库配置

| 配置项 | 默认值 |
|--------|--------|
| 数据库名 | facility_db |
| 用户名 | admin |
| 密码 | admin123 |
| 端口 | 33115 |

## API接口

### 设施管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/facilities | 获取设施列表 |
| GET | /api/facilities/{id} | 获取设施详情 |
| POST | /api/facilities | 新增设施 |
| PUT | /api/facilities/{id} | 更新设施 |
| DELETE | /api/facilities/{id} | 删除设施 |

### 巡检管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/inspections | 获取巡检记录列表 |
| POST | /api/inspections | 新增巡检记录 |
| PUT | /api/inspections/{id} | 更新巡检记录 |

### 维修管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/maintenance | 获取维修记录列表 |
| POST | /api/maintenance | 新增维修记录 |

### 年检管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/annual-inspections | 获取年检记录列表 |
| POST | /api/annual-inspections | 新增年检记录 |

## 容器名称

| 服务 | 容器名称 |
|------|----------|
| MySQL | xgs-15-mysql |
| Redis | xgs-15-redis |
| 后端 | xgs-15-backend |
| 前端 | xgs-15-frontend |

## 注意事项

1. **端口冲突**: 启动脚本自动检测端口占用，如有冲突会提示占用进程信息
2. **首次构建**: 首次构建可能需要较长时间下载依赖
3. **数据持久化**: MySQL和Redis数据分别存储在docker卷中，重启容器数据不会丢失
4. **镜像加速**: 所有Docker基础镜像通过 `DOCKER_REGISTRY` 变量统一管理，可切换国内镜像源

## 许可证

MIT License
