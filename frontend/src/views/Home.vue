<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon facility-icon">🏢</div>
          <div class="stat-info">
            <div class="stat-value">{{ facilityCount }}</div>
            <div class="stat-label">设施总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon inspection-icon">✅</div>
          <div class="stat-info">
            <div class="stat-value">{{ inspectionCount }}</div>
            <div class="stat-label">本月巡检</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon maintenance-icon">🔧</div>
          <div class="stat-info">
            <div class="stat-value">{{ maintenanceCount }}</div>
            <div class="stat-label">待维修</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon annual-icon">📅</div>
          <div class="stat-info">
            <div class="stat-value">{{ annualCount }}</div>
            <div class="stat-label">即将到期年检</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card title="设施类型分布">
          <div class="chart-container">
            <el-row v-for="item in facilityTypeStats" :key="item.type" :gutter="10">
              <el-col :span="6">{{ item.type }}</el-col>
              <el-col :span="18">
                <el-progress :percentage="item.percentage" :color="item.color" />
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="最近巡检记录">
          <el-table :data="recentInspections" border :columns="inspectionColumns">
            <el-table-column prop="facilityCode" label="设施编号" />
            <el-table-column prop="facilityName" label="设施名称" />
            <el-table-column prop="inspectionDate" label="巡检日期" />
            <el-table-column prop="hasAbnormal" label="异常">
              <template #default="scope">
                <el-tag :type="scope.row.hasAbnormal ? 'danger' : 'success'">
                  {{ scope.row.hasAbnormal ? '有异常' : '正常' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const facilityCount = ref(0)
const inspectionCount = ref(0)
const maintenanceCount = ref(0)
const annualCount = ref(0)

const facilityTypeStats = ref([
  { type: '电梯', count: 0, percentage: 0, color: '#1890ff' },
  { type: '消防柜', count: 0, percentage: 0, color: '#f5222d' },
  { type: '水泵', count: 0, percentage: 0, color: '#52c41a' },
  { type: '监控', count: 0, percentage: 0, color: '#722ed1' },
  { type: '照明', count: 0, percentage: 0, color: '#faad14' }
])

const recentInspections = ref([])

const inspectionColumns = [
  { prop: 'facilityCode', label: '设施编号' },
  { prop: 'facilityName', label: '设施名称' },
  { prop: 'inspectionDate', label: '巡检日期' },
  { prop: 'hasAbnormal', label: '异常' }
]

const fetchStats = async () => {
  try {
    const [facilityRes, inspectionRes, maintenanceRes, annualRes] = await Promise.all([
      axios.get('/api/facilities'),
      axios.get('/api/inspections'),
      axios.get('/api/maintenance/status/待维修'),
      axios.get('/api/annual-inspection/expired')
    ])
    
    facilityCount.value = facilityRes.data.length
    
    const now = new Date()
    const currentMonth = now.getMonth()
    inspectionCount.value = inspectionRes.data.filter(item => {
      const date = new Date(item.inspectionDate)
      return date.getMonth() === currentMonth
    }).length
    
    maintenanceCount.value = maintenanceRes.data.length
    annualCount.value = annualRes.data.length

    const typeCount = {}
    facilityRes.data.forEach(item => {
      typeCount[item.facilityType] = (typeCount[item.facilityType] || 0) + 1
    })
    
    facilityTypeStats.value = facilityTypeStats.value.map(item => {
      const count = typeCount[item.type] || 0
      return {
        ...item,
        count,
        percentage: facilityCount.value > 0 ? Math.round((count / facilityCount.value) * 100) : 0
      }
    })

    recentInspections.value = inspectionRes.data.slice(0, 5)
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 48px;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
}

.facility-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.inspection-icon {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.maintenance-icon {
  background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);
}

.annual-icon {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.chart-container {
  padding: 10px;
}
</style>