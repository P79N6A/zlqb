<template>
  <div class="layout-padding row justify-center">
    <div style="width: 500px; max-width: 90vw;">
      <p class="caption" style="margin-top: 120px;">
        Click on buttons and image below to open Popovers.
        <br>
        <em>This page has intended scroll so you can see multiple scenarios.</em>
      </p>

      <div>
        <q-btn color="primary">
          <q-icon name="assignment" />

          <q-popover ref="popover1">
            <q-list link separator class="scroll" style="min-width: 100px">
              <q-item
                v-for="n in 20"
                :key="n"
                @click="showToast(), $refs.popover1.close()"
              >
                <q-item-main label="Label" sublabel="Click me" />
              </q-item>
            </q-list>
          </q-popover>
        </q-btn>

        <q-btn ref="target4" color="negative">
          Disabled Popover

          <q-popover disable>
            This Popover content won't be shown because of "disable"
          </q-popover>
        </q-btn>

        <q-card style="margin-top: 75px">
          <q-card-title class="bg-primary text-center">
            <q-btn push color="orange">
              Tap Me

              <q-popover
                ref="popover2"
                :anchor="anchor"
                :self="self"
              >
                <q-list link style="min-width: 100px">
                  <q-item
                    v-for="n in 3"
                    :key="n"
                    @click="showToast(), $refs.popover2.close()"
                  >
                    <q-item-main label="Label" />
                  </q-item>
                </q-list>
              </q-popover>
            </q-btn>
          </q-card-title>

          <p class="caption text-center">Configure the Popover for button above.</p>
          <p class="text-center">
            <q-chip tag color="primary">anchor="{{anchor}}"</q-chip>
            <q-chip tag color="primary">self="{{self}}"</q-chip>
          </p>
          <q-card-main class="row">
            <div class="column items-center col-6">
              <p class="caption">Anchor Origin</p>
              <div class="flex">
                <div class="column group">
                  <div>Vertical</div>
                  <q-radio v-model="anchorOrigin.vertical" val="top" label="Top" />
                  <q-radio v-model="anchorOrigin.vertical" val="center" label="Center" />
                  <q-radio v-model="anchorOrigin.vertical" val="bottom" label="Bottom" />
                </div>
                <div class="column group">
                  <div>Horizontal</div>
                  <q-radio v-model="anchorOrigin.horizontal" val="left" label="Left" />
                  <q-radio v-model="anchorOrigin.horizontal" val="middle" label="Middle" />
                  <q-radio v-model="anchorOrigin.horizontal" val="right" label="Right" />
                </div>
              </div>
            </div>

            <div class="column items-center col-6">
              <p class="caption">Self Origin</p>
              <div class="flex">
                <div class="column group">
                  <div>Vertical</div>
                  <q-radio v-model="selfOrigin.vertical" val="top" label="Top" />
                  <q-radio v-model="selfOrigin.vertical" val="center" label="Center" />
                  <q-radio v-model="selfOrigin.vertical" val="bottom" label="Bottom" />
                </div>
                <div class="column group">
                  <div>Horizontal</div>
                  <q-radio v-model="selfOrigin.horizontal" val="left" label="Left" />
                  <q-radio v-model="selfOrigin.horizontal" val="middle" label="Middle" />
                  <q-radio v-model="selfOrigin.horizontal" val="right" label="Right" />
                </div>
              </div>
            </div>
          </q-card-main>
        </q-card>

        <div style="margin-bottom: 700px;"></div>

        <q-fixed-position corner="top-right" :offset="[16, 16]">
          <q-btn color="secondary">
            <q-icon name="directions" />

            <q-popover ref="popover3">
              <img
                src="~assets/map.png"
                style="height: 150px; width: 200px;"
                @click="showToast(), $refs.popover3.close()"
              >
            </q-popover>
          </q-btn>
        </q-fixed-position>

        <q-fixed-position corner="bottom-right" :offset="[16, 16]">
          <q-btn color="tertiary">
            <q-icon name="plus_one" />

            <q-popover ref="popover4">
              <div class="group" style="width: 220px; text-align: center;">
                <q-btn flat color="primary" @click="showToast(), $refs.popover4.close()">
                  <q-icon name="thumb_up" />
                </q-btn>
                <q-btn flat color="primary" @click="showToast(), $refs.popover4.close()">
                  <q-icon name="thumb_down" />
                </q-btn>
                <q-btn flat color="secondary" @click="showToast(), $refs.popover4.close()">
                  <q-icon name="share" />
                </q-btn>
              </div>
            </q-popover>
          </q-btn>
        </q-fixed-position>
      </div>

      <q-fixed-position corner="bottom-left" :offset="[16, 16]">
        <q-btn color="primary">
          <q-icon name="menu" />

          <q-popover ref="popover5">
            <q-list link separator class="scroll" style="min-width: 200px">
              <q-item
                v-for="n in 20"
                :key="n"
                @click="showToast(), $refs.popover5.close()"
              >
                <q-item-main label="Label" sublabel="Click me" />
              </q-item>
            </q-list>
          </q-popover>
        </q-btn>
      </q-fixed-position>
    </div>
  </div>
</template>

<script>
import {
  Toast,
  QBtn,
  QPopover,
  QList,
  QItem,
  QItemMain,
  QIcon,
  QRadio,
  QCard,
  QCardTitle,
  QCardMain,
  QChip,
  QFixedPosition
} from 'quasar'

export default {
  components: {
    QBtn,
    QPopover,
    QList,
    QItem,
    QItemMain,
    QIcon,
    QRadio,
    QCard,
    QCardTitle,
    QCardMain,
    QChip,
    QFixedPosition
  },
  data () {
    return {
      anchorOrigin: {vertical: 'bottom', horizontal: 'left'},
      selfOrigin: {vertical: 'top', horizontal: 'left'}
    }
  },
  computed: {
    anchor () {
      return `${this.anchorOrigin.vertical} ${this.anchorOrigin.horizontal}`
    },
    self () {
      return `${this.selfOrigin.vertical} ${this.selfOrigin.horizontal}`
    }
  },
  methods: {
    showToast () {
      Toast.create((this.$q.platform.is.desktop ? 'Clicked' : 'Tapped') + ' on a Popover item')
    }
  }
}
</script>
