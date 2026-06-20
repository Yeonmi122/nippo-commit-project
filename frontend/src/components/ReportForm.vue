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

// ★ エラー表示をコントロールするフラグ
const showErrors = ref(false)

// ★ 日報がすべて入力されているかチェック（'other' は除外）
const isDailyValid = computed(() => {
  const { other, ...requiredFields } = daily
  return Object.values(requiredFields).every(val => val.trim() !== "")
})

// ★ 週報がすべて入力されているかチェック
const isWeeklyValid = computed(() => {
  return Object.values(weekly).every(val => val.trim() !== "")
})

// 現在のモードのバリデーション状態
const isValid = computed(() => mode.value === "daily" ? isDailyValid.value : isWeeklyValid.value)

// 稼働時間形式チェック
const isValidTime = (t: string) => /^\d{2}:\d{2}$/.test(t)

// カレンダー画面に戻る
const router = useRouter()

const goCalendar = () => {
  router.push("/")
}
// メール送信
const submitReport = async () => {
  // ★ 提出ボタンが押されたらエラーチェックを有効にする
  showErrors.value = true

  // ★ 未入力項目がある場合は処理を中断し、画面上部へスクロール
  if (!isValid.value) {
    window.scrollTo({ top: 0, behavior: "smooth" })
    return
  }

  try {
    let payload
    
    if (mode.value === "daily") {
      // 日報の場合
      const { start, end, working, record, good, improve, other, ...baseDaily } = daily
      
      payload = {
        type: "daily",
        ...baseDaily,       // date, subject, to, cc
        content: {
          start: start,
          end: end,
          working: working,
          record: record,   // 実施記録
          good: good,       // うまくいったこと
          improve: improve, // 改善したいこと
          other: other      // その他
        }
      }
    } else {
      // 週報の場合：指定の項目を content の中にまとめる
      const { record, goal, content, days, ...baseWeekly } = weekly
      
      payload = {
        type: "weekly",
        ...baseWeekly,      // date, subject, to, cc
        content: {          // "content" でまとめます
          record: record,   // 実施記録
          goal: goal,       // 目標
          content: content, // 内容記載
          days: days        // 稼働時間
        }
      }
    }

    const response = await fetch("/send", {
      method: "POST",
      headers: {

        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    })
    if (!response.ok) {
      throw new Error("メール送信に失敗しました")
    }
    alert("メール送信が完了しました")
  } catch (error) {
    console.error(error)
    alert("メール送信中にエラーが発生しました")
  }

}
</script>

<template>
  <div class="page">
    <h1>{{ mode === "daily" ? "日報作成" : "週報作成" }}</h1>
    <div class="header">
      <button class="back-btn" @click="goCalendar">← カレンダーに戻る</button>
    </div>

    <div v-if="showErrors && !isValid" class="error-banner">
      未入力の必須項目があります。赤枠の項目を確認してください。
    </div>
    <!-- トグル -->
    <div class="toggle-wrapper">
      <div class="toggle">
        <div class="slider" :class="mode"></div>
        <button @click="mode = 'daily'; showErrors = false">日報</button>
        <button @click="mode = 'weekly'; showErrors = false">週報</button>
      </div>
    </div>

    <div class="card">
      <!-- 日報 -->
      <div v-if="mode === 'daily'" class="form">
        <p v-if="!isValidTime(daily.start)" class="text-error">形式が違います</p>
        <div class="row">
          <div class="field">
            <label>対象日付</label>
            <input type="date" v-model="daily.date" :class="{ 'has-error': showErrors && !daily.date }" />
          </div>

          <div class="field time">
            <label>稼働時間</label>
            <div class="time-row">
              <input v-model="daily.start" :class="{ 'has-error': showErrors && !daily.start }" />
              <input v-model="daily.end" :class="{ 'has-error': showErrors && !daily.end }" />
              <input v-model="daily.working" :class="{ 'has-error': showErrors && !daily.working }" />
            </div>
          </div>
        </div>

        <div class="row">
          <input placeholder="メール件名" v-model="daily.subject" :class="{ 'has-error': showErrors && !daily.subject }" />
          <input placeholder="送信先メール" v-model="daily.to" :class="{ 'has-error': showErrors && !daily.to }" />
          <input placeholder="CC" v-model="daily.cc" :class="{ 'has-error': showErrors && !daily.cc }" />
        </div>

        <div class="field">
          <label>実施記録</label>
          <textarea v-model="daily.record" :class="{ 'has-error': showErrors && !daily.record }"></textarea>
        </div>
        <div class="field">
          <label>うまくいったこと</label>
          <textarea v-model="daily.good" :class="{ 'has-error': showErrors && !daily.good }"></textarea>
        </div>
        <div class="field">
          <label>改善したいこと</label>
          <textarea v-model="daily.improve" :class="{ 'has-error': showErrors && !daily.improve }"></textarea>
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
            <input type="date" v-model="weekly.date" :class="{ 'has-error': showErrors && !weekly.date }" />
          </div>
        </div>

        <div class="row">
          <input placeholder="メール件名" v-model="weekly.subject" :class="{ 'has-error': showErrors && !weekly.subject }" />
          <input placeholder="送信先メール" v-model="weekly.to" :class="{ 'has-error': showErrors && !weekly.to }" />
          <input placeholder="CC" v-model="weekly.cc" :class="{ 'has-error': showErrors && !weekly.cc }" />
        </div>

        <!-- 週テーブル -->
        <div class="week-table">
          <div class="field">
            <label>稼働時間</label>
            <textarea v-model="weekly.days" :class="{ 'has-error': showErrors && !weekly.days }"></textarea>
          </div>
          <div class="field">
            <label>実施記録</label>
            <textarea v-model="weekly.record" :class="{ 'has-error': showErrors && !weekly.record }"></textarea>
          </div>
          <div class="field">
            <label>目標</label>
            <textarea v-model="weekly.goal" :class="{ 'has-error': showErrors && !weekly.goal }"></textarea>
          </div>
          <div class="field">
            <label>内容記載</label>
            <textarea v-model="weekly.content" :class="{ 'has-error': showErrors && !weekly.content }"></textarea>
          </div>
        </div>
      </div>

      <div class="actions">
        <button class="submit" @click="submitReport">提出 & Gmail送信</button>
      </div>
    </div>
  </div>
</template>

<style scoped>

/* ★ 追加：エラー関連のスタイル */
.error-banner {
  background-color: #fee2e2;
  color: #b91c1c;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 20px;
  text-align: center;
  font-weight: bold;
  border: 1px solid #f87171;
}

.text-error {
  color: #b91c1c;
  font-size: 0.9rem;
  margin-bottom: 4px;
}

/* クラスが付与された時だけ枠線を赤くする */
.has-error {
  border: 2px solid #ef4444 !important;
  background-color: #fef2f2;
}
</style>