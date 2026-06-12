import { createRouter, createWebHistory } from "vue-router"
import Dashboard from "../pages/Dashboard.vue"
import Report from "../components/ReportForm.vue"

const routes = [
  { path: "/", name: "dashboard", component: Dashboard },
  { path: "/report", name: "report", component: Report },
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
})