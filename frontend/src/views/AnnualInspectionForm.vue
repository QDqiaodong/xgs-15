<template>
  <div class="annual-inspection-form-container">
    <el-card title="新增特种设备年检记录">
      <el-form :model="form" ref="formRef" label-width="120px" class="annual-inspection-form">
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
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设施类型" prop="facilityType">
              <el-select v-model="form.facilityType" placeholder="请选择设施类型">
                <el-option label="电梯" value="电梯" />
                <el-option label="压力容器" value="压力容器" />
                <el-option label="消防柜" value="消防柜" />
                <el-option label="水泵" value="水泵" />
                <el-option label="监控摄像头" value="监控摄像头" />
                <el-option label="照明设施" value="照明设施" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测日期" prop="inspectionDate" :rules="[{required: true, message: '请选择检测日期'}]">
              <el-date-picker v-model="form.inspectionDate" type="date" placeholder="请选择检测日期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检测机构" prop="inspectionAgency" :rules="[{required: true, message: '请输入检测机构'}]">
              <el-input v-model="form.inspectionAgency" placeholder="请输入检测机构" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测人员" prop="inspector">
              <el-input v-model="form.inspector" placeholder="请输入检测人员" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检测结果" prop="qualificationStatus">
              <el-select v-model="form.qualificationStatus" placeholder="请选择检测结果">
                <el-option label="待检测" value="待检测" />
                <el-option label="检测中" value="检测中" />
                <el-option label="合格" value="合格" />
                <el-option label="不合格" value="不合格" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告编号" prop="reportNumber">
              <el-input v-model="form.reportNumber" placeholder="请输入报告编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="有效期至" prop="expiryDate">
              <el-date-picker v-model="form.expiryDate" type="date" placeholder="请选择有效期至" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改状态" prop="rectificationStatus">
              <el-select v-model="form.rectificationStatus" placeholder="请选择整改状态">
                <el-option label="无缺陷" value="无缺陷" />
                <el-option label="整改中" value="整改中" />
                <el-option label="已整改" value="已整改" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检测项目" prop="inspectionItems">
          <el-textarea v-model="form.inspectionItems" placeholder="请输入检测项目" :rows="3" />
        </el-form-item>
        <el-form-item label="发现缺陷" prop="defectsFound">
          <el-textarea v-model="form.defectsFound" placeholder="请输入发现的缺陷" :rows="3" />
        </el-form-item>
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
  facilityType: '',
  inspectionDate: '',
  inspectionAgency: '',
  inspector: '',
  qualificationStatus: '待检测',
  reportNumber: '',
  expiryDate: '',
  inspectionItems: '',
  defectsFound: '',
  rectificationStatus: '无缺陷',
  remarks: ''
})

const submitForm = async () => {
  try {
    await formRef.value.validate()
    await axios.post('/api/annual-inspection', form)
    elSuccess('添加成功')
    router.push('/annual-inspection')
  } catch (error) {
    elError('操作失败')
  }
}

const cancelForm = () => {
  router.push('/annual-inspection')
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
  form.inspectionDate = today.toISOString().split('T')[0]
})
</script>

<style scoped>
.annual-inspection-form-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.annual-inspection-form {
  padding: 20px;
}
</style>