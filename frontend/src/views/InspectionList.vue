<template>
  <div class="inspection-list-container">
    <div class="toolbar">
      <el-input 
        v-model="searchQuery" 
        placeholder="搜索设施编号、名称" 
        class="search-input"
      />
      <el-select v-model="filterBuilding" placeholder="选择楼栋" class="filter-select">
        <el-option label="全部" value="" />
        <el-option v-for="building in buildings" :key="building" :label="building" :value="building" />
      </el-select>
      <el-select v-model="filterAbnormal" placeholder="异常筛选" class="filter-select">
        <el-option label="全部" value="" />
        <el-option label="有异常" :value="true" />
        <el-option label="无异常" :value="false" />
      </el-select>
      <el-button type="primary" @click="goToAdd">
        <el-icon><Plus /></el-icon> 新增巡检
      </el-button>
    </div>

    <el-card>
      <el-table :data="filteredInspections" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="facilityCode" label="设施编号" />
        <el-table-column prop="facilityName" label="设施名称" />
        <el-table-column prop="building" label="楼栋" />
        <el-table-column prop="inspectionDate" label="巡检日期" />
        <el-table-column prop="appearanceStatus" label="外观状态" />
        <el-table-column prop="operationStatus" label="运行状态" />
        <el-table-column prop="safetyParts" label="安全部件" />
        <el-table-column prop="hasAbnormal" label="异常">
          <template #default="scope">
            <el-tag :type="scope.row.hasAbnormal ? 'danger' : 'success'">
              {{ scope.row.hasAbnormal ? '有异常' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="inspector" label="巡检人" />
        <el-table-column prop="createdAt" label="创建时间" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button size="small" @click="viewRecord(scope.row)">查看</el-button>
            <el-button size="small" type="danger" @click="deleteRecord(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Plus } from '@element-plus/icons-vue'

const router = useRouter()

const searchQuery = ref('')
const filterBuilding = ref('')
const filterAbnormal = ref('')
const inspections = ref([])

const buildings = computed(() => {
  const buildingSet = new Set(inspections.value.map(i => i.building))
  return Array.from(buildingSet)
})

const filteredInspections = computed(() => {
  let result = inspections.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(i => 
      i.facilityCode.toLowerCase().includes(query) ||
      i.facilityName.toLowerCase().includes(query)
    )
  }
  if (filterBuilding.value) {
    result = result.filter(i => i.building === filterBuilding.value)
  }
  if (filterAbnormal.value !== '') {
    result = result.filter(i => i.hasAbnormal === (filterAbnormal.value === 'true'))
  }
  return result
})

const goToAdd = () => {
  router.push('/inspections/add')
}

const viewRecord = (record) => {
  console.log('查看巡检记录:', record)
}

const deleteRecord = (id) => {
  elConfirm('确认删除', '确定要删除该巡检记录吗？', () => {
    axios.delete(`/api/inspections/${id}`).then(() => {
      fetchInspections()
      elSuccess('删除成功')
    }).catch(() => {
      elError('删除失败')
    })
  })
}

const fetchInspections = async () => {
  try {
    const res = await axios.get('/api/inspections')
    inspections.value = res.data
  } catch (error) {
    console.error('获取巡检记录失败:', error)
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
  fetchInspections()
})
</script>

<style scoped>
.inspection-list-container {
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
</style>