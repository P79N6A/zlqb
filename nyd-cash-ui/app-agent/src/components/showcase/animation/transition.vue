<template>
  <div class="layout-padding row justify-center">
    <div style="width: 500px; max-width: 90vw;">
      <q-card style="margin-top: 25px">
        <q-card-title class="bg-primary text-center">
          <q-btn push color="orange" @click="show = !show">Toggle</q-btn>
        </q-card-title>
        <q-card-main class="row">
          <q-select class="col-xs-12 col-sm-6" filter v-model="enter" :options="enterSelectOptions" stack-label="CSS Enter Class" />
          <q-select class="col-xs-12 col-sm-6" filter v-model="leave" :options="leaveSelectOptions" stack-label="CSS Leave Class" />
        </q-card-main>
      </q-card>

      <q-card style="margin-top: 25px" class="overflow-hidden">
        <q-card-title class="text-center">
          Single
        </q-card-title>
        <q-card-main>
          <q-transition
            appear
            :enter="enter"
            :leave="leave"
            :disable="disable"
          >
            <div v-if="show" v-html="loremipsum"></div>
          </q-transition>
        </q-card-main>
      </q-card>

      <q-card style="margin-top: 25px" class="overflow-hidden">
        <q-card-title class="text-center">
          Group
        </q-card-title>
        <q-card-main>
          <q-transition
            group
            appear
            :enter="enter"
            :leave="leave"
            :disable="disable"
            class="group"
          >
            <div
              v-if="show"
              v-for="n in 3"
              :key="n"
              v-html="loremipsum"
            ></div>
          </q-transition>
        </q-card-main>
      </q-card>
    </div>
  </div>
</template>

<script>
import {
  generalAnimations,
  inAnimations,
  outAnimations
} from 'quasar-extras/animate/animate-list.js'

import {
  QTransition,
  QCard,
  QCardTitle,
  QCardMain,
  QBtn,
  QSelect
} from 'quasar'

function alphabetically (a, b) {
  return a.localeCompare(b)
}
function generateOptions (name) {
  return {
    label: name,
    value: name
  }
}

const enter = generalAnimations.concat(inAnimations).sort(alphabetically)
const leave = generalAnimations.concat(outAnimations).sort(alphabetically)

export default {
  components: {
    QTransition,
    QCard,
    QCardTitle,
    QCardMain,
    QBtn,
    QSelect
  },
  data () {
    return {
      enterSelectOptions: enter.map(generateOptions),
      leaveSelectOptions: leave.map(generateOptions),
      enter: 'bounceInLeft',
      leave: 'bounceOutRight',
      show: true,
      disable: false,
      loremipsum: 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.'
    }
  }
}
</script>
