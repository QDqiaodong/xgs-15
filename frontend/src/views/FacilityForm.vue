<template>
  <div class="facility-form-container">
    <el-card :title="isEdit ? '编辑设施' : '新增设施'">
      <el-form :model="form" ref="formRef" label-width="120px" class="facility-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设施编号" prop="facilityCode" :rules="[{required: true, message: '请输入设施编号'}]">
              <el-input v-model="form.facilityCode" placeholder="请输入设施编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设施名称" prop="facilityName" :rules="[{required: true, message: '请输入设施名称'}]">
              <el-input v-model="form.facilityName" placeholder="请输入设施名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设施类型" prop="facilityType" :rules="[{required: true, message: '请选择设施类型'}]">
              <el-select v-model="form.facilityType" placeholder="请选择设施类型">
                <el-option v-for="type in facilityTypes" :key="type" :label="type" :value="type" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在楼栋" prop="building" :rules="[{required: true, message: '请输入所在楼栋'}]">
              <el-input v-model="form.building" placeholder="请输入所在楼栋" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="安装位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入安装位置" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装日期" prop="installationDate">
              <el-date-picker v-model="form.installationDate" type="date" placeholder="请选择安装日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年检周期(月)" prop="annualInspectionCycle">
              <el-input v-model.number="form.annualInspectionCycle" type="number" placeholder="请输入年检周期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="正常" value="正常" />
                <el-option label="维护中" value="维护中" />
                <el-option label="故障" value="故障" />
                <el-option label="停用" value="停用" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产厂家" prop="manufacturer">
              <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入型号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remarks">
          <el-textarea v-model="form.remarks" placeholder="请输入备注" :rows="3" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="cancelForm">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const formRef = ref(null)
const isEdit = ref(false)
const facilityId = ref(null)

const facilityTypes = ['电梯', '消防柜', '水泵', '监控摄像头', '照明设施', '压力容器']

const form = reactive({
  facilityCode: '',
  facilityName: '',
  facilityType: '',
  building: '',
  location: '',
  installationDate: '',
  annualInspectionCycle: 12,
  manufacturer: '',
  model: '',
  status: '正常',
  remarks: ''
})

const submitForm = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await axios.put(`/api/facilities/${facilityId.value}`, form)
      elSuccess('修改成功')
    } else {
      await axios.post('/api/facilities', form)
      elSuccess('添加成功')
    }
    router.push('/facilities')
  } catch (error) {
    if (error.response?.status === 409) {
      elError('设施编号已存在')
    } else {
      elError('操作失败')
    }
  }
}

const cancelForm = () => {
  router.push('/facilities')
}

const fetchFacility = async (id) => {
  try {
    const res = await axios.get(`/api/facilities/${id}`)
    Object.assign(form, res.data)
    if (form.installationDate) {
      form.installationDate = new Date(form.installationDate).toISOString().split('T')[0]
    }
  } catch (error) {
    console.error('获取设施信息失败:', error)
  }
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
  if (route.params.id) {
    isEdit.value = true
    facilityId.value = route.params.id
    fetchFacility(route.params.id)
  }
})
</script>

<style scoped>
.facility-form-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.facility-form {
  padding: 20px;
}
</style>