// vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  base: 'https://github.com/djsz123456/Qinghai-Amdo-Tibetan-Cultural-Communication-Platform.git', // 替换为你的仓库名称
  server: {
    port: 5173,
    open: '/index.html'
  },
  build: {
    rollupOptions: {
      input: {
        main: './index.html',
        jingdian: './jingdian.html',
        minsu: './minsu.html'
      }
    }
  }
})