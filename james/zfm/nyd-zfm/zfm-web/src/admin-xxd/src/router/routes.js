export default [{
        path: '/',
        redirect: '/index',
        component: resolve => require(['@/page/Layout'], resolve),
        name: ''
    },
    {
        path: '/index',
        redirect: '/index',
        component: resolve => require(['@/page/Layout'], resolve),
        meta: {
            title: ['首页'],
            keepAlive: false,
        },
        children: [{
                path: '/index',
                name: 'Dashboard',
                meta: {
                    title: ['首页'],
                    keepAlive: true
                },
                component: resolve => require(['@/page/Index'], resolve),
            },

            // 客服管理模块
            {
                path: '/queryOrders',
                name: 'query_orders',
                meta: {
                    title: ['客服管理', '订单查询'],
                    keepAlive: true
                },
                component: resolve => require(['@/page/customerService/QueryOrders'], resolve),
            },
            {
                path: '/refundProcessing',
                name: 'RefundProcessing',
                meta: {
                    title: ['客服管理', '退款处理'],
                    keepAlive: true
                },
                component: resolve => require(['@/page/customerService/RefundProcessing'], resolve),
            },
            //信审管理
            {
                path: '/orderDistribution',
                name: 'OrderDistribution',
                meta: {
                    title: ['信审管理', '订单分配'],
                    keepAlive: true
                },
                component: resolve => require(['@/page/creditAduit/OrderDistribution'], resolve),
            },
            {
                path: '/orderApproval',
                name: 'OrderApproval',
                meta: {
                    title: ['信审管理', '订单审批'],
                    keepAlive: true
                },
                component: resolve => require(['@/page/creditAduit/OrderApproval'], resolve),
            },
            // 系统设置
            {
                path: '/sysMenuManger',
                component: resolve => require(['@/page/systermManger/SystermMenuManger'], resolve),
                meta: {
                    istoken: false,
                    title: ['系统设置', "菜单管理"]
                },
                name: 'sysMenuManger'
            },
            {
                path: '/sysRolerManger',
                component: resolve => require(['@/page/systermManger/SystermRolerManger'], resolve),
                meta: {
                    istoken: false,
                    title: ['系统设置', "角色管理"]
                },
                name: 'SystermRolerManger'
            },
            {
                path: '/sysUserManger',
                component: resolve => require(['@/page/systermManger/SystermUserManger'], resolve),
                meta: {
                    istoken: false,
                    title: ['系统设置', "用户管理"]
                },
                name: 'SystermUserManger'
            },
            {
                path: '/dictionary',
                component: resolve => require(['@/page/systermManger/Dictionary'], resolve),
                meta: {
                    istoken: false,
                    title: ['系统设置', "数据字典"]
                },
                name: 'Dictionary'
            },

        ]
    },
    {
        path: '/custOrderDetail',
        component: resolve => require(['@/components/CustomerServiceOrderDetail'], resolve),
        meta: {
            istoken: true,
            title: ['订单管理', "订单查询详情"]
        },
        name: 'CustomerServiceOrderDetail'
    },
    {
        path: '/refundOrderDetail',
        component: resolve => require(['@/components/RefundOrderDetail'], resolve),
        meta: {
            istoken: true,
            title: ['订单管理', "退款处理详情"]
        },
        name: 'RefundOrderDetail'
    },
    {
        path: '/distibuteOrderDetail',
        component: resolve => require(['@/components/DistibuteOrderDetail'], resolve),
        meta: {
            istoken: true,
            title: ['信审管理', "订单分配详情"]
        },
        name: 'DistibuteOrderDetail'
    },
    {
        path: '/creOrderDetail',
        component: resolve => require(['@/components/CreditAduitOrderDetail'], resolve),
        meta: {
            istoken: true,
            title: ['信审管理', "订单审批详情"]
        },
        name: 'CreditAduitOrderDetail'
    },
    {
        path: '*',
        name: 'index',
        redirect: '/index',
        meta: {
            title: '首页',
            keepAlive: false
        },
        components: {
            blank: resolve => require(['@/page/Layout'], resolve),
        }
    },


]