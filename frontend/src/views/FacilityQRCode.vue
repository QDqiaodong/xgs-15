<template>
  <div class="qrcode-landing">
    <div class="page-header">
      <div class="header-icon">
        <el-icon :size="32"><Document /></el-icon>
      </div>
      <h1>设施信息核验</h1>
      <p class="subtitle">扫码查看设施详情</p>
    </div>

    <div v-if="loading" class="loading-container">
      <el-icon class="loading-icon" :size="40"><Loading /></el-icon>
      <p>正在加载设施信息...</p>
    </div>

    <div v-else-if="error" class="error-container">
      <el-icon class="error-icon" :size="48" color="#f56c6c"><Warning /></el-icon>
      <p class="error-text">{{ errorMessage }}</p>
      <el-button type="primary" @click="retryLoad">重新加载</el-button>
    </div>

    <div v-else-if="facility" class="content">
      <div class="card facility-main-card">
        <div class="card-header">
          <span class="facility-type-icon">{{ getTypeIcon(facility.facilityType) }}</span>
          <div class="card-title">
            <h2>{{ facility.facilityName }}</h2>
            <el-tag :type="getStatusType(facility.status)" size="large" effect="dark">
              {{ facility.status }}
            </el-tag>
          </div>
        </div>
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">设施编号</span>
            <span class="info-value code">{{ facility.facilityCode }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">设施类型</span>
            <span class="info-value">{{ facility.facilityType }}</span>
          </div>
          <div class="info-item full-width">
            <span class="info-label">楼栋位置</span>
            <span class="info-value">
              <el-icon><Location /></el-icon>
              {{ facility.building }}
              <span v-if="facility.floor">{{ facility.floor }}层</span>
              <span v-if="facility.location"> · {{ facility.location }}</span>
            </span>
          </div>
        </div>
      </div>

      <div class="card inspection-card">
        <div class="card-title-row">
          <el-icon :size="20" color="#409EFF"><Notebook /></el-icon>
          <h3>最近巡检</h3>
        </div>
        <div v-if="latestInspection" class="record-content">
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">巡检日期</span>
              <span class="info-value">{{ formatDate(latestInspection.inspectionDate) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">巡检人员</span>
              <span class="info-value">{{ latestInspection.inspector || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">外观状态</span>
              <el-tag :type="latestInspection.appearanceStatus === '正常' ? 'success' : 'warning'" size="small">
                {{ latestInspection.appearanceStatus || '-' }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="info-label">运行状态</span>
              <el-tag :type="latestInspection.operationStatus === '正常' ? 'success' : 'warning'" size="small">
                {{ latestInspection.operationStatus || '-' }}
              </el-tag>
            </div>
          </div>
          <div class="abnormal-section" v-if="latestInspection.hasAbnormal">
            <el-alert type="warning" :closable="false" show-icon>
              <template #title>
                <span>异常说明</span>
              </template>
              <span>{{ latestInspection.abnormalDescription || '存在异常情况' }}</span>
            </el-alert>
          </div>
          <div v-if="latestInspection.remarks" class="remarks-section">
            <span class="remarks-label">备注：</span>
            <span class="remarks-content">{{ latestInspection.remarks }}</span>
          </div>
        </div>
        <div v-else class="no-record">
          <el-icon :size="24" color="#909399"><CircleClose /></el-icon>
          <span>暂无巡检记录</span>
        </div>
      </div>

      <div class="card maintenance-card">
        <div class="card-title-row">
          <el-icon :size="20" color="#e6a23c"><Tools /></el-icon>
          <h3>最近维修</h3>
        </div>
        <div v-if="latestMaintenance" class="record-content">
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">维修日期</span>
              <span class="info-value">{{ formatDate(latestMaintenance.repairDate) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">维修人员</span>
              <span class="info-value">{{ latestMaintenance.repairPerson || '-' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">维修状态</span>
              <el-tag :type="getMaintenanceStatusType(latestMaintenance.status)" size="small">
                {{ latestMaintenance.status }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="info-label">验收状态</span>
              <el-tag :type="getAcceptanceStatusType(latestMaintenance.acceptanceStatus)" size="small">
                {{ latestMaintenance.acceptanceStatus }}
              </el-tag>
            </div>
          </div>
          <div class="fault-section">
            <span class="fault-label">故障描述：</span>
            <span class="fault-content">{{ latestMaintenance.faultDescription }}</span>
          </div>
          <div v-if="latestMaintenance.replacedParts" class="parts-section">
            <span class="parts-label">更换部件：</span>
            <span class="parts-content">{{ latestMaintenance.replacedParts }}</span>
          </div>
        </div>
        <div v-else class="no-record">
          <el-icon :size="24" color="#909399"><CircleClose /></el-icon>
          <span>暂无维修记录</span>
        </div>
      </div>

      <div class="card annual-card">
        <div class="card-title-row">
          <el-icon :size="20" color="#67c23a"><Medal /></el-icon>
          <h3>年检信息</h3>
        </div>
        <div v-if="latestAnnualInspection" class="record-content">
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">检测日期</span>
              <span class="info-value">{{ formatDate(latestAnnualInspection.inspectionDate) }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">检测机构</span>
              <span class="info-value">{{ latestAnnualInspection.inspectionAgency }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">资质状态</span>
              <el-tag :type="getQualificationType(latestAnnualInspection.qualificationStatus)" size="small" effect="dark">
                {{ latestAnnualInspection.qualificationStatus }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="info-label">年检有效期</span>
              <div class="expiry-wrapper">
                <el-tag 
                  :type="getExpiryTagType(latestAnnualInspection.expiryDate)" 
                  size="small"
                  effect="dark"
                >
                  <el-icon><Calendar /></el-icon>
                  {{ formatDate(latestAnnualInspection.expiryDate) }}
                </el-tag>
                <span class="expiry-hint" v-if="isExpiringSoon(latestAnnualInspection.expiryDate)">
                  {{ getExpiryHint(latestAnnualInspection.expiryDate) }}
                </span>
              </div>
            </div>
          </div>
          <div v-if="latestAnnualInspection.reportNumber" class="report-section">
            <span class="report-label">报告编号：</span>
            <span class="report-content">{{ latestAnnualInspection.reportNumber }}</span>
          </div>
        </div>
        <div v-else class="no-record">
          <el-icon :size="24" color="#909399"><CircleClose /></el-icon>
          <span>暂无年检记录</span>
        </div>
      </div>

      <div class="footer-info">
        <p>安装日期：{{ formatDate(facility.installationDate) }}</p>
        <p v-if="facility.manufacturer || facility.model">
          {{ facility.manufacturer || '' }}
          <span v-if="facility.manufacturer && facility.model"> · </span>
          {{ facility.model || '' }}
        </p>
        <p class="update-time">最后更新：{{ formatDate(facility.updatedAt) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import {
  Document,
  Loading,
  Warning,
  Location,
  Notebook,
  CircleClose,
  Tools,
  Medal,
  Calendar
} from '@element-plus/icons-vue'

const route = useRoute()

const loading = ref(true)
const error = ref(false)
const errorMessage = ref('')
const facility = ref(null)
const latestInspection = ref(null)
const latestMaintenance = ref(null)
const latestAnnualInspection = ref(null)

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

const getMaintenanceStatusType = (status) => {
  const types = {
    '待维修': 'danger',
    '维修中': 'warning',
    '已完成': 'success'
  }
  return types[status] || 'info'
}

const getAcceptanceStatusType = (status) => {
  const types = {
    '未验收': 'warning',
    '验收合格': 'success',
    '验收不合格': 'danger'
  }
  return types[status] || 'info'
}

const getQualificationType = (status) => {
  const types = {
    '合格': 'success',
    '不合格': 'danger',
    '待检测': 'warning'
  }
  return types[status] || 'info'
}

const isExpiringSoon = (date) => {
  if (!date) return false
  const expiry = new Date(date)
  const now = new Date()
  const diffDays = Math.ceil((expiry - now) / (1000 * 60 * 60 * 24))
  return diffDays <= 30
}

const getExpiryTagType = (date) => {
  if (!date) return 'info'
  const expiry = new Date(date)
  const now = new Date()
  const diffDays = Math.ceil((expiry - now) / (1000 * 60 * 60 * 24))
  if (diffDays < 0) return 'danger'
  if (diffDays <= 30) return 'warning'
  return 'success'
}

const getExpiryHint = (date) => {
  if (!date) return ''
  const expiry = new Date(date)
  const now = new Date()
  const diffDays = Math.ceil((expiry - now) / (1000 * 60 * 60 * 24))
  if (diffDays < 0) return `已过期${Math.abs(diffDays)}天`
  if (diffDays === 0) return '今日到期'
  return `还有${diffDays}天到期`
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const fetchFacilityById = async (id) => {
  try {
    const res = await axios.get(`/api/facilities/${id}`)
    return res.data
  } catch (e) {
    return null
  }
}

const fetchFacilityByCode = async (code) => {
  try {
    const res = await axios.get(`/api/facilities/code/${code}`)
    return res.data
  } catch (e) {
    return null
  }
}

const fetchLatestInspection = async (facilityId) => {
  try {
    const res = await axios.get(`/api/inspections/facility/${facilityId}`)
    const records = res.data || []
    if (records.length === 0) return null
    records.sort((a, b) => new Date(b.inspectionDate) - new Date(a.inspectionDate))
    return records[0]
  } catch (e) {
    return null
  }
}

const fetchLatestMaintenance = async (facilityId) => {
  try {
    const res = await axios.get(`/api/maintenance/facility/${facilityId}`)
    const records = res.data || []
    if (records.length === 0) return null
    records.sort((a, b) => {
      const dateA = a.repairDate ? new Date(a.repairDate) : new Date(a.createdAt)
      const dateB = b.repairDate ? new Date(b.repairDate) : new Date(b.createdAt)
      return dateB - dateA
    })
    return records[0]
  } catch (e) {
    return null
  }
}

const fetchLatestAnnualInspection = async (facilityId) => {
  try {
    const res = await axios.get(`/api/annual-inspection/facility/${facilityId}`)
    const records = res.data || []
    if (records.length === 0) return null
    records.sort((a, b) => new Date(b.inspectionDate) - new Date(a.inspectionDate))
    return records[0]
  } catch (e) {
    return null
  }
}

const loadData = async () => {
  loading.value = true
  error.value = false
  errorMessage.value = ''

  const id = route.params.id || route.query.id
  const code = route.query.code

  if (!id && !code) {
    error.value = true
    errorMessage.value = '缺少设施标识参数'
    loading.value = false
    return
  }

  try {
    let facilityData = null
    if (id) {
      facilityData = await fetchFacilityById(id)
    }
    if (!facilityData && code) {
      facilityData = await fetchFacilityByCode(code)
    }

    if (!facilityData) {
      error.value = true
      errorMessage.value = '未找到对应设施信息'
      loading.value = false
      return
    }

    facility.value = facilityData

    const [inspection, maintenance, annual] = await Promise.all([
      fetchLatestInspection(facilityData.id),
      fetchLatestMaintenance(facilityData.id),
      fetchLatestAnnualInspection(facilityData.id)
    ])

    latestInspection.value = inspection
    latestMaintenance.value = maintenance
    latestAnnualInspection.value = annual
  } catch (e) {
    error.value = true
    errorMessage.value = '加载设施信息失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const retryLoad = () => {
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.qrcode-landing {
  min-height: 100vh;
  background: linear-gradient(180deg, #f0f4ff 0%, #f5f7fa 300px);
  padding-bottom: 30px;
}

.page-header {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
  color: white;
  padding: 40px 20px 50px;
  text-align: center;
  position: relative;
}

.page-header::after {
  content: '';
  position: absolute;
  bottom: -20px;
  left: 0;
  right: 0;
  height: 40px;
  background: linear-gradient(180deg, #f0f4ff 0%, transparent 100%);
  border-radius: 50% 50% 0 0 / 100% 100% 0 0;
}

.header-icon {
  width: 64px;
  height: 64px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  backdrop-filter: blur(10px);
}

.page-header h1 {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 600;
}

.subtitle {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.content {
  padding: 10px 16px 0;
  max-width: 640px;
  margin: 0 auto;
}

.card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}

.facility-main-card {
  margin-top: -10px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.facility-type-icon {
  font-size: 40px;
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.card-title {
  flex: 1;
}

.card-title h2 {
  margin: 0 0 8px;
  font-size: 20px;
  color: #1f2937;
}

.card-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.card-title-row h3 {
  margin: 0;
  font-size: 16px;
  color: #1f2937;
  font-weight: 600;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.info-item.full-width {
  grid-column: 1 / -1;
}

.info-label {
  font-size: 12px;
  color: #9ca3af;
}

.info-value {
  font-size: 14px;
  color: #1f2937;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
  word-break: break-all;
}

.info-value.code {
  font-family: 'SF Mono', Monaco, monospace;
  font-size: 15px;
  color: #409EFF;
  background: #eff6ff;
  padding: 4px 10px;
  border-radius: 6px;
  display: inline-flex;
  width: fit-content;
}

.record-content {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.abnormal-section,
.remarks-section,
.fault-section,
.parts-section,
.report-section {
  padding-top: 12px;
  border-top: 1px dashed #e5e7eb;
}

.remarks-label,
.fault-label,
.parts-label,
.report-label {
  font-size: 12px;
  color: #9ca3af;
  margin-right: 6px;
}

.remarks-content,
.fault-content,
.parts-content,
.report-content {
  font-size: 13px;
  color: #4b5563;
  line-height: 1.6;
}

.expiry-wrapper {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: flex-start;
}

.expiry-hint {
  font-size: 12px;
  color: #f56c6c;
  font-weight: 500;
}

.no-record {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 24px 0;
  color: #9ca3af;
  font-size: 14px;
}

.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  gap: 16px;
  color: #6b7280;
}

.loading-icon {
  animation: spin 1s linear infinite;
  color: #409EFF;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.error-icon {
  margin-bottom: 4px;
}

.error-text {
  color: #f56c6c;
  font-size: 15px;
  margin: 0;
}

.footer-info {
  text-align: center;
  padding: 20px 20px 10px;
  color: #9ca3af;
  font-size: 12px;
}

.footer-info p {
  margin: 6px 0;
}

.footer-info .update-time {
  font-size: 11px;
  color: #d1d5db;
}

@media (max-width: 480px) {
  .info-grid {
    grid-template-columns: 1fr;
  }

  .page-header h1 {
    font-size: 20px;
  }

  .card {
    padding: 16px;
  }

  .card-header {
    gap: 12px;
  }

  .facility-type-icon {
    width: 52px;
    height: 52px;
    font-size: 32px;
  }
}
</style>
