<template>
  <div class="folk-page">
    <div class="container" v-if="error">
      <div class="error-page">
        <div class="error-icon"><i class="fa fa-frown-o"></i></div>
        <h2>未找到该民俗内容</h2>
        <p>请检查链接是否正确，或返回首页重新选择。</p>
        <router-link to="/" class="back-btn"><i class="fa fa-home"></i> 返回首页</router-link>
      </div>
    </div>

    <template v-else-if="folk">
      <div class="container">
        <div class="hero-banner">
          <img :src="folk.image" :alt="folk.title" @error="onImgError" />
          <div class="hero-overlay">
            <div class="hero-category"><i class="fa fa-certificate"></i> {{ folk.category }}</div>
            <h1 class="hero-title">{{ folk.title }}</h1>
            <p class="hero-subtitle">{{ folk.subtitle }}</p>
          </div>
        </div>

        <div v-for="(sec, idx) in folk.sections" :key="idx" class="card">
          <div class="card-header">
            <div class="card-icon"><i :class="'fa ' + sec.icon"></i></div>
            <h2 class="card-title">{{ sec.title }}</h2>
          </div>
          <div class="card-body">
            <p v-for="(para, pIdx) in sec.content" :key="pIdx" class="card-para">{{ para }}</p>
          </div>
        </div>

        <div class="back-bar">
          <router-link to="/" class="back-btn"><i class="fa fa-arrow-left"></i> 返回首页</router-link>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import type { FolkCulture } from '@/types/folk'

const route = useRoute()
const error = ref(false)
const folk = ref<FolkCulture | null>(null)

const FOLK_DATA: Record<number, FolkCulture> = {
  1: {
    id: 1, title: '藏族情歌《拉伊》', subtitle: '安多草原上流传千年的爱情吟唱', category: '非物质文化遗产', image: '/images/guoluo/images/gl.jpg',
    sections: [
      { icon: 'fa-history', title: '来源与历史', content: ['《拉伊》（藏语：ལ་གཞས།）是流行于青海安多藏区的一种传统民歌，已有数百年历史。"拉伊"字面意思为"山歌"或"野歌"，因多在山野草原间演唱而得名，是藏族青年男女表达爱慕之情的重要方式。', '安多《拉伊》尤以果洛、黄南、海南等藏族聚居地最为盛行，已被列入国家级非物质文化遗产名录。'] },
      { icon: 'fa-music', title: '表现形式', content: ['《拉伊》曲调高亢悠扬，节奏自由舒展，演唱者常以假声行腔，拖腔绵长，回荡山谷之间。歌词多即兴创作，以比兴手法借助草原、雄鹰、格桑花等自然意象表达内心情感。', '演唱形式分为独唱和对唱两种，对唱时男女双方轮流吟唱，以歌词机智诙谐、意境深远者为胜。'] },
      { icon: 'fa-heart', title: '文化意义', content: ['《拉伊》承载着安多藏族的爱情观与人生哲学，反映了藏族人民对自由、美好生活的热烈向往。它不仅是青年男女的恋爱媒介，更是藏族文化认同与社区凝聚的精神纽带。'] },
      { icon: 'fa-users', title: '传承现状', content: ['随着现代流行文化的冲击，《拉伊》的传承面临挑战。目前，青海省已将《拉伊》保护列入重点工程，在玛沁、同仁等地设立传习所，鼓励年轻一代学习演唱。', '每年夏季的草原歌会和各类民俗节庆活动，仍是《拉伊》展示与传承的重要舞台。'] },
    ],
  },
  2: {
    id: 2, title: '安多藏戏', subtitle: '高原舞台上活态传承的千年戏剧', category: '传统戏剧', image: '/images/huangnan/images/hn1.jpg',
    sections: [
      { icon: 'fa-flag', title: '起源', content: ['安多藏戏起源于藏传佛教寺院的宗教仪式表演，后逐渐发展为具有完整戏剧结构的舞台艺术，迄今已有数百年历史。', '以黄南同仁、海南贵德为主要传承地，于2006年被列入国家级非物质文化遗产名录。'] },
      { icon: 'fa-book', title: '经典剧目', content: ['传统剧目包括《诺桑法王》《卓娃桑姆》《朗萨雯蚌》《顿月顿珠》等八大传统藏戏，以及根据《格萨尔王传》改编的系列剧目。'] },
      { icon: 'fa-star', title: '表演特点', content: ['安多藏戏表演综合了唱、念、做、打多种技艺，演员头戴面具，身穿色彩鲜艳的戏服。一出完整的藏戏演出往往持续一整天甚至数天。'] },
      { icon: 'fa-leaf', title: '传承', content: ['安多藏戏主要依靠师徒口传心授方式传承，黄南州同仁市设有专门的藏戏团。近年来，借助数字化保护项目，大量珍贵的安多藏戏表演影像得到系统整理。'] },
    ],
  },
  3: {
    id: 3, title: '雪顿节', subtitle: '藏区最盛大的节日——酸奶宴与晒佛节', category: '传统节日', image: '/images/taersi/images/ts1.jpg',
    sections: [
      { icon: 'fa-sun-o', title: '节日起源', content: ['雪顿节（藏语：ཞོ་སྟོན།）藏语意为"酸奶宴"，起源于17世纪藏传佛教寺院制度。安居期满后，信众以酸奶招待僧侣，逐渐演变为节日。', '后来与大型藏戏表演、展佛活动相结合，成为藏区一年中规模最宏大的综合性传统节日。'] },
      { icon: 'fa-calendar', title: '时间', content: ['雪顿节一般在藏历六月底至七月初举行，公历通常对应7月下旬至8月上旬。塔尔寺的展佛仪式是青海雪顿节最重要的活动。'] },
      { icon: 'fa-star', title: '活动内容', content: ['展佛——将珍贵巨型唐卡铺展于山坡晾晒供奉；藏戏——各剧团连续演出数天；集市——汇聚各地物产与手工艺品；赛马、射箭、藏族歌舞表演等民俗活动也一应俱全。'] },
      { icon: 'fa-map-marker', title: '各地习俗', content: ['青海塔尔寺以"晒大佛"最为著名；黄南州同仁以大型藏戏演出见长；果洛州与赛马节相结合，彰显安多藏区独特的文化风貌。'] },
    ],
  },
  4: {
    id: 4, title: '转湖习俗', subtitle: '环绕圣湖的朝圣之旅', category: '宗教习俗', image: '/images/qinghahu/images/nn3.jpg',
    sections: [
      { icon: 'fa-heart', title: '朝圣意义', content: ['转湖（藏语：མཚོ་སྐོར།）是藏传佛教信众围绕圣湖顺时针行走的宗教朝圣活动。藏族人相信，圣湖是神灵居所，绕湖转行能消除业障、积累功德。'] },
      { icon: 'fa-road', title: '转湖线路', content: ['青海湖周长约360公里，完整转湖一圈需步行约7至10天。近年来骑行转湖日趋流行。'] },
      { icon: 'fa-flag', title: '转湖习俗', content: ['朝圣者手持转经筒，口诵六字真言，每隔一段路便向湖面抛洒糌粑粉或风马旗。'] },
      { icon: 'fa-clock-o', title: '最佳时间', content: ['最佳季节为藏历4月至9月。每逢藏历马年，转湖的宗教意义尤为殊胜，信众相信马年转湖一圈的功德相当于平常年份的十二倍。'] },
    ],
  },
  5: {
    id: 5, title: '煨桑仪式', subtitle: '以烟为语，与天地神灵沟通的古老礼仪', category: '宗教仪式', image: '/images/sangkecaoyuan/images/skcy1.jpg',
    sections: [
      { icon: 'fa-history', title: '仪式背景', content: ['煨桑（藏语：བསང་།）是藏传佛教及苯教传统中一种以烟火祭祀为核心的宗教仪式，历史可追溯至苯教时代。', '"桑"意为"净化"，通过燃烧芳香植物产生的烟雾，象征向神灵献供、净化污秽、祈求吉祥。'] },
      { icon: 'fa-list-ol', title: '仪式步骤', content: ['煨桑仪式通常在清晨日出时分举行。首先由喇嘛或长者点燃桑火，随后依次添加燃料，同时诵念祈祷经文。信众围绕桑炉顺时针转行，手持哈达和风马旗。'] },
      { icon: 'fa-leaf', title: '用料', content: ['核心燃料为柏树枝，此外还会加入糌粑、酥油、红糖、奶渣、干果、茶叶等食物供品，以及藏香、藏药草等芳香植物。'] },
      { icon: 'fa-star', title: '文化意义', content: ['煨桑仪式贯穿藏族人的整个生命历程——出生、成年、婚嫁、远行、节庆、丧葬等重要时刻都离不开煨桑。', '在安多藏区，桑炉是寺庙与村落中最重要的公共宗教设施，每日清晨升腾的桑烟是藏区最典型的文化符号之一。'] },
    ],
  },
  6: {
    id: 6, title: '藏历新年', subtitle: '洛萨节——雪域高原辞旧迎新的盛典', category: '传统节日', image: '/images/sanjiangyuan/images/sjy.jpg',
    sections: [
      { icon: 'fa-calendar', title: '时间计算', content: ['藏历新年（藏语：ལོ་གསར།，Losar）是藏族一年中最重要的传统节日，时间依据藏历推算，通常在公历2月至3月之间。'] },
      { icon: 'fa-home', title: '节前准备', content: ['藏历十二月下旬，家家户户开始打扫房屋、粉刷墙壁、制作"卡赛"和"切玛"。', '除夕家人共吃"古突"，汤中包有不同东西预示着不同的运势，气氛欢乐热闹。'] },
      { icon: 'fa-star', title: '节日活动', content: ['正月初一互赠祝福，喝青稞酒、吃卡赛。寺院举行大规模法会，跳"羌姆"（神舞）。', '正月初三至初五举行赛马、射箭、拔河、锅庄舞等传统娱乐活动。'] },
      { icon: 'fa-map-marker', title: '各地差异', content: ['果洛州与格萨尔文化紧密相连；黄南州同仁以藏戏表演开年；海南州共和县的煨桑祈福规模盛大。', '塔尔寺的正月祈愿大法会吸引数万名信众，酥油花展、晒佛等活动令人叹为观止。'] },
    ],
  },
}

function onImgError(e: Event) {
  const target = e.target as HTMLImageElement
  target.src = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="920" height="460"><rect width="100%25" height="100%25" fill="%23f0ece6"/><text x="50%25" y="50%25" font-size="18" fill="%239B2B25" text-anchor="middle" dominant-baseline="middle">图片加载中...</text></svg>'
}

onMounted(() => {
  const id = parseInt(String(route.params.id), 10)
  if (!id || !FOLK_DATA[id]) {
    error.value = true
    return
  }
  folk.value = FOLK_DATA[id]
  document.title = folk.value.title + ' - 民俗文化详情 - 雪域安多·藏韵传承'
})
</script>

<style lang="scss" scoped>
.container { max-width: 920px; margin: 0 auto; padding: 40px 20px 80px; }

.error-page { text-align: center; padding: 100px 20px; .error-icon { font-size: 72px; color: #ddd; margin-bottom: 24px; } h2 { color: var(--text-primary); margin-bottom: 12px; } p { color: var(--text-secondary); margin-bottom: 30px; } }

.back-btn { display: inline-flex; align-items: center; gap: 8px; padding: 12px 32px; background: linear-gradient(135deg, var(--primary-red), #B83C2E); color: white; border-radius: 8px; font-size: 16px; text-decoration: none; transition: var(--transition); &:hover { transform: translateY(-2px); box-shadow: var(--shadow-md); } }

.hero-banner { position: relative; width: 100%; height: 460px; overflow: hidden; border-radius: var(--radius-md); margin-bottom: 32px; box-shadow: var(--shadow-lg); img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.6s; } &:hover img { transform: scale(1.04); } }
.hero-overlay { position: absolute; inset: 0; background: linear-gradient(to bottom, transparent 30%, rgba(0,0,0,0.75) 100%); display: flex; flex-direction: column; justify-content: flex-end; padding: 36px; }
.hero-category { display: inline-flex; align-items: center; gap: 6px; background: rgba(212,175,55,0.9); color: #fff; padding: 4px 14px; border-radius: 20px; font-size: 13px; font-weight: 500; align-self: flex-start; margin-bottom: 12px; }
.hero-title { font-size: 36px; font-weight: 700; color: white; text-shadow: 2px 2px 6px rgba(0,0,0,0.5); margin-bottom: 8px; }
.hero-subtitle { color: rgba(255,255,255,0.85); font-size: 16px; }

.card { background: var(--bg-secondary); border-radius: var(--radius-md); padding: 36px 40px; box-shadow: var(--shadow-sm); border-left: 4px solid var(--primary-red); margin-bottom: 24px; transition: var(--transition); &:hover { box-shadow: var(--shadow-md); transform: translateY(-2px); } }
.card-header { display: flex; align-items: center; gap: 12px; margin-bottom: 18px; padding-bottom: 14px; border-bottom: 1px solid #f0ece6; }
.card-icon { width: 40px; height: 40px; background: linear-gradient(135deg, var(--primary-red), #B83C2E); border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-size: 18px; flex-shrink: 0; }
.card-title { font-size: 20px; font-weight: 600; color: var(--primary-red); }
.card-para { font-size: 15px; line-height: 2; color: var(--text-secondary); margin-bottom: 12px; text-indent: 2em; &:last-child { margin-bottom: 0; } }

.back-bar { display: flex; justify-content: center; margin-top: 20px; }

@media (max-width: 768px) {
  .hero-banner { height: 300px; }
  .hero-title { font-size: 26px; }
  .card { padding: 24px 20px; }
}
</style>
