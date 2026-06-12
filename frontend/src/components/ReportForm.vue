<script setup lang="ts">
import { ref, reactive, computed } from "vue"
import { useRouter } from "vue-router"

const mode = ref<"daily" | "weekly">("daily")

const today = new Date()

const formattedDate = `${today.getFullYear()}-${
  String(today.getMonth() + 1).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`

// 日報
const daily = reactive({
  date: formattedDate,
  start: "09:00",
  end: "18:00",
  working: "08:00",
  subject: "",
  to: "",
  cc: "",
  record: "",
  good: "",
  improve: "",
  other: ""
})


// 週報
const weekly = reactive({
  date: formattedDate,
  subject: "",
  to: "",
  cc: "",
  record: "",
  goal: "",
  content: "",
  days: ""
})

// 稼働時間形式チェック
const isValidTime = (t: string) => /^\d{2}:\d{2}$/.test(t)

// カレンダー画面に戻る
const router = useRouter()

const goCalendar = () => {
  router.push("/")
}
</script>

<template>
  <div class="page">
    <h1>{{ mode === "daily" ? "日報作成" : "週報作成" }}</h1>
    <div class="header">
      <button class="back-btn" @click="goCalendar">← カレンダーに戻る  </button>
    </div>
    <!-- トグル -->
    <div class="toggle-wrapper">
      <div class="toggle">
        <div class="slider" :class="mode"></div>
        <button @click="mode = 'daily'">日報</button>
        <button @click="mode = 'weekly'">週報</button>
      </div>
    </div>

    <!-- カード -->
    <div class="card">
      <!-- 日報 -->
      <div v-if="mode === 'daily'" class="form">
        <p v-if="!isValidTime(daily.start)">形式が違います</p>
        <div class="row">
          <div class="field">
            <label>対象日付</label>
            <input type="date" v-model="daily.date" />
          </div>

          <div class="field time">
            <label>稼働時間</label>
            <div class="time-row">
              <input v-model="daily.start" />
              <input v-model="daily.end" />
              <input v-model="daily.working" />
            </div>
          </div>
        </div>

        <div class="row">
          <input placeholder="メール件名" v-model="daily.subject" />
          <input placeholder="送信先メール" v-model="daily.to" />
          <input placeholder="CC" v-model="daily.cc" />
        </div>

        <div class="field">
          <label>実施記録</label>
          <textarea v-model="daily.record"></textarea>
        </div>
        <div class="field">
          <label>うまくいったこと</label>
          <textarea v-model="daily.good"></textarea>
        </div>
        <div class="field">
          <label>改善したいこと</label>
          <textarea v-model="daily.improve"></textarea>
        </div>
        <div class="field">
          <label>その他</label>
          <textarea v-model="daily.other"></textarea>
        </div>
      </div>

      <!-- 週報 -->
      <div v-else class="form">
        <div class="row">
          <div class="field">
            <label>対象日付</label>
            <input type="date" v-model="weekly.date" />
          </div>
        </div>

        <div class="row">
          <input placeholder="メール件名" v-model="weekly.subject" />
          <input placeholder="送信先メール" v-model="weekly.to" />
          <input placeholder="CC" v-model="weekly.cc" />
        </div>

        <!-- 週テーブル -->
        <div class="week-table">
          <div class="field">
            <label>稼働時間</label>
            <textarea v-model="weekly.record"></textarea>
          </div>
          <div class="field">
            <label>実施記録</label>
            <textarea v-model="weekly.record"></textarea>
          </div>
          <div class="field">
            <label>目標</label>
            <textarea v-model="weekly.goal"></textarea>
          </div>
          <div class="field">
            <label>内容記載</label>
            <textarea v-model="weekly.content"></textarea>
          </div>
        </div>
      </div>

      <!-- ボタン -->
      <div class="actions">
        <button class="submit">提出 & Gmail送信</button>
      </div>
    </div>
  </div>
</template>
