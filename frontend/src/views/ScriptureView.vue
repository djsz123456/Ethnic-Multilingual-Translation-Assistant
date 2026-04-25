<template>
  <div class="scripture-page">
    <div class="page-header">
      <h1 class="main-title">藏文经典</h1>
      <div class="sub-title">བོད་ཀྱི་གསུང་རབ་</div>
      <div class="scripture-name">
        《喇荣课诵集·二十一度母礼赞》
        <span class="tibetan-name">སྒྲོལ་མ་ཉེར་གཅིག་གི་བསྟོད་པ།</span>
      </div>
    </div>

    <div class="toolbar">
      <button class="btn" @click="expandAll"><i class="fa fa-expand"></i> 全部展开</button>
      <button class="btn btn-outline" @click="collapseAll"><i class="fa fa-compress"></i> 全部收起</button>
      <button class="btn btn-outline" @click="toggleNightMode"><i class="fa fa-moon-o"></i> 夜间模式</button>
    </div>

    <div class="container">
      <div v-for="(line, index) in scriptureData" :key="index"
           class="verse-line"
           :class="{ expanded: line.isExpanded, active: line.isExpanded }"
           @click="toggleLine(index)">
        <span class="tibetan-text">{{ line.tibetan }}</span>
        <div class="translation">{{ line.chinese }}</div>
      </div>
    </div>

    <div class="footer-info">
      <p>《二十一度母礼赞》是藏传佛教中极具加持力的祈请文，常用于消除恐惧与灾难。</p>
      <p class="tibetan-footer">བརྒྱུད་སྡེ་བཅུ་གཅིག་གི་བསྟོད་པ་ནི་བོད་བརྒྱུད་ནང་བསྟན་དུ་བྱིན་རླབས་ཆེ་བའི་གསོལ་འདེབས་ཤིག་ཡིན།</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

interface VerseLine { tibetan: string; chinese: string; isExpanded: boolean }

const scriptureData = reactive<VerseLine[]>([
  { tibetan: "ཨོཾ་རྗེ་བཙུན་མ་འཕགས་མ་སྒྲོལ་མ་ལ་ཕྱག་འཚལ་ལོ།།", chinese: "嗡杰怎玛帕玛卓玛拉香擦洛 嗡！顶礼至尊圣度母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་སྒྲོལ་མ་མྱུར་མ་དགའ་བོ།།", chinese: "香擦卓玛涅玛华嫫 顶礼奋迅救度母", isExpanded: false },
  { tibetan: "སྤྱན་ནི་སྐད་ཅིག་གློག་དང་འདྲ་མ།།", chinese: "宣呢嘎结络当札玛 目如刹那闪电光", isExpanded: false },
  { tibetan: "འཇིག་རྟེན་གསུམ་མགོན་ཆུ་སྐྱེས་ཞལ་གྱི།།", chinese: "结定僧衮且基压杰 三域怙主莲花面", isExpanded: false },
  { tibetan: "གེ་སར་བྱེ་བ་པས་ནི་བྱུང་བ།།", chinese: "给萨学瓦类呢雄玛 花蕊绽放从中现", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་སྟོན་ཀའི་ཟླ་བ་ཀུན་ཏུ།།", chinese: "香擦敦格达瓦根德 顶礼月色白度母", isExpanded: false },
  { tibetan: "གང་བ་བརྒྱ་ནི་བརྩེགས་པའི་ཞལ་མ།།", chinese: "刚瓦嘉呢贼波压玛 秋百满月聚集脸", isExpanded: false },
  { tibetan: "སྐར་མ་སྟོང་ཕྲག་བརྩེགས་པ་རྣམས་ཀྱིས།།", chinese: "嘎玛东茶措巴南基 成千群星同汇聚", isExpanded: false },
  { tibetan: "རབ་ཏུ་བྱེ་བའི་འོད་རབ་འབར་བ།།", chinese: "高德学沃奥咼巴玛 尽放威光极灿然", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་གསེར་སྔོ་ཆུ་ནས་སྐྱེས་ཀྱི།།", chinese: "香擦色噢且内基杰 顶礼紫磨金色母", isExpanded: false },
  { tibetan: "པདྨས་ཕྱག་ནི་རྣམ་པར་བརྒྱན་བ།།", chinese: "巴美夏呢南巴坚玛 莲花庄严其妙手", isExpanded: false },
  { tibetan: "སྦྱིན་པ་བརྩོན་འགྲུས་དཀའ་ཐུབ་ནི་བ།།", chinese: "因巴尊这嘎特耶瓦 布施精进苦行静", isExpanded: false },
  { tibetan: "བཟོད་པ་བསམ་གཏན་སྤྱོད་ཡུལ་ཉིད་མ།།", chinese: "若巴三丹效耶涅玛 安忍禅定行境性", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་དེ་བཞིན་གཤིགས་པའི་གཙུག་ཏོར།།", chinese: "香擦得银夏波泽朵 顶礼如来顶髻母", isExpanded: false },
  { tibetan: "མཐའ་ཡས་རྣམ་པར་རྒྱལ་བར་སྤྱོད་མ།།", chinese: "塔耶南巴嘉瓦效玛 奉持无边尊胜行", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་ཏུདྡྷུ་རྃ་ཡི་གེས།།", chinese: "香擦德达咭吽叶给 顶礼吽音叱咤母", isExpanded: false },
  { tibetan: "འདོད་དང་ཕྱོགས་དང་ནམ་མཁའ་གང་བ།།", chinese: "多当效当南跨刚玛 声遍欲色虚空界", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་བརྒྱ་བྱིན་ལྷ་ཚངས་པ།།", chinese: "香擦嘉辛美拉苍巴 顶礼帝梵恭奉母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་ཏྲཊ་ཅེས་བྱ་དང་ཕཊ་ཀྱིས།།", chinese: "香擦扎极夏当啪基 顶礼胜伏他方母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་ཏུ་རེ་འཇིགས་པ་ཆེན་པོས།།", chinese: "香擦德热结巴钦布 顶礼大怖救度母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་དཀོན་མཆོག་གསུམ་མཚོན་ཕྱག་རྒྱའི།།", chinese: "香擦衮窍僧存夏吉 顶礼三宝严印母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་རབ་ཏུ་དགའ་བ་འཛིད་པའི།།", chinese: "香擦咭德嘎瓦节波 顶礼欢悦威德母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་ས་གཞི་སྐྱོང་བའི་ཚོགས་རྣམས།།", chinese: "香擦萨耶炯沃措南 顶礼解厄圣度母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་ཟླ་བའི་དུས་བུའི་དབུ་རྒྱན།།", chinese: "香擦达沃登威沃坚 顶礼月相冠冕母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་ཉི་མ་ཟླ་བ་རྒྱས་པའི།།", chinese: "香擦涅玛达瓦吉波 顶礼消疫圣度母", isExpanded: false },
  { tibetan: "ཕྱག་འཚལ་དེ་ཉིད་གསུམ་རྣམས་བཀོད་པས།།", chinese: "香擦得涅僧南果贝 顶礼具光胜度母", isExpanded: false },
  { tibetan: "རྩ་བའི་སྔགས་ཀྱིས་བསྟོད་པ་འདི་དང་ཕྱག་འཚལ་བ་ནི་ཉི་ཤུ་རྩ་གཅིག", chinese: "以根本咒礼赞二十一度母", isExpanded: false },
])

function toggleLine(index: number) {
  scriptureData[index].isExpanded = !scriptureData[index].isExpanded
}

function expandAll() {
  scriptureData.forEach(line => line.isExpanded = true)
}

function collapseAll() {
  scriptureData.forEach(line => line.isExpanded = false)
}

function toggleNightMode() {
  document.body.classList.toggle('night-mode')
}
</script>

<style lang="scss" scoped>
.page-header { text-align: center; padding: 40px 20px 20px; border-bottom: 1px solid rgba(155, 43, 37, 0.1); }
.main-title { color: var(--primary-red); font-size: 2rem; font-weight: 700; margin-bottom: 5px; }
.sub-title { font-family: 'Jomolhari', serif; font-size: 1.5rem; color: #7F8C8D; margin-bottom: 15px; }
.scripture-name { font-size: 1.1rem; color: var(--text-primary); font-weight: bold; }
.tibetan-name { font-family: 'Jomolhari', serif; color: var(--primary-red); margin-left: 10px; }

.toolbar {
  display: flex; justify-content: center; gap: 15px; padding: 20px; flex-wrap: wrap;
  position: sticky; top: 0; background: var(--bg-primary); z-index: 100;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}
.btn { padding: 8px 18px; border-radius: 20px; border: none; background: var(--primary-red); color: white; cursor: pointer; font-size: 0.9rem; display: flex; align-items: center; gap: 6px; transition: all 0.2s; &:hover { opacity: 0.9; transform: translateY(-1px); } }
.btn-outline { background: transparent; border: 1px solid var(--primary-red); color: var(--primary-red); }

.container { max-width: 800px; margin: 0 auto; padding: 20px; }

.verse-line { padding: 12px 15px; border-bottom: 1px solid #eee; cursor: pointer; transition: background 0.2s; &:hover { background: #fafafa; } &.active { background: #FFF5F5; border-left: 3px solid var(--primary-red); } }
.tibetan-text { font-family: 'Jomolhari', 'Microsoft Himalaya', serif; font-size: 1.4rem; line-height: 1.8; color: var(--text-primary); display: block; }
.translation { margin-top: 8px; padding: 10px; background: rgba(212, 175, 55, 0.1); border-radius: 8px; font-size: 0.95rem; color: #595959; line-height: 1.6; display: none; animation: fadeIn 0.3s ease; }
.verse-line.expanded .translation { display: block; }

@keyframes fadeIn { from { opacity: 0; transform: translateY(-5px); } to { opacity: 1; transform: translateY(0); } }

.footer-info { text-align: center; padding: 40px 20px; font-size: 0.85rem; color: #999; border-top: 1px solid #eee; margin-top: 40px; }
.tibetan-footer { margin-top: 5px; }

@media (max-width: 768px) {
  .main-title { font-size: 1.6rem; }
  .tibetan-text { font-size: 1.2rem; }
}
</style>

<style>
body.night-mode { background: #1a1a1a; color: #e0e0e0; }
body.night-mode .verse-line { border-bottom-color: #333; }
body.night-mode .verse-line:hover { background: #2c2c2c; }
body.night-mode .verse-line.active { background: #2c2c2c; }
body.night-mode .translation { background: rgba(255,255,255,0.05); color: #bbb; }
body.night-mode .toolbar { background: #1a1a1a; }
body.night-mode .page-header { border-bottom-color: #333; }
</style>
