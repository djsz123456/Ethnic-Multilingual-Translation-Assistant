<template>
  <div>
    <div class="top-bar">
      <div class="top-bar-text">
        🏔️ 雪域安多·藏韵传承 | 安多藏族文化传播平台
      </div>
      <div class="top-bar-links">
        <a href="#" @click.prevent="handleFeatureClick('收藏')"><i class="fa fa-heart"></i> 收藏</a>
        <a href="#" @click.prevent="handleFeatureClick('分享')"><i class="fa fa-share-alt"></i> 分享</a>
      </div>
    </div>

    <header class="header">
      <div class="header-wrap">
        <router-link to="/" class="logo">གངས་ལྗོངས་ཨ་མདོ། བོད་ཀྱི་སྲོལ་རྒྱུན་རིག་གནས།</router-link>
        <nav class="nav">
          <router-link to="/">首页</router-link>
          <a href="#culture" @click.prevent="scrollTo('culture')">安多民俗</a>
          <a href="#tour" @click.prevent="scrollTo('tour')">藏地旅游</a>
          <a href="#tools" @click.prevent="scrollTo('tools')">藏文工具</a>
        </nav>
        <div class="user-btn-wrapper">
          <button class="user-btn" :class="{ 'logged-in': userStore.isLoggedIn }" @click="toggleDropdown">
            <i :class="userStore.isLoggedIn ? 'fa fa-user' : 'fa fa-sign-in'"></i>
            <span v-if="userStore.isLoggedIn" class="badge">{{ userStore.userName.charAt(0) }}</span>
          </button>
          <div class="dropdown-menu" :class="{ show: showDropdown }">
            <div class="dropdown-header">
              <div class="dropdown-avatar">{{ userStore.userName.charAt(0) }}</div>
              <div class="dropdown-user-info">
                <div class="dropdown-username">{{ userStore.userName }}</div>
                <div class="dropdown-user-email">欢迎回来</div>
              </div>
            </div>
            <div class="dropdown-list">
              <button class="dropdown-item logout" @click="handleLogout">
                <i class="fa fa-sign-out"></i> 退出登录
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const showDropdown = ref(false)

function toggleDropdown() {
  if (!userStore.isLoggedIn) {
    alert('登录功能正在迁移中…')
    return
  }
  showDropdown.value = !showDropdown.value
}

function handleLogout() {
  userStore.logout()
  showDropdown.value = false
}

function handleFeatureClick(feature: string) {
  alert(`${feature}功能正在迁移中…`)
}

function scrollTo(id: string) {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style lang="scss" scoped>
.top-bar {
  width: 100%;
  height: 40px;
  background: linear-gradient(90deg, #f8f8f8, #f0f0f0);
  border-bottom: 1px solid #e0e0e0;
  line-height: 40px;
  padding: 0 30px;
  font-size: 13px;
  color: #666;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.top-bar-links a {
  color: #666;
  text-decoration: none;
  margin-left: 20px;
  transition: var(--transition);
  &:hover { color: var(--primary-red); }
}

.header {
  width: 100%;
  background: linear-gradient(135deg, var(--primary-red) 0%, #B83C2E 100%);
  box-shadow: var(--shadow-md);
  position: sticky;
  top: 0;
  z-index: 999;
}

.header-wrap {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
  padding: 0 30px;
}

.logo {
  font-size: 22px;
  font-weight: bold;
  color: #fff;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
  letter-spacing: 1px;
  text-decoration: none;
}

.nav {
  display: flex;
  gap: 8px;
  a {
    color: #fff;
    text-decoration: none;
    padding: 10px 18px;
    font-size: 15px;
    font-weight: 500;
    border-radius: 8px;
    transition: var(--transition);
    &:hover { background: rgba(255, 255, 255, 0.15); }
  }
}

.user-btn-wrapper { position: relative; }

.user-btn {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.3);
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  transition: var(--transition);
  position: relative;
  &:hover { background: rgba(255, 255, 255, 0.2); }
  &.logged-in { border-color: var(--primary-gold); }
}

.badge {
  position: absolute;
  top: -4px;
  right: -4px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: var(--primary-gold);
  color: #fff;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dropdown-menu {
  position: absolute;
  top: 56px;
  right: 0;
  width: 240px;
  background: #fff;
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
  opacity: 0;
  visibility: hidden;
  transform: translateY(-10px);
  transition: var(--transition);
  z-index: 1000;
  &.show { opacity: 1; visibility: visible; transform: translateY(0); }
}

.dropdown-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.dropdown-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--primary-red);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
}

.dropdown-username { font-weight: 600; font-size: 14px; }
.dropdown-user-email { font-size: 12px; color: #999; }

.dropdown-list { padding: 8px; }

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  color: inherit;
  transition: background 0.2s;
  &:hover { background: #f5f5f5; }
  &.logout { color: #DC3545; &:hover { background: #fef2f2; } }
}

@media (max-width: 768px) {
  .top-bar { display: none; }
  .header-wrap { height: 60px; padding: 0 16px; }
  .logo { font-size: 16px; }
  .nav { display: none; }
}
</style>
