<template>
  <div class="maintenance-list-container">
    <div class="toolbar">
      <el-input 
        v-model="searchQuery" 
        placeholder="搜索设施编号、名称" 
        class="search-input"
      />
      <el-select v-model="filterStatus" placeholder="状态筛选" class="filter-select">
        <el-option label="全部" value="" />
        <el-option label="待维修" value="待维修" />
        <el-option label="维修中" value="维修中" />
        <el-option label="已完成" value="已完成" />
      </el-select>
      <el-select v-model="filterAcceptance" placeholder="验收状态" class="filter-select">
        <el-option label="全部" value="" />
        <el-option label="未验收" value="未验收" />
        <el-option label="已验收" value="已验收" />
      </el-select>
      <el-button type="primary" @click="goToAdd">
        <el-icon><Plus /></el-icon> 新增维修
      </el-button>
    </div>

    <el-card>
      <el-table :data="filteredMaintenance" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="facilityCode" label="设施编号" />
        <el-table-column prop="facilityName" label="设施名称" />
        <el-table-column prop="faultDescription" label="故障描述" />
        <el-table-column prop="repairDate" label="维修日期" />
        <el-table-column prop="replacedParts" label="更换配件" />
        <el-table-column prop="repairCost" label="维修费用">
          <template #default="scope">
            ¥{{ scope.row.repairCost || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="repairPerson" label="维修人员" />
        <el-table-column prop="status" label="维修状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="acceptanceStatus" label="验收状态">
          <template #default="scope">
            <el-tag :type="scope.row.acceptanceStatus === '已验收' ? 'success' : 'warning'">
              {{ scope.row.acceptanceStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" />
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
const filterStatus = ref('')
const filterAcceptance = ref('')
const maintenanceList = ref([])

const getStatusType = (status) => {
  const types = {
    '待维修': 'warning',
    '维修中': 'info',
    '已完成': 'success'
  }
  return types[status] || 'info'
}

const filteredMaintenance = computed(() => {
  let result = maintenanceList.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(m => 
      m.facilityCode.toLowerCase().includes(query) ||
      m.facilityName.toLowerCase().includes(query)
    )
  }
  if (filterStatus.value) {
    result = result.filter(m => m.status === filterStatus.value)
  }
  if (filterAcceptance.value) {
    result = result.filter(m => m.acceptanceStatus === filterAcceptance.value)
  }
  return result
})

const goToAdd = () => {
  router.push('/maintenance/add')
}

const editRecord = (record) => {
  console.log('编辑维修记录:', record)
}

const deleteRecord = (id) => {
  elConfirm('确认删除', '确定要删除该维修记录吗？', () => {
    axios.delete(`/api/maintenance/${id}`).then(() => {
      fetchMaintenance()
      elSuccess('删除成功')
    }).catch(() => {
      elError('删除失败')
    })
  })
}

const fetchMaintenance = async () => {
  try {
    const res = await axios.get('/api/maintenance')
    maintenanceList.value = res.data
  } catch (error) {
    console.error('获取维修记录失败:', error)
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
  fetchMaintenance()
})
</script>

<style scoped>
.maintenance-list-container {
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