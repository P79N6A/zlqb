import * as types from './types'
import api from '@/api'
console.log(api, 1111111)
export default {
    //获取角色菜单列表
    [types.UPDATE_ROLE_MENU_LIST]({
        commit,
        state
    }, params) {
        return new Promise((resolve, reject) => {
            api.getUserModuleList(params).then((res) => {
                if (res.data.success) {
                    localStorage.setItem('menuList', JSON.stringify(res.data.data))
                    resolve(res.data.data);
                    commit(types.UPDATE_ROLE_MENU_LIST, res.data.data)
                } else {
                    vm.$message({
                        type: 'error',
                        message: res.data.msg
                    });
                }
            }).catch((err) => {
                console.log(err)
            })
        })
    },
    [types.GET_DIFF_DAYS]({
        commit,
        state
    }, params) {
        console.log(params, "paramsparamsparams")
        var dateSpan, tempDate, iDays,
            sDate1 = Date.parse(params[0]),
            sDate2 = Date.parse(params[1]);
        dateSpan = sDate2 - sDate1;
        dateSpan = Math.abs(dateSpan);
        iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
        console.log(iDays, "iDaysiDaysiDays")
        commit(types.UPDATE_DIFF_DAYS, iDays)
    }

}