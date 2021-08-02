<template>
  <div>
    <a-input-search placeholder="Index" v-model="indexSearch" style="width: 400px" @search="indexAndSpecialSearchFunc" @change="indexAndSpecialSearchFunc"/>
    <a-input-search placeholder="Node Name" v-model="nodeSearch" style="width: 400px" @search="nodeSearchFunc" @change="nodeSearchFunc"/>
    <span>special({{ specialTotal }})</span><a-switch v-model="showSpecial" @change="indexAndSpecialSearchFunc" />
    <a-pagination
      size="small"
      v-model="currentPage"
      @change="pageChange"
      :total="total"
      :pageSize="pageSize"
      :show-total="total => `Total ${total}`"/>
    <a-table
      :columns="columns"
      :data-source="data"
      size="small"
      :pagination="false">
      <template slot="nodeCell" slot-scope="text">
        <span v-if="text">{{ text.name }}</span>
        <span v-else></span>
      </template>
      <template slot="shardCell" slot-scope="text">
        <a-dropdown v-for="(item, index) in text" :key="index" :trigger="['click']">
          <a-menu slot="overlay" @click="() => { console.log('click')}">
            <a-menu-item key="1">
              1st item
            </a-menu-item>
            <a-menu-item key="2">
              2nd item
            </a-menu-item>
            <a-menu-item key="3">
              3rd item
            </a-menu-item>
          </a-menu>
          <a-button size="small">{{ item.shard }}</a-button>
        </a-dropdown>
        <!-- <a-tooltip placement="left">
          <template slot="title">
            haha
          </template>
          <a-tag v-for="(item, index) in text" :key="index" :color="item.primary ? '#108ee9' : '#2db7f5'">
            {{ item.shard }}
          </a-tag>
        </a-tooltip> -->
      </template>
    </a-table>
  </div>
</template>
<script>
import config from './index.json'
export default {
  name: 'Index',
  data () {
    return {
      columns: [],
      sourceColumns: [],
      data: [],
      sourceData: [],
      showSpecial: false,
      // 只影响 columns
      indexSearch: '',
      // 只影响 data
      nodeSearch: '',
      currentPage: 1,
      pageSize: 10,
      specialTotal: 0,
      total: 0
    }
  },
  mounted () {
    this.refreshShards()
  },
  methods: {
    refreshShards () {
      // 发请求
      const specialTotal = config.columns.filter(item => item.startsWith('.')).length
      const columns = this.filterColumns(config.columns, this.indexSearch, this.showSpecial)
      this.total = columns.length
      const pageColumns = this.getPageColumns(columns, this.currentPage, this.pageSize)
      const data = this.filterData(config.shards, this.nodeSearch)
      this.sourceColumns = config.columns
      this.sourceData = config.shards
      this.specialTotal = specialTotal
      this.currentPage = 1
      this.columns = pageColumns
      this.data = data
      console.log('data', data)
      console.log('columns', columns)
    },
    // onSearch () {
    //   const columns = this.generateColumns(this.sourceColumns, this.search, this.special)

    // },
    getPageColumns (columns, currentPage, pageSize) {
      const col = columns.slice((currentPage - 1) * pageSize, currentPage * pageSize)
      const result = [{
        title: 'Node',
        dataIndex: '_node',
        sorter: (a, b) => {
          if (a._node === b._node) {
            return 0
          }
          if (a._node === null || a._node === undefined) {
            return -1
          }
          if (b._node === null || b._node === undefined) {
            return 1
          }
          return a._node.name.localeCompare(b._node.name)
        },
        scopedSlots: {
          customRender: 'nodeCell'
        }
      }]
      col.forEach(item => {
        result.push({
          dataIndex: item,
          title: item,
          scopedSlots: {
            customRender: 'shardCell'
          }
        })
      })
      return result
    },
    filterColumns (columns = [], search = '', showSpecial = true) {
      return columns.filter(item => {
        if (!showSpecial) {
          if (item.startsWith('.')) {
            return false
          }
        }
        if (search) {
          return item.indexOf(search) > -1
        }
        return true
      })
    },
    filterData (data = [], search = '') {
      return data.filter(item => {
        if (item._node) {
          return item._node.name.indexOf(search) > -1
        }
        return true
      })
    },
    indexAndSpecialSearchFunc () {
      const columns = this.filterColumns(this.sourceColumns, this.indexSearch, this.showSpecial)
      this.total = columns.length
      const pageColumns = this.getPageColumns(columns, this.currentPage, this.pageSize)
      this.columns = pageColumns
    },
    nodeSearchFunc () {
      this.data = this.filterData(this.sourceData, this.nodeSearch)
    },
    pageChange (page, pageSize) {
      this.indexAndSpecialSearchFunc()
    }
  }
}
</script>
