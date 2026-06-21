<template>
  <div class="facility-list-container">
    <div class="toolbar">
      <el-input 
        v-model="searchQuery" 
        placeholder="搜索设施编号、名称" 
        class="search-input"
        @keyup.enter="searchFacilities"
      >
        <template #append>
          <el-button @click="searchFacilities"><el-icon><Search /></el-icon></el-button>
        </template>
      </el-input>
      <el-select v-model="filterBuilding" placeholder="选择楼栋" class="filter-select">
        <el-option label="全部" value="" />
        <el-option v-for="building in buildings" :key="building" :label="building" :value="building" />
      </el-select>
      <el-select v-model="filterType" placeholder="选择类型" class="filter-select">
        <el-option label="全部" value="" />
        <el-option v-for="type in facilityTypes" :key="type" :label="type" :value="type" />
      </el-select>
      <el-button type="primary" @click="goToAdd">
        <el-icon><Plus /></el-icon> 新增设施
      </el-button>
    </div>

    <el-card>
      <div class="building-tabs">
        <el-tabs v-model="activeBuilding" @tab-change="handleBuildingChange">
          <el-tab-pane v-for="building in buildingGroups" :key="building" :label="building" :name="building">
            <div class="facility-grid">
              <el-card 
                v-for="facility in getFacilitiesByBuilding(building)" 
                :key="facility.id" 
                class="facility-card"
                :class="{ 'supervised-card': facility.isSupervised }"
                @click="viewFacility(facility)"
              >
                <div class="facility-header">
                  <span class="facility-icon">{{ getTypeIcon(facility.facilityType) }}</span>
                  <div class="facility-info">
                    <div class="facility-code-row">
                      <span class="facility-code">{{ facility.facilityCode }}</span>
                      <el-tag v-if="facility.isSupervised" type="danger" size="small" effect="dark" class="supervised-tag">监管类</el-tag>
                    </div>
                    <div class="facility-name">{{ facility.facilityName }}</div>
                  </div>
                  <el-tag :type="getStatusType(facility.status)">{{ facility.status }}</el-tag>
                </div>
                <div class="facility-body">
                  <div class="facility-detail">
                    <span class="detail-label">类型:</span>
                    <span class="detail-value">{{ facility.facilityType }}</span>
                  </div>
                  <div class="facility-detail">
                    <span class="detail-label">位置:</span>
                    <span class="detail-value">{{ facility.location }}</span>
                  </div>
                  <div class="facility-detail">
                    <span class="detail-label">安装日期:</span>
                    <span class="detail-value">{{ formatDate(facility.installationDate) }}</span>
                  </div>
                  <div class="facility-detail">
                    <span class="detail-label">年检周期:</span>
                    <span class="detail-value">
                      <el-tag type="primary" size="small" effect="plain">
                        {{ facility.annualInspectionCycle || '-' }}个月
                      </el-tag>
                    </span>
                  </div>
                  <div class="facility-detail">
                    <span class="detail-label">下次年检:</span>
                    <span class="detail-value">
                      <el-tag :type="getNextInspectionTagType(facility.nextAnnualInspectionDate)" size="small">
                        {{ formatDate(facility.nextAnnualInspectionDate) || '-' }}
                      </el-tag>
                    </span>
                  </div>
                  <div class="facility-detail">
                    <span class="detail-label">最近巡检:</span>
                    <span class="detail-value">
                      <el-tag :type="getInspectionResultType(facility.latestInspectionResult)" size="small">
                        {{ facility.latestInspectionResult || '-' }}
                      </el-tag>
                    </span>
                  </div>
                  <div class="facility-detail">
                    <span class="detail-label">维修状态:</span>
                    <span class="detail-value">
                      <el-tag :type="getMaintenanceStatusType(facility.latestMaintenanceStatus)" size="small">
                        {{ facility.latestMaintenanceStatus || '-' }}
                      </el-tag>
                    </span>
                  </div>
                </div>
                <div class="facility-actions">
                  <el-button size="small" @click.stop="editFacility(facility.id)">编辑</el-button>
                  <el-button size="small" type="danger" @click.stop="deleteFacility(facility.id)">删除</el-button>
                </div>
              </el-card>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Search, Plus } from '@element-plus/icons-vue'

const router = useRouter()

const searchQuery = ref('')
const filterBuilding = ref('')
const filterType = ref('')
const activeBuilding = ref('全部')
const facilities = ref([])

const facilityTypes = ['电梯', '消防柜', '水泵', '监控摄像头', '照明设施', '压力容器']

const buildings = computed(() => {
  const buildingSet = new Set(facilities.value.map(f => f.building))
  return ['全部', ...Array.from(buildingSet)]
})

const buildingGroups = computed(() => {
  if (filterBuilding.value) {
    return [filterBuilding.value]
  }
  const buildingSet = new Set(facilities.value.map(f => f.building))
  return ['全部', ...Array.from(buildingSet)]
})

const getFacilitiesByBuilding = (building) => {
  let result = facilities.value
  if (building !== '全部') {
    result = result.filter(f => f.building === building)
  }
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(f => 
      f.facilityCode.toLowerCase().includes(query) ||
      f.facilityName.toLowerCase().includes(query)
    )
  }
  if (filterType.value) {
    result = result.filter(f => f.facilityType === filterType.value)
  }
  return result
}

const getTypeIcon = (type) => {
  const icons = {
    '电梯': '🛗',
    '消防柜': '🔥',
    '水泵': '💧',
    '监控摄像头': '📹',
    '照明设施': '💡',
    '压力容器': '⚗️'
  }
  return icons[type] || '🏭'
}

const getStatusType = (status) => {
  const types = {
    '正常': 'success',
    '维护中': 'warning',
    '故障': 'danger',
    '停用': 'info'
  }
  return types[status] || 'info'
}

const getNextInspectionTagType = (date) => {
  if (!date) return 'info'
  const today = new Date()
  const target = new Date(date)
  const diffDays = Math.ceil((target - today) / (1000 * 60 * 60 * 24))
  if (diffDays < 0) return 'danger'
  if (diffDays <= 30) return 'warning'
  return 'success'
}

const getInspectionResultType = (result) => {
  const types = {
    '正常': 'success',
    '待整改': 'warning',
    '异常': 'danger',
    '未巡检': 'info'
  }
  return types[result] || 'info'
}

const getMaintenanceStatusType = (status) => {
  const types = {
    '待维修': 'warning',
    '维修中': 'primary',
    '已完成': 'success',
    '已验收': 'success',
    '无维修记录': 'info'
  }
  return types[status] || 'info'
}

const formatDate = (date) => {
  return date ? new Date(date).toLocaleDateString('zh-CN') : '-'
}

const searchFacilities = () => {
}

const handleBuildingChange = () => {
  nextTick(() => {
    const tabsContainer = document.querySelector('.building-tabs')
    if (tabsContainer) {
      tabsContainer.scrollTop = 0
    }
  })
}

watch(buildingGroups, (newGroups) => {
  if (newGroups.length > 0 && !newGroups.includes(activeBuilding.value)) {
    activeBuilding.value = newGroups[0]
  }
})

const goToAdd = () => {
  router.push('/facilities/add')
}

const viewFacility = (facility) => {
  router.push(`/facilities/${facility.id}/edit`)
}

const editFacility = (id) => {
  router.push(`/facilities/${id}/edit`)
}

const deleteFacility = (id) => {
  elConfirm('确认删除', '确定要删除该设施吗？', () => {
    axios.delete(`/api/facilities/${id}`).then(() => {
      fetchFacilities()
      elSuccess('删除成功')
    }).catch(() => {
      elError('删除失败')
    })
  })
}

const fetchFacilities = async () => {
  try {
    const res = await axios.get('/api/facilities')
    facilities.value = res.data
    if (buildings.value.length > 1) {
      activeBuilding.value = buildings.value[1]
    }
  } catch (error) {
    console.error('获取设施列表失败:', error)
  }
}

const elConfirm = (title, message, callback) => {
  import('element-plus').then(el => {
    el.ElMessageBox.confirm(message, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(callback).catch(() => {})
  })
}

const elSuccess = (message) => {
  import('element-plus').then(el => {
    el.ElMessage.success(message)
  })
}

const elError = (message) => {
  import('element-plus').then(el => {
    el.ElMessage.error(message)
  })
}

onMounted(() => {
  fetchFacilities()
})
</script>

<style scoped>
.facility-list-container {
  padding: 20px;
}

.toolbar {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.filter-select {
  width: 150px;
}

.building-tabs {
  height: calc(100vh - 280px);
  overflow-y: auto;
}

.facility-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 16px;
  padding: 16px;
}

.facility-card {
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #ebeef5;
}

.facility-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.supervised-card {
  border: 2px solid #f56c6c;
  background: linear-gradient(135deg, #fff5f5 0%, #ffffff 100%);
}

.facility-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.facility-icon {
  font-size: 32px;
}

.facility-info {
  flex: 1;
}

.facility-code-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.facility-code {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.supervised-tag {
  margin-left: 0;
}

.facility-name {
  font-size: 14px;
  color: #666;
}

.facility-body {
  padding: 8px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
}

.facility-detail {
  display: flex;
  margin: 6px 0;
  align-items: center;
}

.detail-label {
  width: 70px;
  color: #999;
  font-size: 13px;
  flex-shrink: 0;
}

.detail-value {
  flex: 1;
  font-size: 13px;
  color: #333;
}

.facility-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
}
</style>
