<template>
  <div class="facility-form-container">
    <el-card :title="isEdit ? '编辑设施' : '新增设施'">
      <el-form :model="form" ref="formRef" label-width="120px" class="facility-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设施编号" prop="facilityCode">
              <el-input 
                v-model="form.facilityCode" 
                :placeholder="codePlaceholder"
                :prefix-icon="Warning"
              >
                <template #append>
                  <el-button @click="generateSuggestedCode" :disabled="!canGenerateCode">自动生成</el-button>
                </template>
              </el-input>
              <div v-if="form.facilityType && form.building" class="code-format-hint">
                格式要求: <el-tag type="info" size="small">{{ expectedFormat }}</el-tag>
                <span v-if="isSupervisedType" class="supervised-hint">
                  <el-icon style="color: #f56c6c; vertical-align: middle;"><WarningFilled /></el-icon>
                  监管类设施
                </span>
              </div>
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
              <el-select v-model="form.facilityType" placeholder="请选择设施类型" @change="onTypeOrBuildingChange">
                <el-option v-for="type in facilityTypes" :key="type" :label="type" :value="type" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在楼栋" prop="building" :rules="[{required: true, message: '请输入所在楼栋'}]">
              <el-input v-model="form.building" placeholder="如: A栋、1号楼" @change="onTypeOrBuildingChange" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所在楼层" prop="floor">
              <el-input-number v-model="form.floor" :min="0" :max="999" placeholder="请输入楼层" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入安装位置" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="安装日期" prop="installationDate">
              <el-date-picker v-model="form.installationDate" type="date" placeholder="请选择安装日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年检周期(月)" prop="annualInspectionCycle">
              <el-input v-model.number="form.annualInspectionCycle" type="number" placeholder="请输入年检周期" />
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
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注" :rows="3" />
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { Warning, WarningFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const formRef = ref(null)
const isEdit = ref(false)
const facilityId = ref(null)

const facilityTypes = ['电梯', '消防柜', '水泵', '监控摄像头', '照明设施', '压力容器']

const typeCodeMap = {
  '电梯': 'DT',
  '消防柜': 'XF',
  '水泵': 'SB',
  '监控摄像头': 'JK',
  '照明设施': 'ZM',
  '压力容器': 'YL'
}

const supervisedTypes = ['电梯', '消防柜', '压力容器']

const form = reactive({
  facilityCode: '',
  facilityName: '',
  facilityType: '',
  building: '',
  floor: null,
  location: '',
  installationDate: '',
  annualInspectionCycle: 12,
  manufacturer: '',
  model: '',
  status: '正常',
  remarks: ''
})

const existingCodes = ref([])

const isSupervisedType = computed(() => supervisedTypes.includes(form.facilityType))

const canGenerateCode = computed(() => form.facilityType && form.building)

const expectedBuildingCode = computed(() => {
  if (!form.building) return ''
  return form.building.replace(/[^0-9A-Za-z]/g, '')
})

const expectedFormat = computed(() => {
  const typeCode = typeCodeMap[form.facilityType] || 'XX'
  const buildingCode = expectedBuildingCode.value || '楼栋代码'
  return `${typeCode}-${buildingCode}-XXX`
})

const codePlaceholder = computed(() => {
  if (form.facilityType && form.building) {
    return `请输入编号，如: ${expectedFormat.value}`
  }
  return '请选择类型和楼栋后填写'
})

const fetchExistingCodes = async () => {
  try {
    const res = await axios.get('/api/facilities')
    existingCodes.value = res.data.map(f => f.facilityCode)
  } catch (error) {
    console.error('获取设施编号列表失败:', error)
  }
}

const generateSuggestedCode = async () => {
  const typeCode = typeCodeMap[form.facilityType]
  const buildingCode = expectedBuildingCode.value
  if (!typeCode || !buildingCode) return

  const prefix = `${typeCode}-${buildingCode}-`
  const regex = new RegExp(`^${prefix}(\\d+)$`)

  try {
    if (existingCodes.value.length === 0) {
      await fetchExistingCodes()
    }
  } catch (e) {}

  let maxSeq = 0
  existingCodes.value.forEach(code => {
    const match = code.match(regex)
    if (match) {
      const seq = parseInt(match[1], 10)
      if (seq > maxSeq) maxSeq = seq
    }
  })

  const nextSeq = (maxSeq + 1).toString().padStart(3, '0')
  form.facilityCode = `${prefix}${nextSeq}`
}

const validateFacilityCode = async (rule, value, callback) => {
  if (!value || !value.trim()) {
    callback(new Error('请输入设施编号'))
    return
  }
  if (!form.facilityType) {
    callback(new Error('请先选择设施类型'))
    return
  }
  if (!form.building) {
    callback(new Error('请先输入所在楼栋'))
    return
  }
  const typeCode = typeCodeMap[form.facilityType]
  const buildingCode = expectedBuildingCode.value
  if (!typeCode || !buildingCode) {
    callback(new Error('设施类型或楼栋名称无效'))
    return
  }
  const regex = new RegExp(`^${typeCode}-${buildingCode.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}-\\d{3,}$`)
  if (!regex.test(value)) {
    callback(new Error(`格式错误，应为: ${typeCode}-${buildingCode}-XXX(至少3位序号)`))
    return
  }

  try {
    if (existingCodes.value.length === 0) {
      await fetchExistingCodes()
    }
  } catch (e) {}

  const isDuplicate = existingCodes.value.some(code => {
    if (isEdit.value && code === value && form.facilityCode === route.params.id) return false
    return code === value && (!isEdit.value || code !== originalCode.value)
  })

  if (isDuplicate) {
    callback(new Error('该设施编号已存在，禁止重复编号进入台账'))
    return
  }

  callback()
}

const originalCode = ref('')

const onTypeOrBuildingChange = () => {}

const submitForm = async () => {
  try {
    const customRules = {
      facilityCode: [
        { required: true, validator: validateFacilityCode, trigger: 'blur' }
      ]
    }
    formRef.value.rules = { ...formRef.value.rules, ...customRules }
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
      elError(error.response.data || '设施编号已存在')
    } else if (error.response?.status === 400) {
      elError(error.response.data || '设施编号格式错误')
    } else if (error !== undefined && !error?.field) {
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
    originalCode.value = res.data.facilityCode
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

onMounted(async () => {
  await fetchExistingCodes()
  if (route.params.id) {
    isEdit.value = true
    facilityId.value = route.params.id
    await fetchFacility(route.params.id)
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

.code-format-hint {
  margin-top: 6px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.supervised-hint {
  color: #f56c6c;
  font-weight: 600;
}
</style>
