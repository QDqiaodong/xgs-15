<template>
  <div class="floor-plan-container">
    <div class="page-header">
      <h2 class="page-title">🏢 楼栋设施平面台账</h2>
      <p class="page-desc">按楼栋和楼层展示设施分布，点击设施查看详细信息</p>
    </div>

    <el-card class="main-card">
      <div class="building-tabs">
        <el-tabs v-model="activeBuilding" @tab-change="handleBuildingChange">
          <el-tab-pane v-for="building in buildings" :key="building" :label="building" :name="building" />
        </el-tabs>
      </div>

      <div class="floor-layout">
        <div class="floor-nav">
          <div class="floor-nav-title">楼层</div>
          <div class="floor-list">
            <div
              v-for="floor in sortedFloors"
              :key="floor"
              class="floor-item"
              :class="{ active: activeFloor === floor }"
              @click="activeFloor = floor"
            >
              <span class="floor-number">{{ floor }}F</span>
              <span class="floor-count">{{ getFloorFacilityCount(floor) }}项</span>
            </div>
          </div>
        </div>

        <div class="floor-plan-area">
          <div class="floor-plan-header">
            <div class="floor-title">{{ activeBuilding }} - {{ activeFloor }}楼</div>
            <div class="facility-legend">
              <span v-for="(item, key) in facilityLegend" :key="key" class="legend-item">
                <span class="legend-icon">{{ item.icon }}</span>
                <span class="legend-text">{{ key }}</span>
                <span class="legend-count">({{ getTypeCount(key) }})</span>
              </span>
            </div>
          </div>

          <div class="floor-plan-content">
            <div class="plan-room" v-for="room in roomLayout" :key="room.name" :style="room.style">
              <div class="room-label">{{ room.name }}</div>
              <div class="room-facilities">
                <div
                  v-for="facility in getRoomFacilities(room.name)"
                  :key="facility.id"
                  class="facility-marker"
                  :class="getStatusClass(facility.status)"
                  @click="showFacilityDetail(facility)"
                >
                  <span class="marker-icon">{{ getTypeIcon(facility.facilityType) }}</span>
                  <el-tooltip :content="facility.facilityName" placement="top">
                    <span class="marker-name">{{ facility.facilityCode }}</span>
                  </el-tooltip>
                </div>
              </div>
            </div>

            <div class="corridor-h" v-if="hasCorridor('h')">走廊</div>
            <div class="corridor-v" v-if="hasCorridor('v')">走廊</div>
          </div>

          <div class="floor-stats">
            <el-row :gutter="12">
              <el-col :span="6">
                <div class="stat-item normal">
                  <div class="stat-num">{{ getStatusCount('正常') }}</div>
                  <div class="stat-label">正常运行</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-item warning">
                  <div class="stat-num">{{ getStatusCount('维护中') }}</div>
                  <div class="stat-label">维护中</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-item danger">
                  <div class="stat-num">{{ getStatusCount('故障') }}</div>
                  <div class="stat-label">故障</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-item info">
                  <div class="stat-num">{{ getStatusCount('停用') }}</div>
                  <div class="stat-label">停用</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="设施详情" width="700px" class="facility-detail-dialog">
      <div v-if="currentFacility" class="detail-content">
        <div class="detail-header">
          <div class="detail-icon">{{ getTypeIcon(currentFacility.facilityType) }}</div>
          <div class="detail-title">
            <h3>{{ currentFacility.facilityName }}</h3>
            <p>编号：{{ currentFacility.facilityCode }}</p>
          </div>
          <el-tag :type="getStatusType(currentFacility.status)" size="large">{{ currentFacility.status }}</el-tag>
        </div>

        <el-tabs v-model="activeDetailTab">
          <el-tab-pane label="基本信息" name="basic">
            <el-descriptions :column="2" border size="default">
              <el-descriptions-item label="设施类型">{{ currentFacility.facilityType }}</el-descriptions-item>
              <el-descriptions-item label="所在楼栋">{{ currentFacility.building }}</el-descriptions-item>
              <el-descriptions-item label="所在楼层">{{ currentFacility.floor }}楼</el-descriptions-item>
              <el-descriptions-item label="具体位置">{{ currentFacility.location }}</el-descriptions-item>
              <el-descriptions-item label="生产厂家">{{ currentFacility.manufacturer || '-' }}</el-descriptions-item>
              <el-descriptions-item label="设备型号">{{ currentFacility.model || '-' }}</el-descriptions-item>
              <el-descriptions-item label="安装日期">{{ formatDate(currentFacility.installationDate) }}</el-descriptions-item>
              <el-descriptions-item label="年检周期">{{ currentFacility.annualInspectionCycle }}个月</el-descriptions-item>
            </el-descriptions>
            <div v-if="currentFacility.remarks" class="remarks-section">
              <div class="remarks-label">备注：</div>
              <div class="remarks-content">{{ currentFacility.remarks }}</div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="巡检摘要" name="inspection">
            <div class="summary-stats">
              <el-row :gutter="12">
                <el-col :span="8">
                  <div class="summary-card total">
                    <div class="summary-num">{{ inspectionRecords.length }}</div>
                    <div class="summary-label">总巡检次数</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-card normal">
                    <div class="summary-num">{{ getInspectionNormalCount() }}</div>
                    <div class="summary-label">正常</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-card abnormal">
                    <div class="summary-num">{{ getInspectionAbnormalCount() }}</div>
                    <div class="summary-label">异常</div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="latest-inspection">
              <h4>最近一次巡检</h4>
              <div v-if="latestInspection" class="latest-card">
                <el-descriptions :column="2" size="small">
                  <el-descriptions-item label="巡检日期">{{ formatDate(latestInspection.inspectionDate) }}</el-descriptions-item>
                  <el-descriptions-item label="巡检人员">{{ latestInspection.inspector || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="外观状态">{{ latestInspection.appearanceStatus || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="运行状态">{{ latestInspection.operationStatus || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="安全部件">{{ latestInspection.safetyParts || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="是否异常">
                    <el-tag :type="latestInspection.hasAbnormal ? 'danger' : 'success'" size="small">
                      {{ latestInspection.hasAbnormal ? '有异常' : '正常' }}
                    </el-tag>
                  </el-descriptions-item>
                </el-descriptions>
                <div v-if="latestInspection.abnormalDescription" class="abnormal-desc">
                  <span class="abnormal-label">异常描述：</span>
                  <span>{{ latestInspection.abnormalDescription }}</span>
                </div>
              </div>
              <el-empty v-else description="暂无巡检记录" :image-size="80" />
            </div>
          </el-tab-pane>

          <el-tab-pane label="年检摘要" name="annual">
            <div class="summary-stats">
              <el-row :gutter="12">
                <el-col :span="8">
                  <div class="summary-card total">
                    <div class="summary-num">{{ annualRecords.length }}</div>
                    <div class="summary-label">总年检次数</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-card qualified">
                    <div class="summary-num">{{ getAnnualQualifiedCount() }}</div>
                    <div class="summary-label">合格</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="summary-card unqualified">
                    <div class="summary-num">{{ getAnnualUnqualifiedCount() }}</div>
                    <div class="summary-label">不合格</div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div class="latest-annual">
              <h4>最近一次年检</h4>
              <div v-if="latestAnnual" class="latest-card">
                <el-descriptions :column="2" size="small">
                  <el-descriptions-item label="年检日期">{{ formatDate(latestAnnual.inspectionDate) }}</el-descriptions-item>
                  <el-descriptions-item label="检测机构">{{ latestAnnual.inspectionAgency }}</el-descriptions-item>
                  <el-descriptions-item label="检测人员">{{ latestAnnual.inspector || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="报告编号">{{ latestAnnual.reportNumber || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="合格状态">
                    <el-tag :type="getAnnualStatusType(latestAnnual.qualificationStatus)" size="small">
                      {{ latestAnnual.qualificationStatus }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="有效期至">{{ formatDate(latestAnnual.expiryDate) }}</el-descriptions-item>
                </el-descriptions>
                <div class="rectification-info">
                  <span class="rectification-label">整改状态：</span>
                  <el-tag :type="getRectificationType(latestAnnual.rectificationStatus)" size="small">
                    {{ latestAnnual.rectificationStatus }}
                  </el-tag>
                </div>
                <div v-if="latestAnnual.defectsFound" class="defects-desc">
                  <span class="defects-label">发现缺陷：</span>
                  <span>{{ latestAnnual.defectsFound }}</span>
                </div>
              </div>
              <el-empty v-else description="暂未年检记录" :image-size="80" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const activeBuilding = ref('')
const activeFloor = ref(1)
const activeDetailTab = ref('basic')
const facilities = ref([])
const detailDialogVisible = ref(false)
const currentFacility = ref(null)
const inspectionRecords = ref([])
const annualRecords = ref([])

const facilityLegend = {
  '电梯': { icon: '🛗', color: '#1890ff' },
  '消防柜': { icon: '🔥', color: '#f5222d' },
  '水泵': { icon: '💧', color: '#52c41a' },
  '监控摄像头': { icon: '📹', color: '#722ed1' },
  '照明设施': { icon: '💡', color: '#faad14' },
  '压力容器': { icon: '⚗️', color: '#eb2f96' }
}

const roomLayout = computed(() => {
  return [
    { name: '东侧区域', style: { gridArea: 'east' } },
    { name: '西侧区域', style: { gridArea: 'west' } },
    { name: '北侧区域', style: { gridArea: 'north' } },
    { name: '南侧区域', style: { gridArea: 'south' } },
    { name: '设备间', style: { gridArea: 'equipment' } },
    { name: '办公区', style: { gridArea: 'office' } }
  ]
})

const buildings = computed(() => {
  const buildingSet = new Set(facilities.value.map(f => f.building))
  return Array.from(buildingSet).sort()
})

const currentBuildingFacilities = computed(() => {
  return facilities.value.filter(f => f.building === activeBuilding.value)
})

const floors = computed(() => {
  const floorSet = new Set(currentBuildingFacilities.value.map(f => f.floor).filter(f => f != null))
  return Array.from(floorSet)
})

const sortedFloors = computed(() => {
  return [...floors.value].sort((a, b) => b - a)
})

const currentFloorFacilities = computed(() => {
  return currentBuildingFacilities.value.filter(f => f.floor === activeFloor.value)
})

const latestInspection = computed(() => {
  if (inspectionRecords.value.length === 0) return null
  return [...inspectionRecords.value].sort((a, b) => new Date(b.inspectionDate) - new Date(a.inspectionDate))[0]
})

const latestAnnual = computed(() => {
  if (annualRecords.value.length === 0) return null
  return [...annualRecords.value].sort((a, b) => new Date(b.inspectionDate) - new Date(a.inspectionDate))[0]
})

const getFloorFacilityCount = (floor) => {
  return currentBuildingFacilities.value.filter(f => f.floor === floor).length
}

const getTypeCount = (type) => {
  return currentFloorFacilities.value.filter(f => f.facilityType === type).length
}

const getStatusCount = (status) => {
  return currentFloorFacilities.value.filter(f => f.status === status).length
}

const getTypeIcon = (type) => {
  return facilityLegend[type]?.icon || '🏭'
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

const getStatusClass = (status) => {
  const classes = {
    '正常': 'status-normal',
    '维护中': 'status-warning',
    '故障': 'status-danger',
    '停用': 'status-info'
  }
  return classes[status] || 'status-info'
}

const getAnnualStatusType = (status) => {
  const types = {
    '合格': 'success',
    '不合格': 'danger',
    '待检测': 'warning',
    '待整改': 'warning'
  }
  return types[status] || 'info'
}

const getRectificationType = (status) => {
  const types = {
    '无缺陷': 'success',
    '已整改': 'success',
    '整改中': 'warning',
    '未整改': 'danger'
  }
  return types[status] || 'info'
}

const formatDate = (date) => {
  return date ? new Date(date).toLocaleDateString('zh-CN') : '-'
}

const getFacilityRoom = (facility) => {
  const location = facility.location || ''
  if (location.includes('设备间') || location.includes('泵房') || location.includes('机房')) return '设备间'
  if (location.includes('办公区')) return '办公区'
  if (location.includes('东') || location.includes('大厅') || location.includes('货梯厅')) return '东侧区域'
  if (location.includes('西')) return '西侧区域'
  if (location.includes('北')) return '北侧区域'
  if (location.includes('南') || location.includes('入口') || location.includes('生产区') || location.includes('车间') || location.includes('走廊')) return '南侧区域'
  return null
}

const getRoomFacilities = (roomName) => {
  return currentFloorFacilities.value.filter(f => getFacilityRoom(f) === roomName)
}

const hasCorridor = (type) => {
  return currentFloorFacilities.value.length > 0
}

const getInspectionNormalCount = () => {
  return inspectionRecords.value.filter(r => !r.hasAbnormal).length
}

const getInspectionAbnormalCount = () => {
  return inspectionRecords.value.filter(r => r.hasAbnormal).length
}

const getAnnualQualifiedCount = () => {
  return annualRecords.value.filter(r => r.qualificationStatus === '合格').length
}

const getAnnualUnqualifiedCount = () => {
  return annualRecords.value.filter(r => r.qualificationStatus === '不合格').length
}

const handleBuildingChange = () => {
  if (sortedFloors.value.length > 0) {
    activeFloor.value = sortedFloors.value[0]
  }
}

const showFacilityDetail = async (facility) => {
  currentFacility.value = facility
  detailDialogVisible.value = true
  activeDetailTab.value = 'basic'
  
  try {
    const [inspectionRes, annualRes] = await Promise.all([
      axios.get(`/api/inspections/facility/${facility.id}`),
      axios.get(`/api/annual-inspection/facility/${facility.id}`)
    ])
    inspectionRecords.value = inspectionRes.data
    annualRecords.value = annualRes.data
  } catch (error) {
    console.error('获取设施详情失败:', error)
  }
}

const fetchFacilities = async () => {
  try {
    const res = await axios.get('/api/facilities')
    facilities.value = res.data
    if (buildings.value.length > 0) {
      activeBuilding.value = buildings.value[0]
    }
  } catch (error) {
    console.error('获取设施列表失败:', error)
  }
}

onMounted(() => {
  fetchFacilities()
})
</script>

<style scoped>
.floor-plan-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #333;
}

.page-desc {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.main-card {
  padding: 0;
}

.building-tabs {
  padding: 0 20px;
  border-bottom: 1px solid #ebeef5;
}

.floor-layout {
  display: flex;
  min-height: 600px;
}

.floor-nav {
  width: 100px;
  background: #fafafa;
  border-right: 1px solid #ebeef5;
  padding: 16px 0;
}

.floor-nav-title {
  text-align: center;
  font-size: 14px;
  color: #999;
  margin-bottom: 12px;
  padding: 0 8px;
}

.floor-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 0 8px;
}

.floor-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 8px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.floor-item:hover {
  border-color: #409eff;
  background: #ecf5ff;
}

.floor-item.active {
  background: #409eff;
  border-color: #409eff;
  color: white;
}

.floor-number {
  font-size: 16px;
  font-weight: bold;
}

.floor-count {
  font-size: 12px;
  opacity: 0.8;
  margin-top: 2px;
}

.floor-plan-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.floor-plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.floor-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.facility-legend {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #666;
}

.legend-icon {
  font-size: 16px;
}

.legend-count {
  color: #999;
  font-size: 12px;
}

.floor-plan-content {
  flex: 1;
  display: grid;
  grid-template-areas:
    "north north north"
    "west equipment east"
    "west office east"
    "south south south";
  grid-template-columns: 1fr 1.5fr 1fr;
  grid-template-rows: auto 1fr 1fr auto;
  gap: 12px;
  min-height: 400px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-bottom: 16px;
}

.plan-room {
  background: white;
  border: 2px solid #dcdfe6;
  border-radius: 8px;
  padding: 12px;
  min-height: 100px;
  display: flex;
  flex-direction: column;
}

.room-label {
  font-size: 13px;
  font-weight: bold;
  color: #666;
  margin-bottom: 8px;
  padding-bottom: 6px;
  border-bottom: 1px dashed #e4e7ed;
}

.room-facilities {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-content: flex-start;
}

.facility-marker {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 10px;
  background: #f0f9ff;
  border: 2px solid #91d5ff;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  min-width: 70px;
}

.facility-marker:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.facility-marker.status-normal {
  background: #f0f9ff;
  border-color: #91d5ff;
}

.facility-marker.status-warning {
  background: #fdf6ec;
  border-color: #f5dab1;
}

.facility-marker.status-danger {
  background: #fef0f0;
  border-color: #fbc4c4;
}

.facility-marker.status-info {
  background: #f4f4f5;
  border-color: #d3d3d6;
}

.marker-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.marker-name {
  font-size: 11px;
  color: #666;
  white-space: nowrap;
}

.corridor-h,
.corridor-v {
  display: flex;
  align-items: center;
  justify-content: center;
  background: #e8eaed;
  color: #909399;
  font-size: 12px;
  border-radius: 4px;
}

.floor-stats {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background: white;
  border-radius: 8px;
}

.stat-item.normal .stat-num { color: #67c23a; }
.stat-item.warning .stat-num { color: #e6a23c; }
.stat-item.danger .stat-num { color: #f56c6c; }
.stat-item.info .stat-num { color: #909399; }

.stat-num {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #999;
}

.detail-content {
  padding: 10px 0;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 16px;
}

.detail-icon {
  font-size: 48px;
  width: 70px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f9ff;
  border-radius: 12px;
}

.detail-title {
  flex: 1;
}

.detail-title h3 {
  margin: 0 0 4px 0;
  font-size: 20px;
  color: #333;
}

.detail-title p {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.summary-stats {
  margin-bottom: 16px;
}

.summary-card {
  text-align: center;
  padding: 16px;
  border-radius: 8px;
}

.summary-card.total {
  background: #f0f9ff;
}
.summary-card.normal {
  background: #f0f9eb;
}
.summary-card.abnormal {
  background: #fef0f0;
}
.summary-card.qualified {
  background: #f0f9eb;
}
.summary-card.unqualified {
  background: #fef0f0;
}

.summary-num {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.summary-label {
  font-size: 13px;
  color: #666;
}

.latest-inspection h4,
.latest-annual h4 {
  margin: 0 0 12px 0;
  font-size: 15px;
  color: #333;
}

.latest-card {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.abnormal-desc,
.defects-desc {
  margin-top: 12px;
  padding: 8px 12px;
  background: #fef0f0;
  border-radius: 4px;
  font-size: 13px;
  color: #f56c6c;
}

.abnormal-label,
.defects-label {
  font-weight: bold;
}

.rectification-info {
  margin-top: 12px;
  font-size: 13px;
}

.rectification-label {
  color: #666;
  margin-right: 8px;
}

.remarks-section {
  margin-top: 16px;
  padding: 12px;
  background: #fafafa;
  border-radius: 6px;
}

.remarks-label {
  font-weight: bold;
  color: #666;
  margin-bottom: 4px;
  font-size: 13px;
}

.remarks-content {
  font-size: 13px;
  color: #333;
  line-height: 1.6;
}
</style>
