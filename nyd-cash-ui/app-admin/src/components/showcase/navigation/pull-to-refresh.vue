<template>
  <q-pull-to-refresh :handler="refresher">
    <div class="layout-padding bg-white">
      <p class="caption">
        Pull down to refresh on the content below.
        On desktop it works by dragging the content down.
      </p>

      <p v-for="(item, index) in items" class="caption">
        <q-chip square color="secondary" class="shadow-1">
          {{ items.length - index }}
        </q-chip>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
      </p>
    </div>
  </q-pull-to-refresh>
</template>

<script>
import {
  QPullToRefresh,
  QChip,
  Toast
} from 'quasar'

export default {
  components: {
    QPullToRefresh,
    QChip
  },
  data () {
    return {
      items: [{}, {}, {}, {}, {}, {}]
    }
  },
  methods: {
    refresher (done) {
      setTimeout(() => {
        done()
        this.items.push({})
        Toast.create('Item #' + this.items.length + ' is new.')
      }, 1000)
    }
  }
}
</script>
