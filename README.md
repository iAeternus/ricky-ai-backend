# Ricky AI Backend

一个基于 Spring Boot 3 + WebFlux 的后端服务，提供：
- 用户注册/登录/刷新/登出
- AI 模型配置管理
- SSE 流式聊天
- 文件上传

## 技术栈
- Java 17
- Spring Boot 3.5.x6
- Spring WebFlux
- Spring Security + JWT
- Spring Data R2DBC
- PostgreSQL + Flyway
- Redis（Reactive）

## 运行环境
- JDK 17+
- Maven 3.9+
- PostgreSQL 14+
- Redis 6+

## 快速启动
1. 创建数据库（示例：`ricky_ai`）。
2. 启动 PostgreSQL 和 Redis。
3. 配置环境变量（见下文）。
4. 启动项目：

```bash
mvn spring-boot:run
```

默认端口 `8080`。

## 配置说明
项目使用 `src/main/resources/application.yml`，支持环境变量覆盖：

- `R2DBC_URL`（默认：`r2dbc:postgresql://127.0.0.1:5432/ricky_ai`）
- `DB_USER`（默认：`ricky`）
- `DB_PASSWORD`（默认：`ricky_pass`）
- `FLYWAY_URL`（默认：`jdbc:postgresql://127.0.0.1:5432/ricky_ai`）
- `REDIS_HOST`（默认：`127.0.0.1`）
- `REDIS_PORT`（默认：`6379`）

安全相关：
- `security.jwt.secret`：JWT 密钥（生产环境务必修改）
- `security.jwt.access-token-ttl`：AccessToken 过期时间
- `security.jwt.refresh-token-ttl`：RefreshToken 过期时间

## 接口概览
- 认证
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - `POST /api/auth/refresh`
  - `POST /api/auth/logout`
- 用户
  - `GET /api/users/me`
  - `PUT /api/users/me`
- 模型
  - `GET /api/models`
  - `GET /api/models/{id}`
  - `POST /api/models`
  - `PUT /api/models/{id}`
- 聊天
  - `POST /api/chat/stream`（SSE）
  - `POST /api/chat/cancel/{requestId}`
- 文件
  - `POST /api/files/upload`

鉴权方式：
- 在请求头中携带 `Authorization: Bearer <accessToken>`。

API 文档：
- `/api/swagger`
- `/api/docs`

## 开发常用命令
```bash
mvn clean
mvn test
mvn -DskipTests compile
```

## 目录结构
```text
src/main/java/com/ricky
├─ auth      # 登录、注册、刷新、登出
├─ user      # 用户资料
├─ model     # 模型配置管理
├─ chat      # 流式聊天
├─ file      # 文件上传
├─ ai        # Provider 网关与协议适配
└─ common    # 安全、异常、统一响应
```

## 说明
- 数据库结构由 Flyway 自动迁移（`db/migration/V1__init.sql`）。
- `ai_models.extra_config` 是 `jsonb` 字段，传参时请使用 JSON 对象。
