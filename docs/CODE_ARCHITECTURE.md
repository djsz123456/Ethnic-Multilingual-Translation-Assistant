# 雪域安多·藏韵传承 — 代码架构文档

## 一、项目概述

本项目为安多藏族文化传播平台，采用前后端分离架构，前端使用 Vue 3 + Vite 构建 SPA 应用，后端使用 Spring Boot 3 提供 RESTful API，数据库采用 PostgreSQL 16，通过 Docker Compose 实现容器化部署。

---

## 二、系统架构图

```
┌─────────────────────────────────────────────────────────────┐
│                        用户浏览器                            │
│                  http://localhost                            │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│              Nginx (frontend 容器, :80)                      │
│  ┌─────────────────────────┐  ┌──────────────────────────┐  │
│  │  静态资源 (Vue SPA)      │  │  /api/* 反向代理          │  │
│  │  try_files → index.html │  │  proxy_pass → backend    │  │
│  └─────────────────────────┘  └──────────┬───────────────┘  │
└──────────────────────────────────────────┼──────────────────┘
                                           │
                                           ▼
┌─────────────────────────────────────────────────────────────┐
│          Spring Boot (backend 容器, :8080)                    │
│  ┌──────────┐ ┌───────────┐ ┌──────────┐ ┌──────────────┐  │
│  │Controller│→│  Service  │→│Repository│→│   JPA/       │  │
│  │  (REST)  │ │  (业务)   │ │ (数据访问)│ │  Hibernate   │  │
│  └──────────┘ └───────────┘ └──────────┘ └──────┬───────┘  │
│  ┌──────────┐ ┌───────────┐                      │          │
│  │JWT Filter│ │  Security │                      │          │
│  └──────────┘ └───────────┘                      │          │
└──────────────────────────────────────────────────┼──────────┘
                                                   │
                                                   ▼
┌─────────────────────────────────────────────────────────────┐
│              PostgreSQL 16 (db 容器, :5432)                   │
│                   数据库: tibetan_platform                    │
│  ┌────────┐ ┌───────────┐ ┌───────┐ ┌───────┐ ┌─────────┐ │
│  │ users  │ │tour_spots │ │ music │ │ notes │ │favorites│ │
│  └────────┘ └───────────┘ └───────┘ └───────┘ └─────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## 三、E-R 图（实体关系图）

```
┌──────────────────┐
│      users       │
├──────────────────┤
│ PK id            │──────────────────────────────────┐
│    username      │                                   │
│    password_hash │                                   │
│    nickname      │                                   │
│    created_at    │                                   │
│    updated_at    │                                   │
└──────────────────┘                                   │
         │                                             │
         │ 1:N                                         │ 1:N
         │                                             │
         ▼                                             ▼
┌──────────────────┐                          ┌──────────────────┐
│      notes       │                          │    favorites     │
├──────────────────┤                          ├──────────────────┤
│ PK id            │                          │ PK id            │
│ FK user_id       │──→ users.id              │ FK user_id       │──→ users.id
│    title         │                          │    item_type     │
│    content       │                          │    item_id       │
│    created_at    │                          │    item_name     │
│    updated_at    │                          │    created_at    │
└──────────────────┘                          └──────────────────┘
                                                       │
                                              item_type + item_id
                                              多态关联 ──→ tour_spots / folk_cultures / music
                                                       │
         ┌─────────────────────────────────────────────┤
         │                          │                  │
         ▼                          ▼                  ▼
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│   tour_spots     │  │  folk_cultures   │  │      music       │
├──────────────────┤  ├──────────────────┤  ├──────────────────┤
│ PK id            │  │ PK id            │  │ PK id            │
│    name          │  │    title         │  │    title         │
│    region        │  │    subtitle      │  │    artist        │
│    address       │  │    category      │  │    category      │
│    description   │  │    description   │  │    duration      │
│    image_url     │  │    image_url     │  │    cover_url     │
│    link_url      │  │    sort_order    │  │    audio_url     │
│    latitude      │  │    created_at    │  │    created_at    │
│    longitude     │  └──────────────────┘  └──────────────────┘
│    sort_order    │
│    created_at    │
└──────────────────┘

┌──────────────────┐              ┌──────────────────┐
│   scriptures     │              │  browse_history   │
├──────────────────┤              ├──────────────────┤
│ PK id            │              │ PK id            │
│    tibetan_text  │              │ FK user_id       │──→ users.id
│    chinese_      │              │    item_type     │
│    translation   │              │    item_id       │
│    sort_order    │              │    item_name     │
│    scripture_name│              │    visited_at    │
└──────────────────┘              └──────────────────┘
```

### 实体关系说明

| 关系 | 类型 | 说明 |
|------|------|------|
| users → notes | 1:N | 一个用户拥有多条笔记，删除用户级联删除笔记 |
| users → favorites | 1:N | 一个用户拥有多条收藏，通过 (user_id, item_type, item_id) 唯一约束防重 |
| users → browse_history | 1:N | 一个用户拥有多条浏览记录 |
| favorites → tour_spots/folk_cultures/music | 多态 | item_type 区分收藏类型（spot/folk/music），item_id 指向对应表的主键 |
| tour_spots | 独立 | 按 region 分组（qinghai/gannan/chuanxi/zangbei） |
| folk_cultures | 独立 | 按 category 分类（非遗/传统节日/宗教习俗等） |
| music | 独立 | 按 category 分类（安多民歌/曼陀铃弹唱/器乐独奏/酒歌） |
| scriptures | 独立 | 经文数据，按 sort_order 排序 |

---

## 四、前端架构

### 4.1 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.5 | 前端框架（Composition API + SFC） |
| Vite | 8.0 | 构建工具 |
| TypeScript | 6.0 | 类型安全 |
| Vue Router | 4.6 | SPA 路由 |
| Pinia | 3.0 | 状态管理 |
| Axios | 1.15 | HTTP 客户端 |
| SCSS | 1.99 | CSS 预处理器 |
| Font Awesome | 4.7 | 图标库（CDN） |

### 4.2 目录结构

```
frontend/
├── index.html                    # Vite 入口（含 CDN: Font Awesome + Google Fonts）
├── vite.config.ts                # Vite 配置（@ 别名, /api 代理）
├── tsconfig.app.json             # TypeScript 配置（@ 路径映射）
├── package.json                  # 依赖管理
├── Dockerfile                    # 生产构建（Node → Nginx）
├── nginx.conf                    # Nginx 配置（SPA history + API 反代）
├── public/
│   ├── images/                   # 静态图片资源
│   └── music/movies/             # 视频资源
└── src/
    ├── main.ts                   # 应用入口（createApp + Pinia + Router）
    ├── App.vue                   # 根组件（Header + router-view + Footer）
    ├── router/index.ts           # 路由配置（10 条路由，懒加载）
    ├── stores/user.ts            # Pinia 用户状态（认证、登录、注销）
    ├── api/request.ts            # Axios 实例（JWT 拦截器、401 处理）
    ├── types/
    │   ├── spot.ts               # TourSpot 接口定义
    │   └── folk.ts               # FolkCulture 接口定义
    ├── components/
    │   ├── layout/
    │   │   ├── AppHeader.vue     # 统一导航栏（藏文 Logo + 导航 + 用户菜单）
    │   │   └── AppFooter.vue     # 统一页脚
    │   └── common/
    │       └── BackToTop.vue     # 返回顶部按钮
    ├── views/
    │   ├── HomeView.vue          # 首页（轮播图 + 民俗卡片 + 旅游卡片 + 工具入口）
    │   ├── SpotDetailView.vue    # 景点详情（6 个景点，卡片网格布局）
    │   ├── FolkDetailView.vue    # 民俗详情（6 个民俗，段落式内容）
    │   ├── MusicView.vue         # 藏族音乐（分类筛选 + 播放控制 + 视频展示）
    │   ├── ScriptureView.vue     # 藏文经典（二十一度母礼赞，点击展开翻译）
    │   ├── TranslateView.vue     # 藏汉翻译（MyMemory API，双向切换）
    │   ├── CalendarView.vue      # 藏历查询（功能介绍 + 外链完整工具）
    │   ├── NoteView.vue          # 藏文笔记（侧边栏列表 + 编辑器，localStorage）
    │   ├── PhraseView.vue        # 藏语短语（33 条，6 类，搜索/收藏/语音）
    │   └── KeyboardView.vue      # 藏文键盘（虚拟键盘 + 复制功能）
    └── assets/styles/
        ├── variables.scss        # CSS 变量（设计令牌）
        └── global.scss           # 全局重置样式
```

### 4.3 路由表

| 路由 | 视图组件 | 功能 | 是否需要认证 |
|------|----------|------|:---:|
| `/` | HomeView | 首页 | 否 |
| `/spots/:id` | SpotDetailView | 景点详情 | 否 |
| `/folk/:id` | FolkDetailView | 民俗详情 | 否 |
| `/music` | MusicView | 藏族音乐 | 否 |
| `/scripture` | ScriptureView | 藏文经典 | 否 |
| `/translate` | TranslateView | 藏汉翻译 | 否 |
| `/calendar` | CalendarView | 藏历查询 | 否 |
| `/notes` | NoteView | 藏文笔记 | 是 |
| `/phrases` | PhraseView | 藏语短语 | 否 |
| `/keyboard` | KeyboardView | 藏文键盘 | 否 |

### 4.4 前端数据流

```
用户操作 → Vue 组件（View）
              │
              ├── 读取/修改状态 → Pinia Store（user.ts）
              │                        │
              │                        └── localStorage（认证 Token）
              │
              └── API 请求 → Axios（request.ts）
                                  │
                                  ├── 自动附加 JWT Token
                                  │
                                  └── /api/* → Vite Proxy → Spring Boot
```

---

## 五、后端架构

### 5.1 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.4.x | 后端框架 |
| Spring Data JPA | - | ORM 数据访问 |
| Spring Security | - | 认证授权 |
| PostgreSQL | 16 | 关系数据库 |
| Flyway | - | 数据库版本迁移 |
| JJWT | 0.12.6 | JWT Token 生成与验证 |
| Lombok | - | 代码简化 |
| Maven | 3.9 | 构建工具 |

### 5.2 目录结构

```
backend/
├── pom.xml                                    # Maven 依赖配置
├── Dockerfile                                 # 多阶段构建（Maven → JRE Alpine）
└── src/main/
    ├── java/com/tibetan/platform/
    │   ├── TibetanPlatformApplication.java    # Spring Boot 启动类
    │   ├── config/
    │   │   ├── SecurityConfig.java            # Spring Security 配置（JWT 过滤链）
    │   │   ├── CorsConfig.java                # CORS 跨域配置
    │   │   ├── JwtUtil.java                   # JWT 工具类（生成/验证/解析）
    │   │   └── JwtAuthFilter.java             # JWT 认证过滤器（OncePerRequestFilter）
    │   ├── entity/
    │   │   ├── User.java                      # 用户实体
    │   │   ├── TourSpot.java                  # 景点实体
    │   │   ├── FolkCulture.java               # 民俗文化实体
    │   │   ├── Music.java                     # 音乐实体
    │   │   ├── Note.java                      # 笔记实体
    │   │   └── Favorite.java                  # 收藏实体
    │   ├── repository/
    │   │   ├── UserRepository.java            # 用户数据访问
    │   │   ├── SpotRepository.java            # 景点数据访问
    │   │   ├── FolkRepository.java            # 民俗数据访问
    │   │   ├── MusicRepository.java           # 音乐数据访问
    │   │   ├── NoteRepository.java            # 笔记数据访问
    │   │   └── FavoriteRepository.java        # 收藏数据访问
    │   ├── controller/
    │   │   ├── AuthController.java            # 认证接口（注册/登录）
    │   │   ├── SpotController.java            # 景点接口（列表/详情）
    │   │   ├── FolkController.java            # 民俗接口（列表/详情）
    │   │   ├── MusicController.java           # 音乐接口（列表）
    │   │   ├── NoteController.java            # 笔记接口（CRUD）
    │   │   ├── FavoriteController.java        # 收藏接口（增删查）
    │   │   └── TranslateController.java       # 翻译代理接口
    │   └── dto/
    │       ├── ApiResponse.java               # 统一响应格式
    │       └── AuthRequest.java               # 认证请求 DTO
    └── resources/
        ├── application.yml                    # 应用配置
        └── db/migration/
            └── V1__init_schema.sql            # Flyway 数据库迁移脚本
```

### 5.3 API 接口总览

#### 认证接口（无需 Token）

| 方法 | 路径 | 说明 | 请求体 | 响应 |
|------|------|------|--------|------|
| POST | `/api/auth/register` | 用户注册 | `{ username, password }` | `{ token, username, userId }` |
| POST | `/api/auth/login` | 用户登录 | `{ username, password }` | `{ token, username, userId }` |

#### 公开接口（无需 Token）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/spots` | 景点列表（可选 `?region=qinghai`） |
| GET | `/api/spots/{id}` | 景点详情 |
| GET | `/api/folk` | 民俗列表 |
| GET | `/api/folk/{id}` | 民俗详情 |
| GET | `/api/music` | 音乐列表（可选 `?category=安多民歌`） |
| POST | `/api/translate` | 翻译代理（`{ text, from, to }`） |

#### 需认证接口（Header: `Authorization: Bearer <token>`）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/notes` | 获取当前用户笔记列表 |
| POST | `/api/notes` | 创建笔记 |
| PUT | `/api/notes/{id}` | 更新笔记（校验所有权） |
| DELETE | `/api/notes/{id}` | 删除笔记（校验所有权） |
| GET | `/api/favorites` | 获取收藏列表 |
| POST | `/api/favorites` | 添加收藏 |
| DELETE | `/api/favorites?itemType=spot&itemId=1` | 取消收藏 |

#### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 5.4 认证流程

```
注册/登录请求 → AuthController
                    │
                    ├── 验证用户名/密码（BCrypt）
                    │
                    └── 生成 JWT Token（JwtUtil）
                              │
                              ▼
                    返回 { token, username, userId }
                              │
                              ▼
    前端存储 token → localStorage → Axios 拦截器自动附加
                              │
                              ▼
    后续请求 → JwtAuthFilter → 验证 Token → 填充 SecurityContext
                              │
                              ▼
               Controller 通过 SecurityContext 获取当前用户 ID
```

### 5.5 后端分层架构

```
HTTP 请求
    │
    ▼
┌─── JwtAuthFilter ───┐     解析 JWT，填充 SecurityContext
│                      │
├─── SecurityConfig ───┤     路由级权限控制（permitAll / authenticated）
│                      │
├─── CorsConfig ───────┤     跨域策略
│                      │
    ▼
┌─── Controller ───────┐     接收请求，参数验证，调用 Repository
│   AuthController     │
│   SpotController     │
│   FolkController     │
│   MusicController    │
│   NoteController     │
│   FavoriteController │
│   TranslateController│
└──────────┬───────────┘
           │
           ▼
┌─── Repository ───────┐     Spring Data JPA 接口，自动生成 SQL
│   UserRepository     │
│   SpotRepository     │
│   FolkRepository     │
│   MusicRepository    │
│   NoteRepository     │
│   FavoriteRepository │
└──────────┬───────────┘
           │
           ▼
┌─── Entity (JPA) ─────┐     ORM 映射，对应 PostgreSQL 表
│   User               │
│   TourSpot           │
│   FolkCulture        │
│   Music              │
│   Note               │
│   Favorite           │
└──────────┬───────────┘
           │
           ▼
    PostgreSQL 数据库
```

---

## 六、数据库设计

### 6.1 表结构总览

| 表名 | 说明 | 行数（预估） |
|------|------|:---:|
| users | 用户表 | 动态增长 |
| tour_spots | 景点表 | ~35 |
| folk_cultures | 民俗文化表 | ~6 |
| music | 音乐表 | ~4 |
| scriptures | 经文表 | ~27 |
| notes | 用户笔记表 | 动态增长 |
| favorites | 用户收藏表 | 动态增长 |
| browse_history | 浏览历史表 | 动态增长 |

### 6.2 索引设计

| 索引名 | 表 | 列 | 用途 |
|--------|-----|-----|------|
| idx_notes_user | notes | user_id | 按用户查询笔记 |
| idx_favorites_user | favorites | user_id | 按用户查询收藏 |
| idx_history_user | browse_history | user_id | 按用户查询历史 |
| idx_spots_region | tour_spots | region | 按区域筛选景点 |
| idx_music_category | music | category | 按分类筛选音乐 |
| UNIQUE | favorites | (user_id, item_type, item_id) | 防止重复收藏 |
| UNIQUE | users | username | 用户名唯一 |

---

## 七、Docker 容器化

### 7.1 容器编排

```
docker-compose.yml
    │
    ├── db (PostgreSQL 16 Alpine)
    │   ├── 端口: 5432:5432
    │   ├── 数据卷: pgdata
    │   └── 健康检查: pg_isready
    │
    ├── backend (Spring Boot)
    │   ├── 端口: 8080:8080
    │   ├── 依赖: db (service_healthy)
    │   └── 环境变量: DB_HOST, DB_USER, DB_PASSWORD, JWT_SECRET
    │
    └── frontend (Nginx)
        ├── 端口: 80:80
        ├── 依赖: backend
        └── 反向代理: /api/* → backend:8080
```

### 7.2 构建流程

```
后端 Dockerfile（多阶段）:
  Stage 1: maven:3.9-eclipse-temurin-21 → mvn package
  Stage 2: eclipse-temurin:21-jre-alpine → java -jar app.jar

前端 Dockerfile（多阶段）:
  Stage 1: node:20-alpine + pnpm → pnpm build
  Stage 2: nginx:alpine → 静态文件 + nginx.conf
```

### 7.3 启动命令

```bash
# 一键启动全部服务
docker-compose up -d --build

# 分别开发
cd frontend && pnpm dev          # 前端 http://localhost:5173
cd backend && mvn spring-boot:run # 后端 http://localhost:8080

# 仅启动数据库
docker-compose up -d db
```

---

## 八、安全设计

| 安全措施 | 实现 |
|----------|------|
| 密码加密 | BCrypt（Spring Security） |
| 认证机制 | JWT Token（有效期 24 小时） |
| CSRF 防护 | 禁用（无状态 API，靠 JWT） |
| CORS 跨域 | 白名单模式（localhost:5173/3000） |
| SQL 注入 | JPA 参数化查询 |
| 权限控制 | 笔记/收藏接口校验 userId 所有权 |
| 数据库迁移 | Flyway 版本控制 |

---

## 九、环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `DB_USER` | tibetan | PostgreSQL 用户名 |
| `DB_PASSWORD` | tibetan123 | PostgreSQL 密码 |
| `DB_HOST` | localhost | 数据库主机（Docker 内为 `db`） |
| `DB_NAME` | tibetan_platform | 数据库名 |
| `JWT_SECRET` | xueyu-amdo-... | JWT 签名密钥 |

---

## 十、文件统计

| 模块 | 文件数 | 说明 |
|------|:---:|------|
| 前端视图 | 10 | Vue SFC 页面组件 |
| 前端组件 | 3 | 布局 + 通用组件 |
| 前端基建 | 7 | 路由/状态/API/类型/样式 |
| 后端实体 | 6 | JPA Entity 类 |
| 后端接口 | 7 | REST Controller |
| 后端仓库 | 6 | JPA Repository |
| 后端配置 | 4 | Security/CORS/JWT/Filter |
| 后端 DTO | 2 | 请求响应封装 |
| SQL 迁移 | 1 | Flyway V1 建表脚本（8 张表） |
| Docker | 4 | Compose + 2 Dockerfile + Nginx |
| **合计** | **50** | |
