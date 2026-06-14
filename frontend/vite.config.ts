import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  // 🔥 backend連結のため、プロキシ(Proxy)設定追加
  server: {
    port: 5174, // frontendポート固定
    proxy: {
      // '/api'が含まれている場合、backendに設定される
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/nippo': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/shuho': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    }
  }
})