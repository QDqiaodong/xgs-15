import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import FacilityList from '@/views/FacilityList.vue'
import FacilityForm from '@/views/FacilityForm.vue'
import FacilityFloorPlan from '@/views/FacilityFloorPlan.vue'
import InspectionList from '@/views/InspectionList.vue'
import InspectionForm from '@/views/InspectionForm.vue'
import MaintenanceList from '@/views/MaintenanceList.vue'
import MaintenanceForm from '@/views/MaintenanceForm.vue'
import AnnualInspectionList from '@/views/AnnualInspectionList.vue'
import AnnualInspectionForm from '@/views/AnnualInspectionForm.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/facilities',
    name: 'FacilityList',
    component: FacilityList
  },
  {
    path: '/facilities/floor-plan',
    name: 'FacilityFloorPlan',
    component: FacilityFloorPlan
  },
  {
    path: '/facilities/add',
    name: 'FacilityAdd',
    component: FacilityForm
  },
  {
    path: '/facilities/:id/edit',
    name: 'FacilityEdit',
    component: FacilityForm
  },
  {
    path: '/inspections',
    name: 'InspectionList',
    component: InspectionList
  },
  {
    path: '/inspections/add',
    name: 'InspectionAdd',
    component: InspectionForm
  },
  {
    path: '/maintenance',
    name: 'MaintenanceList',
    component: MaintenanceList
  },
  {
    path: '/maintenance/add',
    name: 'MaintenanceAdd',
    component: MaintenanceForm
  },
  {
    path: '/annual-inspection',
    name: 'AnnualInspectionList',
    component: AnnualInspectionList
  },
  {
    path: '/annual-inspection/add',
    name: 'AnnualInspectionAdd',
    component: AnnualInspectionForm
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router