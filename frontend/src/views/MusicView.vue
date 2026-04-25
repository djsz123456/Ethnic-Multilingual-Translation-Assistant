<template>
  <div class="music-page">
    <div class="page-header">
      <h1><i class="fa fa-music"></i> 藏族传统乐器 / བོད་ཀྱི་རོལ་མོ།</h1>
    </div>

    <div class="container">
      <section class="mandolin-section">
        <div class="showcase-grid">
          <div class="video-wrapper">
            <video controls :poster="videoUrl">
              <source :src="videoUrl" type="video/mp4" />
            </video>
          </div>
          <div class="info-panel">
            <h2>藏族传统乐器 - 曼陀铃</h2>
            <div class="bilingual-text">
              <div class="text-col">
                <p>曼陀铃（Mandolin）在藏区经过本土化改良，已成为安多藏族弹唱的核心乐器。其音色清脆明亮，常用于节庆、婚礼及日常娱乐。</p>
                <p class="origin-text">起源：源于西域，经丝绸之路传入，融合了藏族六弦琴的演奏技法。</p>
              </div>
              <div class="text-divider"></div>
              <div class="text-col tibetan">
                <p>མན་ཏོ་ལིན་ནི་བོད་ཁུལ་དུ་ཡུལ་ལུང་གི་ཁྱད་ཆོས་ལྡན་པར་གྱུར་ཏེ། ཨ་མདོ་བོད་རིགས་ཀྱི་གླུ་གར་གཏོང་བའི་རོལ་ཆ་གཙོ་བོར་གྱུར་ཡོད།</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="music-list">
        <div class="section-title">安多藏族传统音乐精选</div>
        <div class="filter-tags">
          <button v-for="cat in categories" :key="cat"
                  @click="currentCat = cat"
                  :class="['tag-btn', { active: currentCat === cat }]">
            {{ cat }}
          </button>
        </div>

        <div class="song-grid">
          <div v-for="song in filteredSongs" :key="song.id" class="song-card">
            <div style="position: relative;">
              <img :src="song.cover" class="cover-img" alt="封面" />
              <div class="play-overlay" @click="togglePlay(song)">
                <i class="fa fa-play-circle play-icon-lg"></i>
              </div>
            </div>
            <div class="song-info">
              <h3>{{ song.title }}</h3>
              <p>{{ song.artist }} · {{ song.type }}</p>
            </div>
            <div class="audio-controls">
              <button class="play-btn-sm" @click="togglePlay(song)">
                <i :class="playingSong === song.id ? 'fa fa-pause' : 'fa fa-play'"></i>
              </button>
              <div class="progress-bar"><div class="progress-fill" :style="{ width: song.progress + '%' }"></div></div>
              <span class="duration-text">{{ song.duration }}</span>
            </div>
          </div>
        </div>
      </section>

      <section class="upload-section">
        <div class="section-title">上传藏族传统音乐</div>
        <div class="upload-area" @click="fileInputRef?.click()">
          <i class="fa fa-cloud-upload upload-icon"></i>
          <p>点击或拖拽文件到此处上传</p>
          <p class="upload-hint">支持 MP3/WAV 格式，文件大小不超过 50MB</p>
          <input type="file" ref="fileInputRef" hidden accept=".mp3,.wav" @change="handleUpload" />
        </div>
      </section>
    </div>

    <div class="footer-note">传承安多藏族音乐文化，守护传统乐器之美</div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface Song {
  id: number
  title: string
  artist: string
  type: string
  duration: string
  progress: number
  cover: string
}

const videoUrl = '/music/movies/mantuolin1.mp4'
const fileInputRef = ref<HTMLInputElement | null>(null)
const categories = ['全部', '安多民歌', '曼陀铃弹唱', '器乐独奏', '酒歌']
const currentCat = ref('全部')
const playingSong = ref<number | null>(null)

const songs = ref<Song[]>([
  { id: 1, title: '慈祥的母亲 / བཀྲ་ཤིས་ཀྱི་ཨ་མ།', artist: '才旦卓玛', type: '安多民歌', duration: '04:12', progress: 0, cover: 'https://images.unsplash.com/photo-1504609773096-104ff2c73ba4?auto=format&fit=crop&w=400&q=80' },
  { id: 2, title: '金色的故乡 / གསེར་གྱི་ཕ་ཡུལ།', artist: '亚东', type: '曼陀铃弹唱', duration: '03:45', progress: 0, cover: 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=400&q=80' },
  { id: 3, title: '草原之夜 / རྩྭ་ཐང་གི་མཚན་མོ།', artist: '德德玛', type: '酒歌', duration: '05:01', progress: 0, cover: 'https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=400&q=80' },
  { id: 4, title: '雪山颂 / གངས་རིའི་བསྟོད་པ།', artist: '传统器乐', type: '器乐独奏', duration: '02:58', progress: 0, cover: 'https://images.unsplash.com/photo-1483729558449-99ef09a8c325?auto=format&fit=crop&w=400&q=80' },
])

const filteredSongs = computed(() => {
  if (currentCat.value === '全部') return songs.value
  return songs.value.filter(s => s.type === currentCat.value)
})

function togglePlay(song: Song) {
  if (playingSong.value === song.id) {
    playingSong.value = null
  } else {
    playingSong.value = song.id
    setTimeout(() => { song.progress = Math.min(song.progress + 10, 100) }, 1000)
  }
}

function handleUpload(e: Event) {
  const input = e.target as HTMLInputElement
  const file = input.files?.[0]
  if (file) alert(`成功选择文件: ${file.name}，准备上传...`)
}
</script>

<style lang="scss" scoped>
.page-header {
  text-align: center;
  padding: 40px 20px;
  border-bottom: 2px solid var(--primary-red);
  background: white;
  h1 { color: var(--primary-red); font-size: 2rem; display: flex; align-items: center; justify-content: center; gap: 15px; }
}

.container { max-width: 1200px; margin: 0 auto; padding: 30px 20px; }

.section-title {
  color: var(--primary-red); font-size: 1.5rem; margin-bottom: 25px;
  display: flex; align-items: center; gap: 10px;
  border-bottom: 1px solid rgba(155, 43, 37, 0.2); padding-bottom: 10px;
  &::before { content: '༄'; font-size: 1.8rem; color: var(--primary-gold); }
}

.showcase-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 30px; margin-bottom: 50px; }
.video-wrapper { background: #f0ece2; border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); aspect-ratio: 16/9; video { width: 100%; height: 100%; object-fit: cover; } }
.info-panel { background: white; padding: 30px; border-radius: var(--radius-md); box-shadow: var(--shadow-sm); h2 { color: var(--primary-red); text-align: center; } }
.bilingual-text { display: flex; gap: 20px; margin-top: 20px; }
.text-col { flex: 1; font-size: 0.95rem; &.tibetan { font-family: 'Jomolhari', serif; color: var(--primary-red); direction: rtl; } }
.text-divider { width: 1px; background: #eee; }
.origin-text { margin-top: 10px; font-size: 0.85rem; color: #666; }

.filter-tags { display: flex; gap: 10px; margin-bottom: 20px; flex-wrap: wrap; }
.tag-btn { padding: 8px 16px; border: 1px solid var(--primary-red); border-radius: 20px; background: transparent; color: var(--primary-red); cursor: pointer; transition: var(--transition); &.active, &:hover { background: var(--primary-red); color: white; } }

.song-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 20px; }
.song-card { background: var(--bg-secondary); border-radius: var(--radius-md); padding: 15px; box-shadow: var(--shadow-sm); transition: var(--transition); position: relative; &:hover { transform: translateY(-5px); box-shadow: var(--shadow-md); } }
.cover-img { width: 100%; aspect-ratio: 1/1; border-radius: 8px; background: #eee; margin-bottom: 12px; object-fit: cover; cursor: pointer; }
.play-overlay { position: absolute; top: 15px; left: 15px; right: 15px; aspect-ratio: 1/1; border-radius: 8px; background: rgba(0,0,0,0.3); display: flex; align-items: center; justify-content: center; opacity: 0; transition: var(--transition); .song-card:hover & { opacity: 1; } }
.play-icon-lg { font-size: 3rem; color: white; }
.song-info { h3 { font-size: 1rem; margin-bottom: 4px; } p { font-size: 0.85rem; color: var(--text-secondary); } }
.audio-controls { margin-top: 10px; display: flex; align-items: center; gap: 10px; }
.play-btn-sm { width: 32px; height: 32px; border-radius: 50%; background: var(--primary-red); color: white; border: none; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.progress-bar { flex: 1; height: 4px; background: #eee; border-radius: 2px; overflow: hidden; }
.progress-fill { height: 100%; background: var(--primary-gold); }
.duration-text { font-size: 0.75rem; color: #999; }

.upload-area { border: 2px dashed #ccc; border-radius: var(--radius-md); padding: 40px; text-align: center; background: #fafafa; margin-top: 50px; cursor: pointer; &:hover { border-color: var(--primary-red); background: #fff5f5; } }
.upload-icon { font-size: 3rem; color: var(--primary-red); margin-bottom: 15px; display: block; }
.upload-hint { font-size: 0.8rem; color: #999; }

.footer-note { text-align: center; padding: 40px; color: var(--primary-red); font-weight: bold; margin-top: 50px; }

@media (max-width: 768px) {
  .showcase-grid { grid-template-columns: 1fr; }
  .bilingual-text { flex-direction: column; }
  .text-divider { width: 100%; height: 1px; margin: 10px 0; }
}
</style>
