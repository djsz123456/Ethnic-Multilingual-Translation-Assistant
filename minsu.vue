NEW_FILE_CODE
<template>
  <div class="minsu-page">
    <!-- 顶部导航 -->
    <nav class="top-nav">
      <a href="index.html" class="nav-logo">
        <i class="fa fa-snowflake-o"></i>
        <span>雪域安多·藏韵传承</span>
      </a>
      <a href="index.html" class="nav-back">
        <i class="fa fa-arrow-left"></i>
        返回首页
      </a>
    </nav>

    <!-- 主内容 -->
    <div class="container">
      <!-- 错误页 -->
      <div v-if="error" class="error-page">
        <div class="error-icon"><i class="fa fa-frown-o"></i></div>
        <h2>未找到该民俗内容</h2>
        <p>请检查链接是否正确，或返回首页重新选择。</p>
        <a href="index.html" class="back-btn">
          <i class="fa fa-home"></i> 返回首页
        </a>
      </div>

      <!-- 内容渲染 -->
      <template v-else-if="folk">
        <!-- Hero Banner -->
        <div class="hero-banner">
          <img :src="folk.image" :alt="folk.title" @error="onImgError">
          <div class="hero-overlay">
            <div class="hero-category">
              <i class="fa fa-certificate"></i> {{ folk.category }}
            </div>
            <h1 class="hero-title">{{ folk.title }}</h1>
            <p class="hero-subtitle">{{ folk.subtitle }}</p>
          </div>
        </div>

        <!-- 内容卡片 -->
        <div
          v-for="(sec, idx) in folk.sections"
          :key="idx"
          class="card"
        >
          <div class="card-header">
            <div class="card-icon">
              <i :class="'fa ' + sec.icon"></i>
            </div>
            <h2 class="card-title">{{ sec.title }}</h2>
          </div>
          <div class="card-body">
            <p v-for="(para, pIdx) in sec.content" :key="pIdx">
              {{ para }}
            </p>
          </div>
        </div>

        <!-- 返回按钮 -->
        <div class="back-btn-wrap">
          <a href="index.html" class="back-btn">
            <i class="fa fa-arrow-left"></i> 返回首页
          </a>
        </div>
      </template>
    </div>

    <!-- 返回顶部按钮 -->
    <button
      v-show="showBackToTop"
      class="back-to-top"
      @click="scrollToTop"
      aria-label="返回顶部"
    >
      <i class="fa fa-arrow-up"></i>
    </button>

    <!-- Footer -->
    <footer class="page-footer">
      <p>青海安多藏族文化传播平台 &copy; 2026 &nbsp;·&nbsp; 探寻安多秘境，传承藏韵文化</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'Minsu',
  data() {
    return {
      loading: true,
      error: false,
      folk: null,
      showBackToTop: false,
      FOLK_DATA: {
        1: {
          id: 1,
          title: '藏族情歌《拉伊》',
          subtitle: '安多草原上流传千年的爱情吟唱',
          category: '非物质文化遗产',
          image: 'guoluo/images/gl.jpg',
          sections: [
            {
              icon: 'fa-history',
              title: '来源与历史',
              content: [
                '《拉伊》（藏语：ལ་གཞས།）是流行于青海安多藏区的一种传统民歌，已有数百年历史。"拉伊"字面意思为"山歌"或"野歌"，因多在山野草原间演唱而得名，是藏族青年男女表达爱慕之情的重要方式。',
                '安多《拉伊》尤以果洛、黄南、海南等藏族聚居地最为盛行，是藏族口头文学的重要组成部分，已被列入国家级非物质文化遗产名录。'
              ]
            },
            {
              icon: 'fa-music',
              title: '表现形式',
              content: [
                '《拉伊》曲调高亢悠扬，节奏自由舒展，演唱者常以假声（头声）行腔，拖腔绵长，回荡山谷之间。歌词多即兴创作，以比兴手法借助草原、雄鹰、格桑花等自然意象表达内心情感。',
                '演唱形式分为独唱和对唱两种，对唱时男女双方轮流吟唱，以歌词机智诙谐、意境深远者为胜，体现了藏族青年的才情与智慧。'
              ]
            },
            {
              icon: 'fa-heart',
              title: '文化意义',
              content: [
                '《拉伊》承载着安多藏族的爱情观与人生哲学，反映了藏族人民对自由、美好生活的热烈向往。它不仅是青年男女的恋爱媒介，更是藏族文化认同与社区凝聚的精神纽带。',
                '在藏族社会中，能唱一手好《拉伊》被视为才华与魅力的象征，许多美好姻缘正是通过这种歌唱对话结下的。'
              ]
            },
            {
              icon: 'fa-users',
              title: '传承现状',
              content: [
                '随着现代流行文化的冲击，《拉伊》的传承面临挑战，掌握传统演唱技艺的民间艺人日益减少。目前，青海省已将《拉伊》保护列入重点工程，在玛沁、同仁等地设立传习所，鼓励年轻一代学习演唱。',
                '每年夏季的草原歌会和各类民俗节庆活动，仍是《拉伊》展示与传承的重要舞台，吸引大量藏族青年参与，令这一古老艺术焕发新的生命力。'
              ]
            }
          ]
        },
        2: {
          id: 2,
          title: '安多藏戏',
          subtitle: '高原舞台上活态传承的千年戏剧',
          category: '传统戏剧',
          image: 'huangnan/images/hn1.jpg',
          sections: [
            {
              icon: 'fa-flag',
              title: '起源',
              content: [
                '安多藏戏是藏戏大家族中的重要流派，起源于藏传佛教寺院的宗教仪式表演，后逐渐吸收民间歌舞、杂技等元素，发展为具有完整戏剧结构的舞台艺术，迄今已有数百年历史。',
                '与卫藏藏戏（蓝面具戏）相比，安多藏戏在唱腔、表演风格和服饰上独具特色，以黄南同仁、海南贵德为主要传承地，于2006年被列入国家级非物质文化遗产名录。'
              ]
            },
            {
              icon: 'fa-book',
              title: '经典剧目',
              content: [
                '安多藏戏的传统剧目多取材于佛教故事和藏族英雄传说，经典剧目包括《诺桑法王》《卓娃桑姆》《朗萨雯蚌》《顿月顿珠》等八大传统藏戏，以及根据《格萨尔王传》改编的系列剧目。',
                '这些剧目以善恶有报、因果轮回为主题，融入藏族历史、文化与价值观，既有教化功能，又具极强的艺术感染力。'
              ]
            },
            {
              icon: 'fa-star',
              title: '表演特点',
              content: [
                '安多藏戏表演综合了唱、念、做、打多种技艺，演员头戴面具（象征不同神灵与人物），身穿色彩鲜艳的戏服，以独特的步法、手势和程式化动作展现角色。演出通常在露天广场进行，配以鼓、钹等打击乐器。',
                '唱腔是安多藏戏的灵魂，高亢激越，气势磅礴，一出完整的藏戏演出往往持续一整天甚至数天，场面宏大，观众席地围坐，气氛热烈而虔诚。'
              ]
            },
            {
              icon: 'fa-leaf',
              title: '传承',
              content: [
                '安多藏戏主要依靠师徒口传心授方式传承，黄南州同仁市设有专门的藏戏团，常年进行创作与演出，是安多藏戏传承的核心力量。每年雪顿节、藏历新年等重大节庆期间，藏戏演出是必不可少的重头节目。',
                '近年来，借助数字化保护项目，大量珍贵的安多藏戏表演影像和唱腔资料得到系统整理，为这一古老艺术的保护与传播提供了有力支撑。'
              ]
            }
          ]
        },
        3: {
          id: 3,
          title: '雪顿节',
          subtitle: '藏区最盛大的节日——酸奶宴与晒佛节',
          category: '传统节日',
          image: 'taersi/images/ts1.jpg',
          sections: [
            {
              icon: 'fa-sun-o',
              title: '节日起源',
              content: [
                '雪顿节（藏语：ཞོ་སྟོན།，Zhoton）藏语意为"酸奶宴"，起源于17世纪藏传佛教寺院制度。按藏传佛教戒律，夏季安居期间僧侣不得外出，以免踩踏土地中的虫蚁。安居期满后，信众以酸奶招待僧侣，逐渐演变为节日。',
                '后来，雪顿节与大型藏戏表演、展佛活动相结合，规模不断扩大，成为藏区一年中规模最宏大的综合性传统节日。'
              ]
            },
            {
              icon: 'fa-calendar',
              title: '时间',
              content: [
                '雪顿节一般在藏历六月底至七月初举行，公历通常对应7月下旬至8月上旬。以拉萨的雪顿节最为著名，持续约一周；而塔尔寺的雪顿节亦吸引大批信众和游客。',
                '塔尔寺的展佛仪式是青海雪顿节最重要的活动，届时将巨幅释迦牟尼唐卡铺展于山坡之上，供信众瞻仰礼拜，场面极为壮观震撼。'
              ]
            },
            {
              icon: 'fa-star',
              title: '活动内容',
              content: [
                '雪顿节期间活动丰富多彩：展佛——将珍贵巨型唐卡铺展于山坡晾晒供奉；藏戏——各剧团连续演出数天传统藏戏；集市——汇聚各地物产与手工艺品；赛马、射箭、藏族歌舞表演等民俗活动也一应俱全。',
                '信众们盛装出席，与家人共食酸奶，互赠祝福，整个节庆期间弥漫着浓厚的宗教氛围与欢乐气息，是藏族文化最集中、最生动的展示舞台。'
              ]
            },
            {
              icon: 'fa-map-marker',
              title: '各地习俗',
              content: [
                '青海塔尔寺的雪顿节以"晒大佛"最为著名，清晨时分，僧侣们将绣有释迦牟尼像的巨幅唐卡徐徐展开，信众叩首跪拜，香烟缭绕，场面令人动容。',
                '黄南州同仁的雪顿节则以大型藏戏演出见长，多个藏戏团同台竞技；果洛州的雪顿节与赛马节相结合，融宗教庆典与游牧文化节庆于一体，彰显安多藏区独特的文化风貌。'
              ]
            }
          ]
        },
        4: {
          id: 4,
          title: '转湖习俗',
          subtitle: '环绕圣湖的朝圣之旅',
          category: '宗教习俗',
          image: 'qinghahu/images/nn3.jpg',
          sections: [
            {
              icon: 'fa-heart',
              title: '朝圣意义',
              content: [
                '转湖（藏语：མཚོ་སྐོར།）是藏传佛教信众围绕圣湖顺时针行走的宗教朝圣活动。藏族人相信，圣湖是神灵居所，绕湖转行能消除业障、积累功德、祈福禳灾。青海湖（藏语：错温波，意为"青色的大湖"）是安多地区最神圣的湖泊，也是藏族人心中的圣地。',
                '转湖不仅是一种宗教修行，更是心灵净化与自我超越的旅程，许多信众一生中会多次完成转湖，以示虔诚。'
              ]
            },
            {
              icon: 'fa-road',
              title: '转湖线路',
              content: [
                '青海湖周长约360公里，完整转湖一圈需步行约7至10天。传统徒步转湖路线沿湖岸展开，途经鸟岛、沙岛、二郎剑等著名景点，沿途可见大量玛尼石堆、经幡和嘛呢旗帜。',
                '近年来，骑行转湖日趋流行，每年7至8月的青海湖国际公路自行车赛吸引大批骑行爱好者参与环湖骑行，将宗教朝圣传统与现代运动文化相结合。'
              ]
            },
            {
              icon: 'fa-flag',
              title: '转湖习俗',
              content: [
                '转湖途中，朝圣者手持转经筒，口诵六字真言（嗡嘛呢叭咪吽），每隔一段路便向湖面抛洒糌粑粉或风马旗，祈愿随风飘散至天地四方。遇到玛尼堆时，信众会添放石块并绕行三圈。',
                '在特定圣地和泉眼处，朝圣者会取水饮用或带回家中，认为圣湖之水具有加持与疗愈之力。同行的朝圣者之间互相扶助、同甘共苦，彰显藏族社区的团结精神。'
              ]
            },
            {
              icon: 'fa-clock-o',
              title: '最佳时间',
              content: [
                '转湖的最佳季节为每年藏历4月至9月，对应公历5月至10月，此时气候温和，湖畔草场青翠，油菜花盛开，风光绝美。其中藏历6月15日（公历7月中下旬）前后是转湖的传统高峰期，信众最为集中。',
                '每逢藏历马年，转湖的宗教意义尤为殊胜，信众相信马年转湖一圈的功德相当于平常年份的十二倍，届时来自全国各地的藏族信众蜂拥而至，场面蔚为壮观。'
              ]
            }
          ]
        },
        5: {
          id: 5,
          title: '煨桑仪式',
          subtitle: '以烟为语，与天地神灵沟通的古老礼仪',
          category: '宗教仪式',
          image: 'sangkecaoyuan/images/skcy1.jpg',
          sections: [
            {
              icon: 'fa-history',
              title: '仪式背景',
              content: [
                '煨桑（藏语：བསང་།，Sang）是藏传佛教及苯教传统中一种以烟火祭祀为核心的宗教仪式，历史可追溯至苯教时代，后被藏传佛教吸纳融合，成为藏区最普遍的日常宗教活动之一。',
                '"桑"意为"净化"，煨桑仪式通过燃烧芳香植物产生的烟雾，象征向神灵献供、净化污秽、祈求吉祥，是藏族人与天地神灵沟通的重要媒介。'
              ]
            },
            {
              icon: 'fa-list-ol',
              title: '仪式步骤',
              content: [
                '煨桑仪式通常在清晨日出时分举行，选择寺庙外、山顶或水边的专用桑炉处进行。首先由喇嘛或长者点燃桑火，随后依次添加燃料，同时诵念相应的祈祷经文和咒语。',
                '信众围绕桑炉顺时针转行，手持哈达和风马旗，向火中抛洒供品，最后将风马旗抛向空中，任其随烟飘散。整个仪式庄严肃穆，参与者心怀虔诚，专注祈愿。'
              ]
            },
            {
              icon: 'fa-leaf',
              title: '用料',
              content: [
                '煨桑的核心燃料为柏树枝（尤以柏枝为最佳），因其清香之气被认为能愉悦神灵。此外还会加入糌粑（炒青稞粉）、酥油、红糖、奶渣、干果、茶叶等食物供品，以及藏香、藏药草等芳香植物。',
                '不同的祈愿目的使用不同的配料组合：祈福长寿用菊花、藏红花；消灾驱邪用艾草、杜鹃枝；祈求丰收用五谷；祈求健康用藏医草药。'
              ]
            },
            {
              icon: 'fa-star',
              title: '文化意义',
              content: [
                '煨桑仪式贯穿藏族人的整个生命历程——出生、成年、婚嫁、远行、节庆、丧葬等重要时刻都离不开煨桑，是藏族信仰体系与日常生活深度融合的生动体现。',
                '在安多藏区，桑炉是寺庙与村落中最重要的公共宗教设施，每日清晨升腾的桑烟是藏区最典型的文化符号之一，承载着一方百姓对美好生活的期许与对神圣力量的敬畏。'
              ]
            }
          ]
        },
        6: {
          id: 6,
          title: '藏历新年',
          subtitle: '洛萨节——雪域高原辞旧迎新的盛典',
          category: '传统节日',
          image: 'sanjiangyuan/images/sjy.jpg',
          sections: [
            {
              icon: 'fa-calendar',
              title: '时间计算',
              content: [
                '藏历新年（藏语：ལོ་གསར།，Losar）是藏族一年中最重要的传统节日，时间依据藏历推算，通常在公历2月至3月之间，与汉族春节相近但不完全重合，有时相差数周。',
                '藏历以阴阳五行和天干地支为基础结合月相推算，每年的确切日期由各大寺院的天文历算师根据传统方法测定发布，全藏区以此为准。'
              ]
            },
            {
              icon: 'fa-home',
              title: '节前准备',
              content: [
                '藏历十二月下旬，家家户户开始打扫房屋、粉刷墙壁、制作节庆食品。最具代表性的准备工作是制作"卡赛"（油炸糕点）和"切玛"（五谷丰登的盒装供品），以及在屋顶悬挂崭新的经幡。',
                '除夕（藏历十二月二十九日）家人共吃"古突"（九宝面疙瘩汤），汤中包有石子、羊毛、辣椒、木炭等不同东西，据说吃到不同的东西预示着不同的运势，气氛欢乐热闹。'
              ]
            },
            {
              icon: 'fa-star',
              title: '节日活动',
              content: [
                '藏历正月初一，家人互赠"洛萨扎西德勒"（新年吉祥如意）的祝福，喝青稞酒、吃卡赛。寺院举行大规模法会，喇嘛们身着盛装诵经祈祷，跳"羌姆"（神舞，亦称跳神），驱邪纳吉。',
                '正月初三至初五，村民聚集广场，举行赛马、射箭、拔河、锅庄舞等传统娱乐活动，歌声舞影弥漫整个村落。寺庙的辩经和晒佛活动也是重要节目，信众络绎不绝。'
              ]
            },
            {
              icon: 'fa-map-marker',
              title: '各地差异',
              content: [
                '安多藏区各地的藏历新年习俗各有侧重：果洛州的新年庆典与格萨尔文化紧密相连，民间说唱艺人专门演唱新年版格萨尔故事；黄南州同仁以藏戏表演开年；海南州共和县的煨桑祈福规模尤为盛大。',
                '塔尔寺的正月祈愿大法会（毛兰姆）吸引来自各地的数万名信众，寺院内外人山人海，酥油花展、晒佛等活动令人叹为观止，是安多藏历新年最壮观的宗教盛典。'
              ]
            }
          ]
        }
      }
    }
  },

  mounted() {
    this.loadFolk()
    window.addEventListener('scroll', this.handleScroll)
  },

  beforeUnmount() {
    window.removeEventListener('scroll', this.handleScroll)
  },

  methods: {
    loadFolk() {
      const params = new URLSearchParams(window.location.search)
      const id = parseInt(params.get('id'), 10)

      if (!id || !this.FOLK_DATA[id]) {
        this.error = true
        this.loading = false
        return
      }

      this.folk = this.FOLK_DATA[id]
      document.title = this.folk.title + ' - 民俗文化详情 - 雪域安多·藏韵传承'
      this.loading = false
    },

    onImgError(e) {
      e.target.src = 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="920" height="460"><rect width="100%25" height="100%25" fill="%23f0ece6"/><text x="50%25" y="50%25" font-size="18" fill="%239B2B25" text-anchor="middle" dominant-baseline="middle">图片加载中...</text></svg>'
    },

    handleScroll() {
      this.showBackToTop = window.pageYOffset > 300
    },

    scrollToTop() {
      window.scrollTo({ top: 0, behavior: 'smooth' })
    }
  }
}
</script>

<style scoped>
:root {
  --primary-red: #9B2B25;
  --primary-gold: #D4AF37;
  --secondary-orange: #E8A87C;
  --bg-primary: #FAF9F7;
  --bg-secondary: #FFFFFF;
  --text-primary: #2C3E50;
  --text-secondary: #5D6D7E;
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.08);
  --shadow-md: 0 4px 16px rgba(0, 0, 0, 0.12);
  --shadow-lg: 0 8px 32px rgba(0, 0, 0, 0.15);
  --radius-md: 12px;
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.minsu-page * {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Microsoft YaHei", "Noto Sans SC", sans-serif;
}

.minsu-page {
  background-color: var(--bg-primary);
  color: var(--text-primary);
  line-height: 1.6;
  overflow-x: hidden;
}

/* 顶部导航 */
.top-nav {
  background: linear-gradient(135deg, var(--primary-red) 0%, #7a1f1a 100%);
  padding: 0 30px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: var(--shadow-md);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  text-decoration: none;
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 1px;
}

.nav-logo i {
  color: var(--primary-gold);
  font-size: 22px;
}

.nav-back {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  font-size: 15px;
  padding: 8px 18px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  transition: var(--transition);
}

.nav-back:hover {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  border-color: rgba(255, 255, 255, 0.6);
}

/* 主容器 */
.container {
  max-width: 920px;
  margin: 0 auto;
  padding: 40px 20px 80px;
}

/* 错误页 */
.error-page {
  text-align: center;
  padding: 100px 20px;
}

.error-page .error-icon {
  font-size: 72px;
  color: #ddd;
  margin-bottom: 24px;
}

.error-page h2 {
  font-size: 24px;
  color: var(--text-secondary);
  margin-bottom: 16px;
}

.error-page p {
  font-size: 16px;
  color: #aaa;
  margin-bottom: 30px;
}

/* Hero Banner */
.hero-banner {
  position: relative;
  width: 100%;
  height: 460px;
  overflow: hidden;
  border-radius: var(--radius-md);
  margin-bottom: 36px;
  box-shadow: var(--shadow-lg);
}

.hero-banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.hero-banner:hover img {
  transform: scale(1.04);
}

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom, transparent 30%, rgba(0, 0, 0, 0.72) 100%);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 40px 36px;
}

.hero-category {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: var(--primary-gold);
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  padding: 4px 14px;
  border-radius: 20px;
  margin-bottom: 14px;
  width: fit-content;
  letter-spacing: 0.5px;
}

.hero-title {
  font-size: 38px;
  font-weight: 700;
  color: white;
  text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.5);
  margin-bottom: 10px;
  line-height: 1.3;
}

.hero-subtitle {
  font-size: 17px;
  color: rgba(255, 255, 255, 0.88);
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.5);
}

/* 内容卡片 */
.card {
  background: var(--bg-secondary);
  border-radius: var(--radius-md);
  padding: 36px 40px;
  margin-bottom: 24px;
  box-shadow: var(--shadow-sm);
  border-left: 4px solid var(--primary-red);
  transition: var(--transition);
}

.card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--bg-primary);
}

.card-icon {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, var(--primary-red), #b83c2e);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 19px;
  flex-shrink: 0;
}

.card-title {
  font-size: 21px;
  font-weight: 600;
  color: var(--primary-red);
}

.card-body {
  font-size: 15.5px;
  line-height: 2;
  color: var(--text-primary);
}

.card-body p {
  margin-bottom: 12px;
}

.card-body p:last-child {
  margin-bottom: 0;
}

/* 返回按钮 */
.back-btn-wrap {
  text-align: center;
  margin-top: 40px;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 14px 40px;
  background: linear-gradient(135deg, var(--primary-red), #b83c2e);
  color: white;
  text-decoration: none;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 500;
  box-shadow: var(--shadow-md);
  transition: var(--transition);
}

.back-btn:hover {
  background: linear-gradient(135deg, var(--primary-gold), #e8c84a);
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
}

/* 返回顶部按钮 */
.back-to-top {
  position: fixed;
  bottom: 30px;
  right: 30px;
  width: 50px;
  height: 50px;
  background: var(--primary-red);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  cursor: pointer;
  box-shadow: var(--shadow-md);
  border: none;
  z-index: 998;
  transition: var(--transition);
}

.back-to-top:hover {
  background: var(--primary-gold);
  transform: translateY(-3px);
}

/* Footer */
.page-footer {
  background: var(--primary-red);
  color: rgba(255, 255, 255, 0.85);
  text-align: center;
  padding: 24px 20px;
  font-size: 14px;
}

/* 响应式 */
@media (max-width: 768px) {
  .top-nav {
    padding: 0 16px;
  }

  .nav-logo span {
    display: none;
  }

  .hero-banner {
    height: 300px;
  }

  .hero-title {
    font-size: 26px;
  }

  .hero-overlay {
    padding: 24px 20px;
  }

  .card {
    padding: 24px 20px;
  }

  .container {
    padding: 24px 14px 60px;
  }
}

@media (max-width: 480px) {
  .hero-banner {
    height: 240px;
  }

  .hero-title {
    font-size: 22px;
  }

  .card-title {
    font-size: 18px;
  }
}
</style>