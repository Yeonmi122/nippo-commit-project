<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue"

// 年月
const today = new Date()
const year = ref(today.getFullYear())
const month = ref(today.getMonth() + 1)

// 日報ステータスAPI（提出済み日付）
const submittedSet = ref<Set<string>>(new Set())

const fetchStatus = async () => {
  const res = await fetch("/api/getNippoStatus")
  const data = await res.json()

  submittedSet.value = new Set(
    data.nippoStatus.map((d: any) => d.date)
  )
}
// 週報ステータスAPI（提出済み日付）
const shuhoSet = ref<Set<string>>(new Set())

const fetchShuhoStatus = async () => {
  const res = await fetch(`/api/getShuhoStatus?year=${year.value}&month=${month.value}`)
  const data = await res.json()

  shuhoSet.value = new Set(
    data.shuhoStatus.map((d: any) => d.date)
  )
}

onMounted(() => {
  fetchStatus()
  fetchShuhoStatus()
})

watch([year, month], () => {
  fetchStatus()
  fetchShuhoStatus()
})

// 月の日数
const calendarDays = computed(() => {
  const firstDay = new Date(year.value, month.value - 1, 1)
  const lastDate = new Date(year.value, month.value, 0).getDate()

  const startDay = firstDay.getDay() // 0:日曜

  const days = []

  // 前の空白
  for (let i = 0; i < startDay; i++) {
    days.push(null)
  }

  // 当月
  for (let i = 1; i <= lastDate; i++) {
    days.push(i)
  }

  return days
})

// 日付フォーマット
const formatDate = (day: number) => {
  return `${year.value}-${String(month.value).padStart(2,"0")}-${String(day).padStart(2,"0")}`
}

const parseDate = (dateStr: string) => {
  const [y, m, d] = dateStr.split("-").map(Number)
  return new Date(y, m - 1, d)
}

// 土日を取得する
const getDayOfWeek = (dateStr: string) => {
  return parseDate(dateStr).getDay() // 0:日曜, 6:土曜
}

// 日付状態判定
const getStatus = (dateStr: string) => {
  const todayCopy = new Date()
  const date = parseDate(dateStr)

  todayCopy.setHours(0,0,0,0)
  date.setHours(0,0,0,0)

  const day = getDayOfWeek(dateStr)

  // ① 日曜 → 何も表示しない
  if (day === 0) return "sunday"

  // ② 未来
  if (date > todayCopy) return "future"

  // ③ 土曜 → 週報判定
  if (day === 6) {
    if (shuhoSet.value.has(dateStr)) return "shuhoSubmitted"
    return "shuhoNotSubmitted"
  }

  // ④ 平日 → 日報判定
  if (submittedSet.value.has(dateStr)) return "submitted"
  return "notSubmitted"
}


// 前の月に移動
const prevMonth = () => {
  if (month.value === 1) {
    month.value = 12
    year.value--
  } else {
    month.value--
  }
}
// 次の月に移動
const nextMonth = () => {
  if (month.value === 12) {
    month.value = 1
    year.value++
  } else {
    month.value++
  }
}

// モーダル
const selectedReport = ref<any>(null)
const showModal = ref(false)

const handleClick = (day: number) => {
  const date = formatDate(day)

  if (!submittedSet.value.has(date)) return

  selectedReport.value = {
    date,
    title: "日報タイトル",
    time: "18:30",
    sender: "山田太郎",
    to: "チーム",
    content: "本日の業務内容..."
  }

  showModal.value = true
}
</script>

<template>
  <div class="layout">
    <!-- サイドバー -->
    <aside class="sidebar">
      <h2 class="logo">Dashboard</h2>
      <ul>
        <li class="active">ダッシュボード</li>
        <li>
          <router-link to="/report">日報・週報提出</router-link>
        </li>
      </ul>
    </aside>

    <!-- メイン -->
    <main class="main">
      <div class="header">
        <h1>日報・週報　提出カレンダー</h1>
      </div>

      <div class="calendar-header">
        <button class="arrow" @click="prevMonth">◀</button>
        <h2>{{ year }}年 {{ month }}月</h2>
        <button class="arrow" @click="nextMonth">▶</button>
      </div>

      <!-- カレンダー -->
      <div class="calendar">
        <div class="day-header" v-for="d in ['日','月','火','水','木','金','土']" :key="d">
          {{ d }}
        </div>

        <div
        v-for="(day, index) in calendarDays"
        :key="index"
        :class="['cell', day ? getStatus(formatDate(day)): 'empty']"
        @click="day && handleClick(day)"
        >
          <div class="date">{{ day }}</div>
          
          <template v-if="day && getStatus(formatDate(day)) !== 'future'">
            <!-- 日報 -->
            <div v-if="getStatus(formatDate(day)) === 'submitted'" class="badge success">
              ✓ 日報済
            </div>

            <div v-else-if="getStatus(formatDate(day)) === 'notSubmitted'" class="badge error">
              × 未提出
            </div>

            <!-- 週報（土曜） -->
            <div v-else-if="getStatus(formatDate(day)) === 'shuhoSubmitted'" class="badge weekly">
            ✓ 週報済
            </div>

            <div v-else-if="getStatus(formatDate(day)) === 'shuhoNotSubmitted'" class="badge error">
              × 週報未提出
            </div>
              <!-- 日曜は何も表示しない（何も書かない） -->
          </template>
        </div>
      </div>
    </main>

    <!-- モーダル -->
    <div v-if="showModal" class="overlay">
      <div class="modal">
        <button class="close" @click="showModal=false">×</button>

        <h2>日報詳細</h2>
        <p>{{ selectedReport.date }}</p>
        <p>件名：{{ selectedReport.title }}</p>
        <p>送信：{{ selectedReport.time }}</p>
        <p>送信者：{{ selectedReport.sender }}</p>
        <p>宛先：{{ selectedReport.to }}</p>

        <div class="content">
          {{ selectedReport.content }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  font-family: "Segoe UI", sans-serif;
}

/* サイドバー */
.sidebar {
  width: 220px;
  background: #f8f9fb;
  padding: 20px;
}

.logo {
  margin-bottom: 20px;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  padding: 10px;
  border-radius: 6px;
  cursor: pointer;
}

.sidebar li.active {
  background: #e6f0ff;
}

/* メイン */
.main {
  flex: 1;
  padding: 20px;
}

/* ヘッダー */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.submit-btn {
  background: #3b82f6;
  color: white;
  padding: 10px 14px;
  border-radius: 8px;
  border: none;
}

/* カレンダー */
.calendar {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
  margin-top: 20px;
}

.day-header {
  font-weight: bold;
  text-align: center;
}

.cell {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 8px;
  height: 100px;
  cursor: pointer;
  position: relative;
}

.date {
  font-size: 14px;
}

/* バッジ */
.badge {
  margin-top: 6px;
  font-size: 12px;
  padding: 4px 6px;
  border-radius: 6px;
  display: inline-block;
}

.success {
  background: #d1fae5;
  color: #065f46;
}

.error {
  background: #fee2e2;
  color: #991b1b;
}

.weekly {
  background: #bbf7d0;
  color: #065f46;
}

/* モーダル */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.3);
}

.modal {
  background: white;
  width: 500px;
  margin: 100px auto;
  padding: 20px;
  border-radius: 12px;
}

.close {
  float: right;
  border: none;
  background: none;
  font-size: 18px;
}

.content {
  margin-top: 10px;
  border: 1px solid #ddd;
  padding: 10px;
  max-height: 250px;
  overflow: auto;
}

.cell.future {
  background: #f3f4f6;
  color: #9ca3af;
  pointer-events: none;
}

.badge.success {
  background: #d1fae5;
}

.badge.error {
  background: #fee2e2;
}

.cell.sunday {
  background: #fee2e2;
  color: #b91c1c;
}

.cell.empty {
  background: transparent;
  border: none;
  cursor: default;
}
</style>