import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
  },
  {
    path: '/spots/:id',
    name: 'SpotDetail',
    component: () => import('@/views/SpotDetailView.vue'),
  },
  {
    path: '/folk/:id',
    name: 'FolkDetail',
    component: () => import('@/views/FolkDetailView.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

export default router
