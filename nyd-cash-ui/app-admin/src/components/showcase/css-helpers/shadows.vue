<template>
  <div class="layout-padding">
    <p class="caption text-left">Standard shadows</p>
    <div class="flex inline shadow-box flex-center" v-for="n in 24" :class="['shadow-' + n]">.shadow-{{n}}</div>
    <p class="caption text-left">Shadows pointing up</p>
    <div class="flex inline shadow-box flex-center" v-for="n in 24" :class="['shadow-up-' + n]">.shadow-up-{{n}}</div>
    <p class="caption text-left">Inset Shadow</p>
    <div class="flex inline shadow-box flex-center inset-shadow doc-inset-shadow">.inset-shadow</div>
  </div>
</template>

<style lang="styl">
@import '~variables'

.shadow-box
  width 90px
  height 90px
  margin 25px
  border-radius 50%
  font-size 12px
.doc-inset-shadow
  border 1px solid $grey-3
</style>
