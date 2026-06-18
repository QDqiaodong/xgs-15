<template>
  <div class="maintenance-form-container">
    <el-card title="新增维修记录">
      <el-form :model="form" ref="formRef" label-width="120px" class="maintenance-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设施编号" prop="facilityCode" :rules="[{required: true, message: '请输入设施编号'}]">
              <el-input v-model="form.facilityCode" placeholder="请输入设施编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设施名称" prop="facilityName">
              <el-input v-model="form.facilityName" placeholder="请输入设施名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="故障描述" prop="faultDescription" :rules="[{required: true, message: '请输入故障描述'}]">
          <el-textarea v-model="form.faultDescription" placeholder="请输入故障描述" :rows="3" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维修日期" prop="repairDate">
              <el-date-picker v-model="form.repairDate" type="date" placeholder="请选择维修日期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修人员" prop="repairPerson">
              <el-input v-model="form.repairPerson" placeholder="请输入维修人员" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="更换配件" prop="replacedParts">
              <el-input v-model="form.replacedParts" placeholder="请输入更换配件" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维修费用" prop="repairCost">
              <el-input v-model.number="form.repairCost" type="number" placeholder="请输入维修费用" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维修状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="待维修" value="待维修" />
                <el-option label="维修中" value="维修中" />
                <el-option label="已完成" value="已完成" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="验收状态" prop="acceptanceStatus">
              <el-select v-model="form.acceptanceStatus" placeholder="请选择验收状态">
                <el-option label="未验收" value="未验收" />
                <el-option label="已验收" value="已验收" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="验收日期" prop="acceptanceDate">
              <el-date-picker v-model="form.acceptanceDate" type="date" placeholder="请选择验收日期" />
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const formRef = ref(null)

const form = reactive({
  facilityCode: '',
  facilityName: '',
  faultDescription: '',
  repairDate: '',
  replacedParts: '',
  repairCost: 0,
  repairPerson: '',
  acceptanceStatus: '未验收',
  acceptanceDate: '',
  status: '待维修',
  remarks: ''
})

const submitForm = async () => {
  try {
    await formRef.value.validate()
    await axios.post('/api/maintenance', form)
    elSuccess('添加成功')
    router.push('/maintenance')
  } catch (error) {
    elError('操作失败')
  }
}

const cancelForm = () => {
  router.push('/maintenance')
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
  const today = new Date()
  form.repairDate = today.toISOString().split('T')[0]
})
</script>

<style scoped>
.maintenance-form-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.maintenance-form {
  padding: 20px;
}
</style>