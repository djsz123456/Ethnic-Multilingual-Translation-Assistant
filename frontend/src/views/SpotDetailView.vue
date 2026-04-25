<template>
  <div class="spot-page">
    <div class="container" v-if="loading">
      <div class="loading-container">
        <div class="loading-spinner"></div>
        <div class="loading-text">正在加载景点详情...</div>
      </div>
    </div>

    <div class="container" v-else-if="error">
      <div class="error-container">
        <div class="error-icon"><i class="fa fa-exclamation-circle"></i></div>
        <div class="error-message">{{ errorMessage }}</div>
        <router-link to="/" class="back-btn"><i class="fa fa-home"></i> 返回首页</router-link>
      </div>
    </div>

    <div v-if="!loading && !error && spot">
      <div class="container">
        <div class="hero-banner">
          <img :src="spot.cover" :alt="spot.name" @error="onImgError" />
          <div class="hero-overlay">
            <h1 class="hero-title">{{ spot.name }}</h1>
            <span class="hero-address"><i class="fa fa-map-marker"></i> {{ spot.address }}</span>
          </div>
        </div>

        <div :class="['cards-grid', spot.sections.length === 1 ? 'single-col' : '']">
          <div v-for="(sec, idx) in spot.sections" :key="idx"
               :class="['content-card', spot.sections.length % 2 !== 0 && idx === spot.sections.length - 1 ? 'full-width' : '']">
            <div class="card-header">
              <div class="card-icon"><i :class="'fa ' + sec.icon"></i></div>
              <h2 class="card-title">{{ sec.title }}</h2>
            </div>
            <div class="card-body">{{ sec.content }}</div>
          </div>
        </div>

        <div class="action-bar" v-if="spot.link">
          <a :href="'/' + spot.link" class="visit-btn">
            <i class="fa fa-external-link"></i> 访问完整页面
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import type { TourSpot } from '@/types/spot'

const route = useRoute()
const loading = ref(true)
const error = ref(false)
const errorMessage = ref('')
const spot = ref<TourSpot | null>(null)

const SPOT_DATABASE: Record<string, TourSpot> = {
  '1': {
    id: '1', name: '塔尔寺', address: '青海·西宁',
    cover: '/images/taersi/images/ts1.jpg', link: 'taersi/taersi.html',
    sections: [
      { icon: 'fa-info-circle', title: '景点简介', content: '塔尔寺（藏语：སྐུ་འབུམ་བམས་པ་གླིང་།）位于青海省湟中区，距西宁市约25公里，是藏传佛教格鲁派六大寺院之一，也是宗喀巴大师的诞生地。始建于明朝嘉靖年间，至今已有逾四百年历史，是安多地区规模最大、最具影响力的藏传佛教寺院之一。' },
      { icon: 'fa-book', title: '历史渊源', content: '宗喀巴大师（1357—1419）是藏传佛教格鲁派的创立者。相传其母觉尊仁钦在大师出生地种下一株菩提树，后人在此建塔纪念，逐渐扩建为今日之塔尔寺。寺名来源于先有大银塔（"塔尔"即藏语"大银塔"之缩译）、后有寺院之建寺历史。' },
      { icon: 'fa-building', title: '建筑艺术', content: '全寺占地约600余亩，由大金瓦寺、小金瓦寺、大经堂、如意塔等九个殿堂及众多僧舍组成，融藏、汉建筑风格于一体。主殿金顶辉煌，大经堂气势恢宏，是藏汉建筑艺术高度融合的典范。' },
      { icon: 'fa-star', title: '三绝特色', content: '塔尔寺以"酥油花、壁画、堆绣"三绝驰名中外。\n· 酥油花：以酥油雕塑各种佛像、花卉、人物，精妙绝伦，每年正月十五灯节展出，吸引万人观瞻。\n· 壁画：寺内壁画色彩浓烈、题材丰富，是研究藏族绘画史的重要资料。\n· 堆绣：以彩色绸缎剪贴拼缝而成，立体感强，工艺繁复。' },
      { icon: 'fa-calendar', title: '旅游信息', content: '开放时间：07:30—18:30（旺季），08:00—17:30（淡季）\n门票：全票80元，优惠票40元\n最佳游览时节：4月—10月，其中农历正月十五（酥油花灯节）、四月法会期间最为热闹\n交通：西宁市区乘坐旅游专线或打车约40分钟可达' },
    ],
  },
  '2': {
    id: '2', name: '青海湖', address: '青海·海南州',
    cover: '/images/qinghahu/images/nn3.jpg', link: 'qinghahu/qinghaihu.html',
    sections: [
      { icon: 'fa-tint', title: '圣湖简介', content: '青海湖（藏语：མཚོ་སྔོན་པོ།，意为"蓝色的海"）是中国面积最大的内陆咸水湖，湖面海拔约3196米，面积约4583平方公里。在藏族文化中，青海湖被视为神圣之地，是安多藏区重要的宗教朝圣目的地。' },
      { icon: 'fa-twitter', title: '鸟岛奇观', content: '湖中的海西山（俗称"鸟岛"）是国家级自然保护区，每年4—7月有斑头雁、棕头鸥、鱼鸥、鸬鹚等10余万只候鸟在此繁殖栖息，场面壮观。' },
      { icon: 'fa-bicycle', title: '环湖骑行', content: '环湖公路全长约360公里，沿途经过二郎剑、黑马河、鸟岛、沙岛等著名景点。每年7—8月举办的"环青海湖国际公路自行车赛"是亚洲顶级赛事之一。' },
      { icon: 'fa-sun-o', title: '最佳游览时节', content: '最佳旅游时间：6月下旬—8月，此时湖畔油菜花盛开，金黄花海与碧蓝湖水相映，是拍摄大片的绝佳季节。' },
    ],
  },
  '3': {
    id: '3', name: '拉卜楞寺', address: '甘南·夏河',
    cover: '/images/labulengsi/images/lb8.jpg', link: 'labulengsi/labulengsi.html',
    sections: [
      { icon: 'fa-university', title: '六大学院', content: '拉卜楞寺（藏语：བླ་བྲང་བཀྲ་ཤིས་འཁིལ།）建于清康熙四十八年（1709年），是格鲁派六大寺院之一，也是安多地区最大的藏传佛教寺院群。寺内设有闻思学院、时轮学院、医学院等六大学院。' },
      { icon: 'fa-repeat', title: '世界最长转经廊', content: '拉卜楞寺拥有世界上最长的转经廊，全长约3公里，共设转经筒1750余个。每天清晨，信众手持转经筒沿廊缓行，场面庄严肃穆。' },
      { icon: 'fa-calendar-check-o', title: '宗教节庆活动', content: '拉卜楞寺全年举行多场重要的宗教活动：\n· 正月祈愿大法会（毛兰姆）\n· 四月法会：宗教舞蹈与演出\n· 七月法会：高僧讲经，万人朝圣' },
    ],
  },
  '4': {
    id: '4', name: '黄南热贡', address: '青海·黄南',
    cover: '/images/huangnan/images/hn1.jpg', link: 'huangnan/huangnan.html',
    sections: [
      { icon: 'fa-paint-brush', title: '热贡艺术', content: '热贡艺术发源于青海省黄南藏族自治州同仁市，已有逾700年历史。2009年被列入联合国教科文组织"人类非物质文化遗产代表作名录"。涵盖唐卡、堆绣、泥塑、建筑彩绘等多种艺术形式。' },
      { icon: 'fa-image', title: '唐卡艺术', content: '唐卡是藏族传统绘画的主要载体，以矿物颜料绘制于棉布或丝绸之上，色彩历经千年不褪。同仁唐卡工艺精湛，线条流畅，人物造型生动。' },
      { icon: 'fa-th-large', title: '堆绣工艺', content: '堆绣是用各色绸缎剪裁成各种形状，经填充后拼贴于布面，形成立体感极强的图案。与酥油花、壁画并称"塔尔寺三绝"。' },
    ],
  },
  '5': {
    id: '5', name: '日月山', address: '青海·湟源',
    cover: '/images/riyueshan/images/nn2.jpg', link: 'riyueshan/riyueshan.html',
    sections: [
      { icon: 'fa-user', title: '文成公主传说', content: '日月山海拔约3520米，是唐朝文成公主入藏途经之地。相传公主行至此处，取出皇帝所赐的日月宝镜，失手将宝镜摔落，化作两山，由此得名"日月山"。' },
      { icon: 'fa-history', title: '历史地位', content: '日月山古称"赤岭"，是唐代唐蕃古道上的重要关口，也是历史上农耕文明与游牧文明的天然分界线。这里也是古代丝绸之路和茶马互市的重要节点。' },
      { icon: 'fa-tree', title: '景区概况', content: '景区主要看点：\n· 日亭：建于山顶，可俯瞰日月山全貌\n· 月亭：陈列文成公主入藏历史资料\n· 倒淌河：中国少有的自东向西流的河流\n· 观光台：可俯瞰东西两侧截然不同的地貌景观' },
    ],
  },
  '6': {
    id: '6', name: '三江源', address: '青海·玉树',
    cover: '/images/sanjiangyuan/images/sjy.jpg', link: 'sanjiangyuan/sanjiangyuan.html',
    sections: [
      { icon: 'fa-leaf', title: '生态屏障', content: '三江源地区位于青海省南部，平均海拔4000米以上，是长江、黄河、澜沧江三大河流的发源地，被誉为"中华水塔"。每年向下游输送近600亿立方米的淡水。' },
      { icon: 'fa-tint', title: '三江源头', content: '· 长江源：沱沱河发源于唐古拉山脉各拉丹冬雪山\n· 黄河源：约古宗列曲发源于巴颜喀拉山北麓\n· 澜沧江源：扎曲发源于唐古拉山北麓，出境后成为湄公河' },
      { icon: 'fa-shield', title: '国家公园与保护', content: '2021年，三江源国家公园正式设立，面积约19.07万平方公里，是中国面积最大的国家公园。园内有雪豹、藏羚羊、野牦牛等珍稀野生动物。' },
    ],
  },
}

function onImgError(e: Event) {
  const target = e.target as HTMLImageElement
  target.src = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="960" height="460"><rect width="100%25" height="100%25" fill="%23f0ece6"/><text x="50%25" y="50%25" font-size="18" fill="%239B2B25" text-anchor="middle" dominant-baseline="middle">图片加载中...</text></svg>'
}

onMounted(() => {
  const id = String(route.params.id)
  if (!id) {
    errorMessage.value = '未指定景点 ID，请从首页重新进入。'
    error.value = true
    loading.value = false
    return
  }
  const data = SPOT_DATABASE[id]
  if (!data) {
    errorMessage.value = `未找到 ID 为 ${id} 的景点数据。`
    error.value = true
    loading.value = false
    return
  }
  setTimeout(() => {
    spot.value = data
    loading.value = false
  }, 500)
})
</script>

<style lang="scss" scoped>
.container { max-width: 960px; margin: 0 auto; padding: 30px 20px 80px; }

.loading-container { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 55vh; gap: 20px; }
.loading-spinner { width: 52px; height: 52px; border: 4px solid #f0ece6; border-top: 4px solid var(--primary-red); border-radius: 50%; animation: spin 0.9s linear infinite; }
@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
.loading-text { color: var(--text-secondary); font-size: 16px; }

.error-container { text-align: center; padding: 80px 20px; }
.error-icon { font-size: 56px; color: var(--primary-red); margin-bottom: 20px; }
.error-message { color: var(--text-secondary); font-size: 18px; margin-bottom: 28px; }

.back-btn { display: inline-flex; align-items: center; gap: 8px; padding: 12px 32px; background: linear-gradient(135deg, var(--primary-red), #B83C2E); color: white; border-radius: 8px; font-size: 16px; text-decoration: none; transition: var(--transition); &:hover { transform: translateY(-2px); box-shadow: var(--shadow-md); } }

.hero-banner { position: relative; width: 100%; height: 460px; overflow: hidden; border-radius: var(--radius-md); margin-bottom: 28px; box-shadow: var(--shadow-lg); img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.6s; } &:hover img { transform: scale(1.04); } }
.hero-overlay { position: absolute; inset: 0; background: linear-gradient(to bottom, transparent 35%, rgba(0,0,0,0.72) 100%); display: flex; flex-direction: column; justify-content: flex-end; padding: 36px; }
.hero-title { font-size: 38px; font-weight: 700; color: white; text-shadow: 2px 2px 6px rgba(0,0,0,0.5); margin-bottom: 8px; }
.hero-address { display: inline-flex; align-items: center; gap: 6px; background: rgba(212,175,55,0.9); color: #fff; padding: 5px 14px; border-radius: 20px; font-size: 14px; align-self: flex-start; }

.cards-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 28px; &.single-col { grid-template-columns: 1fr; } }
.content-card { background: var(--bg-secondary); border-radius: var(--radius-md); padding: 30px 28px; box-shadow: var(--shadow-sm); border-top: 3px solid var(--primary-gold); transition: var(--transition); &:hover { box-shadow: var(--shadow-md); transform: translateY(-3px); } &.full-width { grid-column: 1 / -1; } }
.card-header { display: flex; align-items: center; gap: 10px; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid #f0ece6; }
.card-icon { width: 36px; height: 36px; background: linear-gradient(135deg, var(--primary-red), #B83C2E); border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-size: 16px; flex-shrink: 0; }
.card-title { font-size: 18px; font-weight: 600; color: var(--primary-red); }
.card-body { font-size: 15px; line-height: 2; color: var(--text-secondary); white-space: pre-line; }

.action-bar { display: flex; justify-content: center; margin-top: 10px; }
.visit-btn { display: inline-flex; align-items: center; gap: 10px; padding: 15px 48px; background: linear-gradient(135deg, var(--primary-red), #B83C2E); color: white; text-decoration: none; border-radius: 40px; font-size: 17px; font-weight: 600; box-shadow: 0 6px 20px rgba(155,43,37,0.35); transition: var(--transition); &:hover { background: linear-gradient(135deg, var(--primary-gold), #E8C84A); transform: translateY(-3px); } }

@media (max-width: 768px) {
  .hero-banner { height: 300px; }
  .hero-title { font-size: 28px; }
  .cards-grid { grid-template-columns: 1fr; }
  .content-card.full-width { grid-column: 1; }
}
</style>
