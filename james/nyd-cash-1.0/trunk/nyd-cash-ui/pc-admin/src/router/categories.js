var categories = [{
  "text": "首页",
  "module": "index",
}, {
  "text": "基本信息",
  "module": "baseinfo",
  "icon": "ui-icon ui-icon-instorage",
  "sub": [
    {
      "text": "用户管理",
      "icon": "blur_on",
      "id": "user",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "商品品类管理",
      "icon": "blur_on",
      "id": "goods",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "商品分类管理",
      "icon": "blur_on",
      "id": "goods_classify",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "商品管理",
      "icon": "blur_on",
      "id": "goods",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "库区库位",
      "icon": "blur_on",
      "id": "location",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }
  ]
}, {
  "text": "生产管理",
  "module": "production",
  "icon": "ui-icon",
  "sub": [{
    "text": "生产配置",
    "icon": "blur_on",
    "id": "config",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }, {
    "text": "生产报表",
    "icon": "blur_on",
    "id": "report",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }]
}, {
  "text": "产品管理",
  "module": "product",
  "icon": "ui-icon",
  "sub": [{
    "text": "产品配置",
    "icon": "blur_on",
    "id": "config",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }, {
    "text": "产品报表",
    "icon": "blur_on",
    "id": "report",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }]
}, {
  "text": "工种管理",
  "module": "server",
  "icon": "ui-icon",
  "sub": [{
    "text": "维修人员",
    "icon": "blur_on",
    "id": "repairer",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }, {
    "text": "安装人员",
    "icon": "blur_on",
    "id": "installer",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }]
}, {
  "text": "工单管理",
  "module": "worksheet",
  "icon": "ui-icon",
  "sub": [{
    "text": "维修订单",
    "icon": "blur_on",
    "id": "repair",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }, {
    "text": "安装订单",
    "icon": "blur_on",
    "id": "install",
    "actions": {
      "detail": "detail",
      "add": "edit",
      "edit": "edit",
      "index": "index"
    }
  }]
}, {
  "text": "入库作业",
  "module": "instorage_action",
  "icon": "ui-icon ui-icon-stock",
  "sub": [
    {
      "text": "采购入库交接",
      "icon": "blur_on",
      "id": "procurement_storage"
    }, {
      "text": "销售退货入库交接",
      "icon": "blur_on",
      "id": "sales_returns_warehousing"
    }, {
      "text": "入库上架",
      "icon": "blur_on",
      "id": "warehouse_shelves"
    }
  ]
}, {
  "text": "出库作业",
  "module": "outstorage_action",
  "icon": "ui-icon ui-icon-outgoing",
  "sub": [
    {
      "text": "销售出库",
      "icon": "blur_on",
      "id": "sell_out"
    }, {
      "text": "波次拣货",
      "icon": "blur_on",
      "id": "wave_picking"
    }, {
      "text": "播种墙",
      "icon": "blur_on",
      "id": "seeding_wall"
    }, {
      "text": "出库复核",
      "icon": "blur_on",
      "id": "outstock_check"
    }, {
      "text": "采购退货出库",
      "icon": "blur_on",
      "id": "purchase_returns"
    }
  ]
}, {
  "text": "库内作业",
  "module": "storage_action",
  "icon": "ui-icon ui-icon-user",
  "sub": [
    {
      "text": "库位库存",
      "icon": "blur_on",
      "id": "warehouse_stock"
    }, {
      "text": "库存调整",
      "icon": "blur_on",
      "id": "inventory_adjustment"
    }, {
      "text": "盘点",
      "icon": "blur_on",
      "id": "inventory"
    }, {
      "text": "移库记录",
      "icon": "blur_on",
      "id": "transfer_list"
    }, {
      "text": "补货记录",
      "icon": "blur_on",
      "id": "replenishment_list"
    }, {
      "text": "库存预警",
      "icon": "blur_on",
      "id": "inventory_warning"
    }
  ]
}, {
  "text": "数据报表",
  "module": "datachart",
  "icon": "ui-icon ui-icon-user",
  "sub": [
    {
      "text": "商品库存",
      "icon": "blur_on",
      "id": "goods_stock"
    }, {
      "text": "入库",
      "icon": "blur_on",
      "id": "instorage"
    }, {
      "text": "出库",
      "icon": "blur_on",
      "id": "outstorage"
    }, {
      "text": "盘点损益",
      "icon": "blur_on",
      "id": "inventory_profit_loss"
    }, {
      "text": "销售出库排行",
      "icon": "blur_on",
      "id": "sales_list"
    }, {
      "text": "仓库日志",
      "icon": "blur_on",
      "id": "warehouse_log"
    }
  ]
}, {
  "text": "数据字典",
  "module": "dictionary",
  "icon": "ui-icon ui-icon-user",
  "sub": [
    {
      "text": "工厂字典",
      "icon": "blur_on",
      "id": "factory",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "品牌字典",
      "icon": "blur_on",
      "id": "brand",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "系列字典",
      "icon": "blur_on",
      "id": "series",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "型号字典",
      "icon": "blur_on",
      "id": "model",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }, {
      "text": "仓库字典",
      "icon": "blur_on",
      "id": "warehouse",
      "actions": {
        "detail": "detail",
        "add": "edit",
        "edit": "edit",
        "index": "index"
      }
    }
  ]
}, {
  "text": "系统配置",
  "module": "sys_setting",
  "icon": "ui-icon ui-icon-user",
  "sub": [
    {
      "text": "操作日志",
      "icon": "blur_on",
      "id": "operation_log"
    }, {
      "text": "库区字典",
      "icon": "blur_on",
      "id": "reservoir_dictionary"
    }, {
      "text": "库位顺序",
      "icon": "blur_on",
      "id": "library_sequence"
    }, {
      "text": "仓库员工",
      "icon": "blur_on",
      "id": "warehouse_staff"
    }, {
      "text": "岗位管理",
      "icon": "blur_on",
      "id": "post_manage"
    }, {
      "text": "部门管理",
      "icon": "blur_on",
      "id": "department_manage"
    }, {
      "text": "业务规则",
      "icon": "blur_on",
      "id": "business_rules"
    }
  ]
}]

export default categories;
