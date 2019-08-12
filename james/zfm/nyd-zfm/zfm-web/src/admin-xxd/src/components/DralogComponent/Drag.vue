<template>
    <div v-drag-dialog class='drag-dialog-wrap' :style='styleObj'>
      <slot></slot>
    </div>
</template>

<script>
import DragDialog from '@/directive/drag-dialog'
export default {
  directives: {DragDialog},
  props: {
   styleObj: {
     type: Object,
     default() {
       return {
         position: 'fixed',
          top: '20px',
          left: '1020px',
          width: '400px',
          height: '300px',
          background:'#ffffff',
          border:"#ddd solid 1px",
          zIndex: 3000,
          userSelect: 'none',
          padding:'10px',

       }
     }
   }
  },
  data() {
    return {};
  },
 
};
</script>

<style scoped lang="scss">
 .drag-dialog-wrap {
    
  }
</style>