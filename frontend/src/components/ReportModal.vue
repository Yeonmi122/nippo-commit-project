<script setup lang="ts">
import type { ReportDetail } from "../types/report"

const props = defineProps<{
  report: ReportDetail | null
}>()

const emit = defineEmits(["close"])

// 背景クリックで閉じる
const handleOverlayClick = () => {
  emit("close")
}

// モーダル内クリックは伝播させない
const stop = (e: Event) => e.stopPropagation()
</script>

<template>
  <div class="overlay" @click="handleOverlayClick">
    <div class="modal" @click="stop">
      <!-- 閉じるボタン -->
      <button class="close" @click="emit('close')">×</button>

      <h2>日報詳細</h2>

      <div v-if="report">
        <p><strong>日付：</strong>{{ report.date }}</p>
        <p><strong>件名：</strong>{{ report.title }}</p>
        <p><strong>送信日時：</strong>{{ report.createdAt }}</p>
        <p><strong>送信者：</strong>{{ report.sender }}</p>
        <p><strong>宛先：</strong>{{ report.to }}</p>

        <div class="content">
          {{ report.content }}
        </div>
      </div>

      <div v-else>
        読み込み中...
      </div>
    </div>
  </div>
</template>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  width: 600px;
  max-height: 80vh;
  overflow: auto;
  padding: 20px;
  border-radius: 12px;
  position: relative;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  border: none;
  background: transparent;
  font-size: 20px;
  cursor: pointer;
}

.content {
  margin-top: 12px;
  padding: 10px;
  border: 1px solid #eee;
  background: #fafafa;
  border-radius: 8px;
  white-space: pre-wrap;
}
</style>