-- V1__init_schema.sql
-- 用户表
CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nickname      VARCHAR(100),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 景点表
CREATE TABLE tour_spots (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    region      VARCHAR(50) NOT NULL,
    address     VARCHAR(200),
    description TEXT,
    image_url   VARCHAR(500),
    link_url    VARCHAR(500),
    latitude    DECIMAL(10, 7),
    longitude   DECIMAL(10, 7),
    sort_order  INT DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 民俗文化表
CREATE TABLE folk_cultures (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    subtitle    VARCHAR(200),
    category    VARCHAR(50),
    description TEXT,
    image_url   VARCHAR(500),
    sort_order  INT DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 音乐表
CREATE TABLE music (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    artist      VARCHAR(100),
    category    VARCHAR(50),
    duration    VARCHAR(10),
    cover_url   VARCHAR(500),
    audio_url   VARCHAR(500),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 经文表
CREATE TABLE scriptures (
    id                  BIGSERIAL PRIMARY KEY,
    tibetan_text        TEXT NOT NULL,
    chinese_translation TEXT,
    sort_order          INT DEFAULT 0,
    scripture_name      VARCHAR(200)
);

-- 用户笔记表
CREATE TABLE notes (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title       VARCHAR(200),
    content     TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户收藏表
CREATE TABLE favorites (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    item_type   VARCHAR(20) NOT NULL,
    item_id     BIGINT NOT NULL,
    item_name   VARCHAR(200),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, item_type, item_id)
);

-- 浏览历史表
CREATE TABLE browse_history (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    item_type   VARCHAR(20) NOT NULL,
    item_id     BIGINT NOT NULL,
    item_name   VARCHAR(200),
    visited_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX idx_notes_user ON notes(user_id);
CREATE INDEX idx_favorites_user ON favorites(user_id);
CREATE INDEX idx_history_user ON browse_history(user_id);
CREATE INDEX idx_spots_region ON tour_spots(region);
CREATE INDEX idx_music_category ON music(category);
