import * as types from './types'
export default{
	//更新菜单列表
	[types.UPDATE_ROLE_MENU_LIST](state,newData){
		state.rolerMenuList = newData
	},
	[types.UPDATE_DIFF_DAYS](state,newData){
		state.diffDays = newData
	},
}
