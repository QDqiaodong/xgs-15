<template>
  <div class="annual-inspection-list-container">
    <div class="toolbar">
      <el-input 
        v-model="searchQuery" 
        placeholder="搜索设施编号、名称" 
        class="search-input"
      />
      <el-select v-model="filterType" placeholder="设施类型" class="filter-select">
        <el-option label="全部" value="" />
        <el-option label="电梯" value="电梯" />
        <el-option label="压力容器" value="压力容器" />
        <el-option label="消防柜" value="消防柜" />
      </el-select>
      <el-select v-model="filterStatus" placeholder="检测状态" class="filter-select">
        <el-option label="全部" value="" />
        <el-option label="待检测" value="待检测" />
        <el-option label="检测中" value="检测中" />
        <el-option label="合格" value="合格" />
        <el-option label="不合格" value="不合格" />
      </el-select>
      <el-button type="primary" @click="goToAdd">
        <el-icon><Plus /></el-icon> 新增年检
      </el-button>
    </div>

    <el-card>
      <el-table :data="filteredRecords" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="facilityCode" label="设施编号" />
        <el-table-column prop="facilityName" label="设施名称" />
        <el-table-column prop="facilityType" label="设施类型" />
        <el-table-column prop="inspectionDate" label="检测日期" />
        <el-table-column prop="inspectionAgency" label="检测机构" />
        <el-table-column prop="inspector" label="检测人员" />
        <el-table-column prop="qualificationStatus" label="检测结果">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.qualificationStatus)">{{ scope.row.qualificationStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reportNumber" label="报告编号" />
        <el-table-column prop="expiryDate" label="有效期至" />
        <el-table-column prop="rectificationStatus" label="整改状态">
          <template #default="scope">
            <el-tag :type="scope.row.rectificationStatus === '无缺陷' ? 'success' : 'warning'">
              {{ scope.row.rectificationStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="editRecord(scope.row)">编辑</el-button>
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
const filterType = ref('')
const filterStatus = ref('')
const records = ref([])

const getStatusType = (status) => {
  const types = {
    '待检测': 'info',
    '检测中': 'warning',
    '合格': 'success',
    '不合格': 'danger'
  }
  return types[status] || 'info'
}

const filteredRecords = computed(() => {
  let result = records.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(r => 
      r.facilityCode.toLowerCase().includes(query) ||
      r.facilityName.toLowerCase().includes(query)
    )
  }
  if (filterType.value) {
    result = result.filter(r => r.facilityType === filterType.value)
  }
  if (filterStatus.value) {
    result = result.filter(r => r.qualificationStatus === filterStatus.value)
  }
  return result
})

const goToAdd = () => {
  router.push('/annual-inspection/add')
}

const editRecord = (record) => {
  console.log('编辑年检记录:', record)
}

const deleteRecord = (id) => {
  elConfirm('确认删除', '确定要删除该年检记录吗？', () => {
    axios.delete(`/api/annual-inspection/${id}`).then(() => {
      fetchRecords()
      elSuccess('删除成功')
    }).catch(() => {
      elError('删除失败')
    })
  })
}

const fetchRecords = async () => {
  try {
    const res = await axios.get('/api/annual-inspection')
    records.value = res.data
  } catch (error) {
    console.error('获取年检记录失败:', error)
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
  fetchRecords()
})
</script>

<style scoped>
.annual-inspection-list-container {
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