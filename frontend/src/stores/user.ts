import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const authToken = ref(localStorage.getItem('authToken') || '')
  const userName = ref(localStorage.getItem('userName') || '')

  const isLoggedIn = computed(() => !!authToken.value)

  function login(username: string, password: string) {
    const users = JSON.parse(localStorage.getItem('registeredUsers') || '[]')
    const user = users.find((u: { username: string; password: string }) => u.username === username && u.password === password)
    if (!user) return false
    const token = 'local_' + Date.now()
    authToken.value = token
    userName.value = username
    localStorage.setItem('authToken', token)
    localStorage.setItem('userName', username)
    return true
  }

  function register(username: string, password: string) {
    const users = JSON.parse(localStorage.getItem('registeredUsers') || '[]')
    if (users.some((u: { username: string }) => u.username === username)) return false
    users.push({ username, password })
    localStorage.setItem('registeredUsers', JSON.stringify(users))
    const token = 'local_' + Date.now()
    authToken.value = token
    userName.value = username
    localStorage.setItem('authToken', token)
    localStorage.setItem('userName', username)
    return true
  }

  function logout() {
    authToken.value = ''
    userName.value = ''
    localStorage.removeItem('authToken')
    localStorage.removeItem('userName')
  }

  return { authToken, userName, isLoggedIn, login, register, logout }
})
