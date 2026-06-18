<template>
  <div class="inspection-form-container">
    <el-card title="批量巡检录入">
      <div class="batch-section">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="选择楼栋">
              <el-select v-model="selectedBuilding" placeholder="请选择楼栋">
                <el-option v-for="building in buildings" :key="building" :label="building" :value="building" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="巡检日期">
              <el-date-picker v-model="inspectionDate" type="date" placeholder="请选择巡检日期" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="巡检人">
              <el-input v-model="inspector" placeholder="请输入巡检人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="loadFacilities" :disabled="!selectedBuilding">加载设施</el-button>
      </div>

      <el-card title="巡检清单" class="inspection-list-card">
        <el-table :data="inspectionItems" border>
          <el-table-column type="selection" width="55" />
          <el-table-column prop="facilityCode" label="设施编号" />
          <el-table-column prop="facilityName" label="设施名称" />
          <el-table-column prop="facilityType" label="设施类型" />
          <el-table-column prop="building" label="楼栋" />
          <el-table-column prop="location" label="位置" />
          <el-table-column label="外观状态">
            <template #default="scope">
              <el-select v-model="scope.row.appearanceStatus" placeholder="请选择">
                <el-option label="正常" value="正常" />
                <el-option label="轻微损坏" value="轻微损坏" />
                <el-option label="严重损坏" value="严重损坏" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="运行状态">
            <template #default="scope">
              <el-select v-model="scope.row.operationStatus" placeholder="请选择">
                <el-option label="正常" value="正常" />
                <el-option label="异常" value="异常" />
                <el-option label="停机" value="停机" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="安全部件">
            <template #default="scope">
              <el-select v-model="scope.row.safetyParts" placeholder="请选择">
                <el-option label="完好" value="完好" />
                <el-option label="需更换" value="需更换" />
                <el-option label="已过期" value="已过期" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="异常描述">
            <template #default="scope">
              <el-input v-model="scope.row.abnormalDescription" placeholder="异常描述" />
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <div class="button-section">
        <el-button type="primary" @click="batchSubmit">批量提交</el-button>
        <el-button @click="cancelForm">取消</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const selectedBuilding = ref('')
const inspectionDate = ref('')
const inspector = ref('')
const buildings = ref([])
const inspectionItems = ref([])

const loadFacilities = async () => {
  try {
    const res = await axios.get(`/api/facilities/building/${selectedBuilding.value}`)
    inspectionItems.value = res.data.map(facility => ({
      facilityId: facility.id,
      facilityCode: facility.facilityCode,
      facilityName: facility.facilityName,
      facilityType: facility.facilityType,
      building: facility.building,
      location: facility.location,
      appearanceStatus: '正常',
      operationStatus: '正常',
      safetyParts: '完好',
      hasAbnormal: false,
      abnormalDescription: '',
      inspectionDate: inspectionDate.value,
      inspector: inspector.value
    }))
  } catch (error) {
    console.error('加载设施失败:', error)
  }
}

const batchSubmit = async () => {
  if (!inspectionDate.value || !inspector.value) {
    elError('请填写巡检日期和巡检人')
    return
  }
  
  const selectedItems = inspectionItems.value.filter(item => {
    return item.appearanceStatus !== '正常' || 
           item.operationStatus !== '正常' || 
           item.safetyParts !== '完好' ||
           item.abnormalDescription
  })
  
  if (selectedItems.length === 0) {
    elError('请选择或填写异常设施')
    return
  }
  
  selectedItems.forEach(item => {
    item.hasAbnormal = item.appearanceStatus !== '正常' || 
                       item.operationStatus !== '正常' || 
                       item.safetyParts !== '完好'
    item.inspectionDate = inspectionDate.value
    item.inspector = inspector.value
  })
  
  try {
    await axios.post('/api/inspections/batch', selectedItems)
    elSuccess('提交成功')
    router.push('/inspections')
  } catch (error) {
    elError('提交失败')
  }
}

const cancelForm = () => {
  router.push('/inspections')
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

const fetchBuildings = async () => {
  try {
    const res = await axios.get('/api/facilities')
    const buildingSet = new Set(res.data.map(f => f.building))
    buildings.value = Array.from(buildingSet)
  } catch (error) {
    console.error('获取楼栋列表失败:', error)
  }
}

onMounted(() => {
  fetchBuildings()
  const today = new Date()
  inspectionDate.value = today.toISOString().split('T')[0]
})
</script>

<style scoped>
.inspection-form-container {
  padding: 20px;
}

.batch-section {
  margin-bottom: 20px;
}

.inspection-list-card {
  margin-bottom: 20px;
}

.button-section {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
}
</style>