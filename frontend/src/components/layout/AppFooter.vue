<template>
  <footer class="footer">
    <div class="footer-inner">
      <p><i class="fa fa-map-marker"></i> 安多藏族文化传播平台</p>
      <p class="footer-source">内容来源：安多本地人讲述、当地导游提供 · 真实民俗 · 拒绝编造</p>
      <p class="footer-copy">&copy; {{ year }} 雪域安多·藏韵传承</p>
    </div>
  </footer>
</template>

<script setup lang="ts">
const year = new Date().getFullYear()
</script>

<style lang="scss" scoped>
.footer {
  background: linear-gradient(135deg, #2C3E50, #1a252f);
  color: rgba(255, 255, 255, 0.7);
  text-align: center;
  padding: 30px 20px;
  margin-top: 60px;
}

.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-source {
  font-size: 12px;
  margin-top: 8px;
  opacity: 0.6;
}

.footer-copy {
  font-size: 12px;
  margin-top: 12px;
  opacity: 0.5;
}
</style>
