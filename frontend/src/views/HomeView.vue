<template>
  <div class="home-page">
    <section class="hero">
      <div class="hero-content">
        <h1>雪域安多·藏韵传承</h1>
        <p>安多藏族文化传播平台 — 展示安多地区旅游景点与民俗文化</p>
        <div class="hero-stats">
          <div class="stat-item">
            <span class="stat-number">12+</span>
            <div class="stat-label">圣地景点</div>
          </div>
          <div class="stat-item">
            <span class="stat-number">1000+</span>
            <div class="stat-label">年文化传承</div>
          </div>
          <div class="stat-item">
            <span class="stat-number">50+</span>
            <div class="stat-label">传统节日</div>
          </div>
        </div>
      </div>
    </section>

    <main class="main-container">
      <section id="culture">
        <h2 class="section-title">安多藏族民俗文化（རིག་གནས།）</h2>
        <div class="card-grid">
          <router-link v-for="item in folkList" :key="item.id" :to="`/folk/${item.id}`" class="card">
            <div class="card-image-wrapper">
              <img :src="item.img" :alt="item.title" loading="lazy" />
              <span class="card-badge">民俗</span>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.title }}</h3>
              <p class="card-desc">{{ item.desc }}</p>
            </div>
          </router-link>
        </div>
      </section>

      <section id="tour">
        <h2 class="section-title">藏地旅游（དམངས་སྲོལ།）</h2>
        <div class="card-grid">
          <router-link v-for="item in tourList" :key="item.id" :to="`/spots/${item.id}`" class="card">
            <div class="card-image-wrapper">
              <img :src="item.img" :alt="item.title" loading="lazy" />
              <span class="card-badge">旅游</span>
            </div>
            <div class="card-body">
              <h3 class="card-title">{{ item.title }}</h3>
              <p class="card-desc">{{ item.desc }}</p>
            </div>
          </router-link>
        </div>
      </section>

      <section id="tools">
        <h2 class="section-title">藏文实用工具（ལག་ཆ།）</h2>
        <div class="tools-grid">
          <a href="/zangli-master/calendar.html" class="tool-card">
            <div class="tool-icon"><i class="fa fa-calendar"></i></div>
            <h3>藏文日历</h3>
            <p>百年藏历在线查询</p>
          </a>
          <router-link to="/translate" class="tool-card">
            <div class="tool-icon"><i class="fa fa-language"></i></div>
            <h3>藏汉翻译</h3>
            <p>藏汉双向智能翻译</p>
          </router-link>
          <a href="/jianpan/jianpan.html" class="tool-card">
            <div class="tool-icon"><i class="fa fa-keyboard-o"></i></div>
            <h3>藏文键盘</h3>
            <p>在线藏文输入法</p>
          </a>
          <router-link to="/music" class="tool-card">
            <div class="tool-icon"><i class="fa fa-music"></i></div>
            <h3>藏族音乐</h3>
            <p>安多传统音乐欣赏</p>
          </router-link>
          <router-link to="/scripture" class="tool-card">
            <div class="tool-icon"><i class="fa fa-book"></i></div>
            <h3>藏文经典</h3>
            <p>经典藏文诵读学习</p>
          </router-link>
          <a href="/biji/biji.html" class="tool-card">
            <div class="tool-icon"><i class="fa fa-pencil"></i></div>
            <h3>藏文笔记</h3>
            <p>个人藏文学习笔记</p>
          </a>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
const folkList = [
  { id: 1, title: '藏族情歌《拉伊》', desc: '安多藏族最具代表性的民间歌曲形式，以即兴对唱表达爱情。', img: '/images/guoluo/images/gl.jpg' },
  { id: 2, title: '安多藏戏', desc: '融合歌舞、念白、表演的综合艺术，被誉为"藏文化活化石"。', img: '/images/huangnan/images/hn1.jpg' },
  { id: 3, title: '雪顿节', desc: '藏族最隆重的传统节日之一，意为"酸奶宴"。', img: '/images/taersi/images/ts1.jpg' },
  { id: 4, title: '转湖习俗', desc: '信众沿青海湖环行祈福，转湖一圈约360公里。', img: '/images/qinghahu/images/nn3.jpg' },
  { id: 5, title: '煨桑仪式', desc: '以松柏枝、酥油等为祭品，点燃产生桑烟，祈求神灵庇佑。', img: '/images/sangkecaoyuan/images/skcy1.jpg' },
  { id: 6, title: '藏历新年', desc: '藏族最重要的传统节日，家家户户制作"切玛"供品，欢庆新春。', img: '/images/sanjiangyuan/images/sjy.jpg' },
]

const tourList = [
  { id: 1, title: '塔尔寺', desc: '格鲁派六大寺之一，宗喀巴大师诞生地，酥油花艺术驰名中外。', img: '/images/taersi/images/ts1.jpg' },
  { id: 2, title: '青海湖', desc: '中国最大内陆咸水湖，藏族心中的圣湖，候鸟天堂。', img: '/images/qinghahu/images/nn3.jpg' },
  { id: 3, title: '拉卜楞寺', desc: '世界最大的藏学学府，拥有最完整的藏传佛教教学体系。', img: '/images/labulengsi/images/lb8.jpg' },
  { id: 4, title: '黄南热贡', desc: '热贡艺术国家级非遗，唐卡、堆绣、泥塑享誉世界。', img: '/images/huangnan/images/hn1.jpg' },
  { id: 5, title: '日月山', desc: '文成公主入藏必经地，农耕与游牧文明的地理分界线。', img: '/images/riyueshan/images/nn2.jpg' },
  { id: 6, title: '三江源', desc: '长江、黄河、澜沧江发源地，中华水塔，世界级生态保护区。', img: '/images/sanjiangyuan/images/sjy.jpg' },
]
</script>

<style lang="scss" scoped>
.hero {
  height: 500px;
  background: linear-gradient(135deg, var(--primary-red) 0%, #B83C2E 50%, var(--primary-gold) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #fff;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: 700;
  text-shadow: 2px 4px 8px rgba(0, 0, 0, 0.3);
  margin-bottom: 16px;
}

.hero-content p {
  font-size: 1.1rem;
  opacity: 0.9;
  margin-bottom: 30px;
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: var(--primary-gold);
}

.stat-label {
  font-size: 0.85rem;
  opacity: 0.8;
  margin-top: 4px;
}

.main-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.section-title {
  font-size: 1.5rem;
  color: var(--primary-red);
  margin-bottom: 30px;
  padding-bottom: 12px;
  border-bottom: 2px solid rgba(155, 43, 37, 0.15);
  &::before { content: '༄ '; color: var(--primary-gold); }
}

section { margin-bottom: 60px; }

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 24px;
}

.card {
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  text-decoration: none;
  color: inherit;
  &:hover { transform: translateY(-6px); box-shadow: var(--shadow-lg); }
}

.card-image-wrapper {
  position: relative;
  height: 200px;
  overflow: hidden;
  img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; }
  .card:hover & img { transform: scale(1.05); }
}

.card-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: var(--primary-red);
  color: #fff;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.card-body { padding: 20px; }
.card-title { font-size: 1.05rem; font-weight: 600; margin-bottom: 8px; }
.card-desc { font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6; }

.tools-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.tool-card {
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  padding: 30px 20px;
  text-align: center;
  box-shadow: var(--shadow-sm);
  transition: var(--transition);
  text-decoration: none;
  color: inherit;
  &:hover { transform: translateY(-4px); box-shadow: var(--shadow-md); }
  h3 { font-size: 1rem; margin-top: 12px; }
  p { font-size: 0.8rem; color: var(--text-secondary); margin-top: 6px; }
}

.tool-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-red), #c0392b);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .hero { height: 400px; }
  .hero-content h1 { font-size: 2rem; }
  .hero-stats { gap: 20px; }
  .stat-number { font-size: 1.5rem; }
  .card-grid { grid-template-columns: 1fr; }
}
</style>
