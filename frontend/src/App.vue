<template>
  <div class="app-container">
    <el-container>
      <el-header class="header">
        <div class="logo">
          <span class="logo-icon">🏢</span>
          <span class="logo-text">商业园区物业公共设施维保资产台账系统</span>
        </div>
      </el-header>
      <el-container>
        <el-aside class="aside" width="200px">
          <el-menu :default-active="activeMenu" class="sidebar-menu" mode="vertical">
            <el-menu-item index="/" @click="navigate('/')">
              <template #icon>
                <span>🏠</span>
              </template>
              <span>首页</span>
            </el-menu-item>
            <el-sub-menu index="facility">
              <template #title>
                <span>🏭</span>
                <span>设施管理</span>
              </template>
              <el-menu-item index="/facilities" @click="navigate('/facilities')">
                <span>设施台账</span>
              </el-menu-item>
              <el-menu-item index="/facilities/floor-plan" @click="navigate('/facilities/floor-plan')">
                <span>平面台账</span>
              </el-menu-item>
              <el-menu-item index="/facilities/add" @click="navigate('/facilities/add')">
                <span>新增设施</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="inspection">
              <template #title>
                <span>🔍</span>
                <span>巡检管理</span>
              </template>
              <el-menu-item index="/inspections" @click="navigate('/inspections')">
                <span>巡检记录</span>
              </el-menu-item>
              <el-menu-item index="/inspections/add" @click="navigate('/inspections/add')">
                <span>新增巡检</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="maintenance">
              <template #title>
                <span>🔧</span>
                <span>维修管理</span>
              </template>
              <el-menu-item index="/maintenance" @click="navigate('/maintenance')">
                <span>维修记录</span>
              </el-menu-item>
              <el-menu-item index="/maintenance/add" @click="navigate('/maintenance/add')">
                <span>新增维修</span>
              </el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="annual">
              <template #title>
                <span>📅</span>
                <span>年检管理</span>
              </template>
              <el-menu-item index="/annual-inspection" @click="navigate('/annual-inspection')">
                <span>年检记录</span>
              </el-menu-item>
              <el-menu-item index="/annual-inspection/add" @click="navigate('/annual-inspection/add')">
                <span>新增年检</span>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => {
  return route.path
})

const navigate = (path) => {
  router.push(path)
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.header {
  background-color: #1890ff;
  color: white;
  padding: 0 20px;
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
}

.aside {
  background-color: #001529;
}

.sidebar-menu {
  height: 100%;
  border-right: none;
}

.main {
  padding: 20px;
}
</style>