<template>
  <div class="translate-page">
    <div class="page-header">
      <h1>藏汉互译工具</h1>
      <p>支持藏语 ⇄ 汉语双向翻译，由MyMemory翻译API提供支持</p>
      <span class="badge"><i class="fa fa-language"></i> བོད་སྐད་ · 中文</span>
    </div>

    <div class="main">
      <div class="direction-bar">
        <label>翻译方向：</label>
        <button :class="['btn-dir', { active: srcLang === 'zh' }]" @click="setDirection('zh', 'bo')">
          <i class="fa fa-arrow-right"></i> 汉语 → 藏语
        </button>
        <button :class="['btn-dir', { active: srcLang === 'bo' }]" @click="setDirection('bo', 'zh')">
          <i class="fa fa-arrow-right"></i> 藏语 → 汉语
        </button>
        <button class="btn-swap" @click="swapDirection" title="互换方向">
          <i class="fa fa-exchange"></i> 互换
        </button>
      </div>

      <div class="translate-card">
        <div class="panel-row">
          <div class="panel">
            <div class="panel-header">
              <span class="lang-label"><i class="fa fa-pencil"></i> {{ LANG_NAMES[srcLang] }}输入</span>
              <span class="char-count">{{ inputText.length }} 字</span>
            </div>
            <textarea :class="['text-area', { tibetan: srcLang === 'bo' }]"
                      v-model="inputText"
                      :placeholder="`请输入需要翻译的${LANG_NAMES[srcLang]}…`"
                      maxlength="2000"></textarea>
            <div class="panel-footer">
              <button class="action-btn" @click="clearAll"><i class="fa fa-trash-o"></i> 清空</button>
            </div>
          </div>

          <div class="divider-col">
            <i class="fa fa-arrow-right arrow-icon"></i>
          </div>

          <div class="panel">
            <div class="panel-header">
              <span class="lang-label"><i class="fa fa-check"></i> {{ LANG_NAMES[tgtLang] }}结果</span>
            </div>
            <textarea :class="['text-area', 'output', { tibetan: tgtLang === 'bo' }]"
                      v-model="outputText"
                      readonly
                      placeholder="翻译结果将显示在这里…"></textarea>
            <div class="panel-footer">
              <button class="action-btn" @click="copyResult"><i class="fa fa-copy"></i> 复制</button>
            </div>
          </div>
        </div>

        <div class="translate-btn-wrap">
          <button class="translate-btn" @click="doTranslate" :disabled="translating">
            <i :class="translating ? 'fa fa-spinner fa-spin' : 'fa fa-language'"></i>
            {{ translating ? '翻译中…' : '开始翻译' }}
          </button>
        </div>

        <div :class="['status-bar', statusType]">{{ statusMsg }}</div>
      </div>

      <div class="info-card">
        <h2><i class="fa fa-info-circle"></i> 使用说明</h2>
        <ul class="info-list">
          <li>支持藏语（བོད་སྐད་）⇄ 汉语双向翻译。</li>
          <li>由MyMemory翻译API提供支持，单次最多 2000 字符。</li>
          <li>翻译结果仅供学习与参考，正式文件请人工核对。</li>
          <li>如翻译失败，请检查网络连接是否畅通。</li>
          <li>支持快捷键 Ctrl+Enter 快速翻译。</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const LANG_NAMES: Record<string, string> = { zh: '汉语', bo: '藏语' }

const srcLang = ref('zh')
const tgtLang = ref('bo')
const inputText = ref('')
const outputText = ref('')
const translating = ref(false)
const statusMsg = ref('')
const statusType = ref('')

function setDirection(src: string, tgt: string) {
  srcLang.value = src
  tgtLang.value = tgt
  outputText.value = ''
  setStatus('')
}

function swapDirection() {
  const out = outputText.value
  const [s, t] = srcLang.value === 'zh' ? ['bo', 'zh'] : ['zh', 'bo']
  setDirection(s, t)
  inputText.value = out
  outputText.value = ''
}

function clearAll() {
  inputText.value = ''
  outputText.value = ''
  setStatus('')
}

function copyResult() {
  const out = outputText.value.trim()
  if (!out) { setStatus('没有可复制的内容', 'error'); return }
  navigator.clipboard.writeText(out).then(() => {
    setStatus('已复制到剪贴板', 'success')
  }).catch(() => {
    setStatus('复制失败', 'error')
  })
}

function setStatus(msg: string, type?: string) {
  statusMsg.value = msg
  statusType.value = type || ''
}

async function doTranslate() {
  const q = inputText.value.trim()
  if (!q) { setStatus('请先输入需要翻译的文字', 'error'); return }
  translating.value = true
  setStatus('正在翻译…')
  const langpair = srcLang.value === 'zh' ? 'zh-CN|bo' : 'bo|zh-CN'
  const url = `https://api.mymemory.translated.net/get?q=${encodeURIComponent(q)}&langpair=${langpair}`
  try {
    const resp = await fetch(url, { signal: AbortSignal.timeout(10000) })
    if (!resp.ok) throw new Error('网络错误 ' + resp.status)
    const data = await resp.json()
    if (data.responseStatus === 200) {
      outputText.value = data.responseData.translatedText
      setStatus('翻译完成 ✓', 'success')
    } else {
      setStatus('翻译失败：' + (data.responseDetails || data.responseStatus), 'error')
    }
  } catch (e: unknown) {
    const err = e as Error
    if (err.name === 'TimeoutError') setStatus('请求超时，请检查网络', 'error')
    else setStatus('请求失败：' + err.message, 'error')
  } finally {
    translating.value = false
  }
}

function onKeydown(e: KeyboardEvent) {
  if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') doTranslate()
}

onMounted(() => document.addEventListener('keydown', onKeydown))
onUnmounted(() => document.removeEventListener('keydown', onKeydown))
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, var(--primary-red) 0%, #c0392b 100%);
  color: #fff; text-align: center; padding: 48px 20px 40px;
  h1 { font-size: 30px; font-weight: 700; letter-spacing: 2px; margin-bottom: 10px; }
  p { font-size: 15px; opacity: 0.85; margin-bottom: 20px; }
}
.badge { display: inline-block; background: rgba(255,255,255,0.18); border: 1px solid rgba(255,255,255,0.35); border-radius: 20px; padding: 4px 16px; font-size: 13px; i { margin-right: 6px; color: var(--primary-gold); } }

.main { max-width: 860px; margin: 0 auto; padding: 32px 20px 60px; }

.direction-bar {
  background: var(--bg-secondary); border-radius: var(--radius-md); box-shadow: var(--shadow-sm);
  padding: 18px 24px; margin-bottom: 20px; display: flex; align-items: center; gap: 12px; flex-wrap: wrap;
  label { font-size: 14px; color: var(--text-secondary); font-weight: 500; }
}
.btn-dir { border: 2px solid #e8e0d8; background: var(--bg-primary); color: var(--text-primary); border-radius: 8px; padding: 8px 20px; font-size: 14px; cursor: pointer; display: flex; align-items: center; gap: 6px; transition: all 0.2s; &.active { border-color: var(--primary-red); background: var(--primary-red); color: #fff; } &:hover:not(.active) { border-color: var(--primary-red); color: var(--primary-red); } }
.btn-swap { margin-left: auto; background: rgba(155,43,37,0.1); border: none; color: var(--primary-red); border-radius: 8px; padding: 8px 16px; cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 6px; transition: background 0.2s; &:hover { background: rgba(155,43,37,0.18); } }

.translate-card { background: var(--bg-secondary); border-radius: var(--radius-md); box-shadow: var(--shadow-sm); padding: 28px; margin-bottom: 20px; }
.panel-row { display: grid; grid-template-columns: 1fr auto 1fr; gap: 0; align-items: stretch; }
.panel { display: flex; flex-direction: column; }
.panel-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px; }
.lang-label { font-size: 13px; font-weight: 600; color: var(--primary-red); background: rgba(155,43,37,0.1); padding: 4px 12px; border-radius: 20px; display: flex; align-items: center; gap: 6px; }
.char-count { font-size: 12px; color: var(--text-secondary); }
.text-area { width: 100%; min-height: 180px; padding: 14px; border: 1.5px solid #e8e0d8; border-radius: 8px; font-size: 15px; line-height: 1.7; resize: vertical; font-family: inherit; background: #fff; transition: border-color 0.2s; &:focus { outline: none; border-color: var(--primary-red); } &.output { background: var(--bg-primary); color: var(--text-primary); } &.tibetan { font-size: 18px; line-height: 2; } }
.divider-col { width: 52px; display: flex; align-items: center; justify-content: center; padding: 0 4px; }
.arrow-icon { color: var(--text-secondary); font-size: 20px; }
.panel-footer { display: flex; justify-content: flex-end; margin-top: 8px; }
.action-btn { background: none; border: 1px solid #e8e0d8; border-radius: 6px; padding: 6px 14px; font-size: 13px; cursor: pointer; display: flex; align-items: center; gap: 5px; color: var(--text-secondary); transition: all 0.2s; &:hover { border-color: var(--primary-red); color: var(--primary-red); } }

.translate-btn-wrap { display: flex; justify-content: center; margin: 20px 0 10px; }
.translate-btn { background: linear-gradient(135deg, var(--primary-red), #c0392b); color: #fff; border: none; border-radius: 10px; padding: 14px 48px; font-size: 16px; font-weight: 600; cursor: pointer; display: flex; align-items: center; gap: 8px; transition: all 0.2s; box-shadow: 0 4px 12px rgba(155,43,37,0.3); &:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(155,43,37,0.4); } &:disabled { opacity: 0.7; cursor: not-allowed; } }

.status-bar { text-align: center; font-size: 13px; padding: 6px 0; min-height: 28px; color: var(--text-secondary); &.error { color: #c0392b; } &.success { color: #27ae60; } }

.info-card { background: var(--bg-secondary); border-radius: var(--radius-md); box-shadow: var(--shadow-sm); padding: 26px 28px; h2 { font-size: 17px; color: var(--text-primary); margin-bottom: 16px; display: flex; align-items: center; gap: 8px; i { color: var(--primary-red); } } }
.info-list { list-style: none; li { position: relative; padding: 7px 0 7px 20px; font-size: 14px; color: var(--text-secondary); border-bottom: 1px dashed #e8e0d8; &:last-child { border-bottom: none; } &::before { content: ""; position: absolute; left: 0; top: 50%; transform: translateY(-50%); width: 6px; height: 6px; border-radius: 50%; background: var(--primary-red); } } }

@media (max-width: 640px) {
  .panel-row { grid-template-columns: 1fr; }
  .divider-col { width: 100%; height: 36px; }
  .arrow-icon { transform: rotate(90deg); }
  .page-header h1 { font-size: 22px; }
  .btn-swap { margin-left: 0; }
}
</style>
