# 雪域安多·藏韵传承 — 代码架构文档

## 一、项目概述

本项目为安多藏族文化传播平台，采用前后端分离架构，前端使用 Vue 3 + Vite 构建 SPA 应用，后端使用 Spring Boot 3 提供 RESTful API，数据库采用 PostgreSQL 16，通过 Docker Compose 实现容器化部署。

---

## 二、系统总体架构图

```mermaid
graph TB
    subgraph 用户端
        Browser[用户浏览器]
    end

    subgraph Docker Compose
        subgraph frontend ["前端容器 (Nginx :80)"]
            StaticFiles["Vue SPA 静态资源<br/>HTML/JS/CSS/Images"]
            NginxProxy["Nginx 反向代理<br/>try_files → index.html<br/>/api/* → backend:8080"]
        end

        subgraph backend ["后端容器 (Spring Boot :8080)"]
            Filter["JwtAuthFilter<br/>JWT 令牌验证"]
            Security["Spring Security<br/>路由权限控制"]
            Controllers["REST Controllers<br/>Auth | Spot | Folk | Music<br/>Note | Favorite | Scripture<br/>History | Translate"]
            Repositories["JPA Repositories<br/>Spring Data 自动实现"]
            Entities["JPA Entities<br/>ORM 实体映射"]
        end

        subgraph db ["数据库容器 (PostgreSQL :5432)"]
            Database["tibetan_platform<br/>8 张业务表"]
        end
    end

    subgraph 外部服务
        MyMemory["MyMemory 翻译 API"]
    end

    Browser -->|HTTP :80| NginxProxy
    NginxProxy -->|静态资源| StaticFiles
    NginxProxy -->|/api/*| Filter
    Filter --> Security --> Controllers
    Controllers --> Repositories --> Entities --> Database
    Controllers -->|翻译代理| MyMemory
```

---

## 三、E-R 图（实体关系图）

```mermaid
erDiagram
    users ||--o{ notes : "拥有"
    users ||--o{ favorites : "收藏"
    users ||--o{ browse_history : "浏览"

    users {
        bigserial id PK
        varchar username UK "唯一用户名"
        varchar password_hash "BCrypt加密"
        varchar nickname
        timestamp created_at
        timestamp updated_at
    }

    notes {
        bigserial id PK
        bigint user_id FK "→ users.id CASCADE"
        varchar title
        text content
        timestamp created_at
        timestamp updated_at
    }

    favorites {
        bigserial id PK
        bigint user_id FK "→ users.id CASCADE"
        varchar item_type "spot/folk/music"
        bigint item_id "多态关联目标ID"
        varchar item_name
        timestamp created_at
    }

    browse_history {
        bigserial id PK
        bigint user_id FK "→ users.id CASCADE"
        varchar item_type
        bigint item_id
        varchar item_name
        timestamp visited_at
    }

    tour_spots {
        bigserial id PK
        varchar name
        varchar region "qinghai/gannan/chuanxi/zangbei"
        varchar address
        text description
        varchar image_url
        varchar link_url
        decimal latitude "精度10,7"
        decimal longitude "精度10,7"
        int sort_order
        timestamp created_at
    }

    folk_cultures {
        bigserial id PK
        varchar title
        varchar subtitle
        varchar category "非遗/传统节日/宗教习俗"
        text description
        varchar image_url
        int sort_order
        timestamp created_at
    }

    music {
        bigserial id PK
        varchar title
        varchar artist
        varchar category "安多民歌/曼陀铃弹唱/器乐独奏/酒歌"
        varchar duration
        varchar cover_url
        varchar audio_url
        timestamp created_at
    }

    scriptures {
        bigserial id PK
        text tibetan_text "藏文原文"
        text chinese_translation "汉文翻译"
        int sort_order
        varchar scripture_name "经名"
    }
```

### 实体关系说明

| 关系 | 类型 | 说明 |
|------|------|------|
| users → notes | 1:N | 一个用户拥有多条笔记，删除用户级联删除笔记 |
| users → favorites | 1:N | 一个用户拥有多条收藏，(user_id, item_type, item_id) 唯一约束 |
| users → browse_history | 1:N | 一个用户拥有多条浏览记录 |
| favorites → tour_spots/folk_cultures/music | 多态 | item_type 区分收藏类型，item_id 指向对应表主键 |
| tour_spots | 独立 | 按 region 分组 |
| folk_cultures | 独立 | 按 category 分类 |
| music | 独立 | 按 category 分类 |
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

### 4.2 前端组件树

```mermaid
graph TD
    App["App.vue<br/>根组件"]
    AppHeader["AppHeader.vue<br/>导航栏 + 用户菜单"]
    RouterView["&lt;router-view /&gt;<br/>页面路由出口"]
    AppFooter["AppFooter.vue<br/>页脚"]
    BackToTop["BackToTop.vue<br/>返回顶部"]

    App --> AppHeader
    App --> RouterView
    App --> AppFooter
    App --> BackToTop

    RouterView --> HomeView["HomeView<br/>首页: 轮播图+卡片+工具入口"]
    RouterView --> SpotDetailView["SpotDetailView<br/>景点详情: 6个景点"]
    RouterView --> FolkDetailView["FolkDetailView<br/>民俗详情: 6个民俗"]
    RouterView --> MusicView["MusicView<br/>藏族音乐: 播放器"]
    RouterView --> ScriptureView["ScriptureView<br/>藏文经典: 展开翻译"]
    RouterView --> TranslateView["TranslateView<br/>藏汉翻译: API调用"]
    RouterView --> CalendarView["CalendarView<br/>藏历查询"]
    RouterView --> NoteView["NoteView<br/>藏文笔记: CRUD"]
    RouterView --> PhraseView["PhraseView<br/>藏语短语: 搜索/语音"]
    RouterView --> KeyboardView["KeyboardView<br/>藏文键盘"]
```

### 4.3 目录结构

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
    ├── vite-env.d.ts             # Vite 客户端类型声明
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
    ├── views/                    # 10 个页面视图（见组件树）
    └── assets/styles/
        ├── variables.scss        # CSS 变量（设计令牌）
        └── global.scss           # 全局重置样式
```

### 4.4 路由表

| 路由 | 视图组件 | 功能 | 是否需要认证 |
|------|----------|------|:---:|
| `/` | HomeView | 首页（轮播图+民俗卡片+旅游卡片+8个工具） | 否 |
| `/spots/:id` | SpotDetailView | 景点详情 | 否 |
| `/folk/:id` | FolkDetailView | 民俗详情 | 否 |
| `/music` | MusicView | 藏族音乐 | 否 |
| `/scripture` | ScriptureView | 藏文经典 | 否 |
| `/translate` | TranslateView | 藏汉翻译 | 否 |
| `/calendar` | CalendarView | 藏历查询 | 否 |
| `/notes` | NoteView | 藏文笔记 | 是 |
| `/phrases` | PhraseView | 藏语短语 | 否 |
| `/keyboard` | KeyboardView | 藏文键盘 | 否 |

### 4.5 前端数据流图

```mermaid
flowchart LR
    User["用户操作"] --> View["Vue 视图组件"]
    View -->|读写状态| Store["Pinia Store<br/>(user.ts)"]
    Store -->|持久化| LS["localStorage<br/>authToken / userName"]
    View -->|HTTP 请求| Axios["Axios 实例<br/>(request.ts)"]
    Axios -->|自动附加 JWT| Proxy["Vite Dev Proxy<br/>或 Nginx 反代"]
    Proxy -->|/api/*| Backend["Spring Boot<br/>:8080"]
```

---

## 五、后端架构

### 5.1 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.4.3 | 后端框架 |
| Spring Data JPA | - | ORM 数据访问 |
| Spring Security | - | 认证授权 |
| PostgreSQL | 16 | 关系数据库 |
| Flyway | - | 数据库版本迁移 |
| JJWT | 0.12.6 | JWT Token 生成与验证 |
| Lombok | - | 代码简化 |
| Maven | 3.9 | 构建工具 |

### 5.2 后端分层架构图

```mermaid
flowchart TD
    HTTP["HTTP 请求"]
    HTTP --> JwtFilter["JwtAuthFilter<br/>解析 JWT → 填充 SecurityContext"]
    JwtFilter --> Security["SecurityConfig<br/>路由级权限: permitAll / authenticated"]
    Security --> CORS["CorsConfig<br/>跨域白名单"]
    CORS --> Controller

    subgraph Controller ["Controller 层 (9个)"]
        AuthC["AuthController<br/>POST /auth/register<br/>POST /auth/login"]
        SpotC["SpotController<br/>GET /spots<br/>GET /spots/{id}"]
        FolkC["FolkController<br/>GET /folk<br/>GET /folk/{id}"]
        MusicC["MusicController<br/>GET /music<br/>GET /music/{id}"]
        ScriptureC["ScriptureController<br/>GET /scriptures<br/>GET /scriptures/{id}"]
        NoteC["NoteController<br/>GET/POST/PUT/DELETE /notes"]
        FavC["FavoriteController<br/>GET/POST/DELETE /favorites"]
        HistC["HistoryController<br/>GET/POST/DELETE /history"]
        TransC["TranslateController<br/>POST /translate"]
    end

    Controller --> Repository

    subgraph Repository ["Repository 层 (8个)"]
        UserRepo["UserRepository"]
        SpotRepo["SpotRepository"]
        FolkRepo["FolkRepository"]
        MusicRepo["MusicRepository"]
        ScriptureRepo["ScriptureRepository"]
        NoteRepo["NoteRepository"]
        FavRepo["FavoriteRepository"]
        HistRepo["BrowseHistoryRepository"]
    end

    Repository --> Entity

    subgraph Entity ["Entity 层 (8个)"]
        UserE["User"]
        SpotE["TourSpot"]
        FolkE["FolkCulture"]
        MusicE["Music"]
        ScriptureE["Scripture"]
        NoteE["Note"]
        FavE["Favorite"]
        HistE["BrowseHistory"]
    end

    Entity --> DB["PostgreSQL<br/>tibetan_platform"]
    TransC -->|RestTemplate Bean| ExtAPI["MyMemory<br/>翻译 API"]
```

### 5.3 目录结构

```
backend/
├── pom.xml                                    # Maven 依赖配置
├── Dockerfile                                 # 多阶段构建（Maven → JRE Alpine）
└── src/main/
    ├── java/com/tibetan/platform/
    │   ├── TibetanPlatformApplication.java    # Spring Boot 启动类
    │   ├── config/
    │   │   ├── SecurityConfig.java            # Spring Security 配置
    │   │   ├── CorsConfig.java                # CORS 跨域配置
    │   │   ├── JwtUtil.java                   # JWT 工具类
    │   │   ├── JwtAuthFilter.java             # JWT 认证过滤器
    │   │   └── RestTemplateConfig.java        # RestTemplate Bean 配置
    │   ├── entity/                            # 8 个 JPA 实体类
    │   ├── repository/                        # 8 个数据访问接口
    │   ├── controller/                        # 9 个 REST 控制器
    │   └── dto/
    │       ├── ApiResponse.java               # 统一响应格式
    │       └── AuthRequest.java               # 认证请求 DTO
    └── resources/
        ├── application.yml                    # 应用配置
        └── db/migration/
            ├── V1__init_schema.sql            # 建表脚本（8张表+索引）
            └── V2__seed_data.sql              # 种子数据（12景点+6民俗+4音乐+27经文）
```

### 5.4 API 接口总览

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
| GET | `/api/music/{id}` | 音乐详情 |
| GET | `/api/scriptures` | 经文列表（可选 `?name=二十一度母礼赞`） |
| GET | `/api/scriptures/{id}` | 经文详情 |
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
| GET | `/api/history` | 获取浏览历史 |
| POST | `/api/history` | 记录浏览 |
| DELETE | `/api/history` | 清空浏览历史 |

#### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 5.5 JWT 认证时序图

```mermaid
sequenceDiagram
    participant C as 前端 (Vue)
    participant N as Nginx
    participant F as JwtAuthFilter
    participant S as SecurityConfig
    participant Ctrl as Controller
    participant DB as PostgreSQL

    Note over C,DB: === 注册/登录流程 ===
    C->>N: POST /api/auth/login {username, password}
    N->>Ctrl: 转发请求
    Ctrl->>DB: 查询用户 + BCrypt验证
    DB-->>Ctrl: 用户数据
    Ctrl-->>C: {token, username, userId}
    C->>C: localStorage.setItem('authToken', token)

    Note over C,DB: === 认证请求流程 ===
    C->>N: GET /api/notes (Header: Bearer token)
    N->>F: 转发请求
    F->>F: 解析 JWT → 验证签名 → 提取 userId
    F->>S: 填充 SecurityContext
    S->>S: 检查路由权限 (authenticated ✓)
    S->>Ctrl: 放行请求
    Ctrl->>Ctrl: SecurityContext.getAuthentication()
    Ctrl->>DB: 查询 notes WHERE user_id = ?
    DB-->>Ctrl: 笔记列表
    Ctrl-->>C: ApiResponse {code:200, data:[...]}
```

---

## 六、数据库设计

### 6.1 表结构总览

| 表名 | 说明 | 种子数据 |
|------|------|:---:|
| users | 用户表 | - |
| tour_spots | 景点表 | 12 条 |
| folk_cultures | 民俗文化表 | 6 条 |
| music | 音乐表 | 4 条 |
| scriptures | 经文表 | 27 条 |
| notes | 用户笔记表 | - |
| favorites | 用户收藏表 | - |
| browse_history | 浏览历史表 | - |

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

### 6.3 Flyway 迁移文件

| 文件 | 内容 |
|------|------|
| `V1__init_schema.sql` | 8 张表 + 7 个索引 |
| `V2__seed_data.sql` | 12 景点 + 6 民俗 + 4 音乐 + 27 经文 |

---

## 七、Docker 容器化

### 7.1 部署拓扑图

```mermaid
graph LR
    subgraph Host ["宿主机"]
        subgraph DC ["docker-compose.yml"]
            FE["frontend<br/>Nginx :80<br/>Vue SPA + 反代"]
            BE["backend<br/>Spring Boot :8080<br/>REST API"]
            PG["db<br/>PostgreSQL :5432<br/>tibetan_platform"]
        end
        Volume["pgdata<br/>数据持久卷"]
    end

    Internet["用户"] -->|:80| FE
    FE -->|/api/*| BE
    BE -->|JDBC| PG
    PG --- Volume

    BE -->|健康检查依赖| PG
    FE -->|启动依赖| BE
```

### 7.2 构建流程图

```mermaid
flowchart LR
    subgraph 后端构建
        M1["maven:3.9-temurin-21<br/>mvn package"] --> M2["temurin:21-jre-alpine<br/>java -jar app.jar"]
    end

    subgraph 前端构建
        N1["node:20-alpine + pnpm<br/>pnpm build"] --> N2["nginx:alpine<br/>静态文件 + nginx.conf"]
    end
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

## 八、安全架构图

```mermaid
flowchart TD
    Request["HTTP 请求"]
    Request --> CORS["CorsConfig<br/>跨域白名单过滤"]
    CORS --> JWT["JwtAuthFilter<br/>解析 Bearer Token"]
    JWT -->|有效 Token| Auth["填充 SecurityContext<br/>设置 Authentication"]
    JWT -->|无 Token / 无效| NoAuth["匿名请求"]
    Auth --> SC["SecurityConfig 路由匹配"]
    NoAuth --> SC

    SC -->|"/api/auth/**"| PermitAll["permitAll<br/>注册/登录"]
    SC -->|"GET /api/spots/**"| PermitAll2["permitAll<br/>公开数据"]
    SC -->|"/api/notes/**"| Authenticated["authenticated<br/>需要有效 Token"]
    SC -->|其他| Authenticated

    Authenticated -->|有 Auth| Controller["Controller 处理"]
    Authenticated -->|无 Auth| Reject["401 Unauthorized"]

    subgraph 安全措施
        BCrypt["密码: BCrypt 加密存储"]
        JWTSign["Token: HMAC-SHA256 签名"]
        ParamQuery["SQL: JPA 参数化查询"]
        Ownership["权限: userId 所有权校验"]
        InputLimit["输入: 翻译文本 ≤5000 字符"]
    end
```

| 安全措施 | 实现 |
|----------|------|
| 密码加密 | BCrypt（Spring Security） |
| 认证机制 | JWT Token（HMAC-SHA256，有效期 24 小时） |
| CSRF 防护 | 禁用（无状态 API，靠 JWT） |
| CORS 跨域 | 白名单模式（localhost:5173/3000） |
| SQL 注入 | JPA 参数化查询 |
| 权限控制 | 笔记/收藏/历史接口校验 userId 所有权 |
| 输入验证 | 翻译文本长度限制 5000 字符 |
| 错误脱敏 | 外部 API 错误不暴露给客户端 |
| 数据库迁移 | Flyway 版本控制 |

---

## 九、环境变量

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `DB_USER` | tibetan | PostgreSQL 用户名 |
| `DB_PASSWORD` | tibetan123 | PostgreSQL 密码 |
| `DB_HOST` | localhost | 数据库主机（Docker 内为 `db`） |
| `DB_NAME` | tibetan_platform | 数据库名 |
| `JWT_SECRET` | xueyu-amdo-... | JWT 签名密钥（生产环境请更换） |

---

## 十、文件统计

| 模块 | 文件数 | 说明 |
|------|:---:|------|
| 前端视图 | 10 | Vue SFC 页面组件 |
| 前端组件 | 3 | 布局 + 通用组件 |
| 前端基建 | 8 | 路由/状态/API/类型/样式/类型声明 |
| 后端实体 | 8 | JPA Entity 类 |
| 后端接口 | 9 | REST Controller |
| 后端仓库 | 8 | JPA Repository |
| 后端配置 | 5 | Security/CORS/JWT/Filter/RestTemplate |
| 后端 DTO | 2 | 请求响应封装 |
| SQL 迁移 | 2 | Flyway V1 建表 + V2 种子数据 |
| Docker | 4 | Compose + 2 Dockerfile + Nginx |
| **合计** | **59** | |
