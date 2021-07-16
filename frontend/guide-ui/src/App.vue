<template>
  <div id="app">
    <h1>unionj-generator-guide frontend vue2 + typescript scaffold</h1>
    <b-table striped hover :items="data" :fields="fields" fixed>
      <template #cell(avatar)="data">
        <img :src="data.value" alt="">
      </template>
    </b-table>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import axios from 'axios'
import UserService from './services/UserService'
import { ResultVOPageResultVOUserDetailVO, UserDetailVO, UserPageReqVO } from './services/types'

export default Vue.extend({
  name: 'App',
  data() {
    return {
      userService: {} as UserService,
      fields: [
        {
          key: 'id',
          label: 'ID',
        },
        {
          key: 'avatar',
          label: '头像',
        },
        {
          key: 'name',
          label: '姓名',
        },
        {
          key: 'age',
          label: '年龄',
        },
        {
          key: 'sex',
          label: '性别',
        },
      ],
      data: [] as UserDetailVO[],
    }
  },
  mounted() {
    this.userService = new UserService(axios)
    this.getAllUser()
  },
  methods: {
    getAllUser() {
      const payload = {
        size: 10,
        current: 1,
      } as UserPageReqVO
      this.userService.postApiUserPage(payload).then((resp: ResultVOPageResultVOUserDetailVO) => {
        if (resp.code === 0) {
          this.data = resp.data.items
          this.data.forEach((x) => {
            x.avatar = process.env.VUE_APP_BIZ_SERVICE + "/api/user/avatar?id=" + x.id
          })
        }
      })
    },
  },
})
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
img{
  width: 80px;
  height: auto;
}
</style>
