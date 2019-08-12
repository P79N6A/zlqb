<template>
  <div id="chart" style="width: 100%;height:100%;"></div>
</template>
<script>


  var echarts = require('echarts');
  var citys = [
    '北京', '天津', '河北', '山西', '内蒙古', '辽宁', '吉林', '黑龙江', '上海', '江苏', '浙江',
    '安徽', '福建', '江西', '山东', '河南', '湖北', '湖南', '广东', '广西', '海南', '重庆',
    '四川', '贵州', '云南', '西藏', '陕西', '甘肃', '青海', '宁夏', '新疆'
  ];
  var dataMap = {
    dataPI: dataFormatter({
      //max : 4000,
      2011: [136.27, 159.72, 2905.73, 641.42, 1306.3, 1915.57, 1277.44, 1701.5, 124.94, 3064.78, 1583.04, 2015.31, 1612.24, 1391.07, 3973.85, 3512.24, 2569.3, 2768.03, 2665.2, 2047.23, 659.23, 844.52, 2983.51, 726.22, 1411.01, 74.47, 1220.9, 678.75, 155.08, 184.14, 1139.03],
      2010: [124.36, 145.58, 2562.81, 554.48, 1095.28, 1631.08, 1050.15, 1302.9, 114.15, 2540.1, 1360.56, 1729.02, 1363.67, 1206.98, 3588.28, 3258.09, 2147, 2325.5, 2286.98, 1675.06, 539.83, 685.38, 2482.89, 625.03, 1108.38, 68.72, 988.45, 599.28, 134.92, 159.29, 1078.63],
      2009: [118.29, 128.85, 2207.34, 477.59, 929.6, 1414.9, 980.57, 1154.33, 113.82, 2261.86, 1163.08, 1495.45, 1182.74, 1098.66, 3226.64, 2769.05, 1795.9, 1969.69, 2010.27, 1458.49, 462.19, 606.8, 2240.61, 550.27, 1067.6, 63.88, 789.64, 497.05, 107.4, 127.25, 759.74],
      2008: [112.83, 122.58, 2034.59, 313.58, 907.95, 1302.02, 916.72, 1088.94, 111.8, 2100.11, 1095.96, 1418.09, 1158.17, 1060.38, 3002.65, 2658.78, 1780, 1892.4, 1973.05, 1453.75, 436.04, 575.4, 2216.15, 539.19, 1020.56, 60.62, 753.72, 462.27, 105.57, 118.94, 691.07]
    }),
    dataSI: dataFormatter({
      //max : 26600,
      2011: [3752.48, 5928.32, 13126.86, 6635.26, 8037.69, 12152.15, 5611.48, 5962.41, 7927.89, 25203.28, 16555.58, 8309.38, 9069.2, 6390.55, 24017.11, 15427.08, 9815.94, 9361.99, 26447.38, 5675.32, 714.5, 5543.04, 11029.13, 2194.33, 3780.32, 208.79, 6935.59, 2377.83, 975.18, 1056.15, 3225.9],
      2010: [3388.38, 4840.23, 10707.68, 5234, 6367.69, 9976.82, 4506.31, 5025.15, 7218.32, 21753.93, 14297.93, 6436.62, 7522.83, 5122.88, 21238.49, 13226.38, 7767.24, 7343.19, 23014.53, 4511.68, 571, 4359.12, 8672.18, 1800.06, 3223.49, 163.92, 5446.1, 1984.97, 744.63, 827.91, 2592.15],
      2009: [2855.55, 3987.84, 8959.83, 3993.8, 5114, 7906.34, 3541.92, 4060.72, 6001.78, 18566.37, 11908.49, 4905.22, 6005.3, 3919.45, 18901.83, 11010.5, 6038.08, 5687.19, 19419.7, 3381.54, 443.43, 3448.77, 6711.87, 1476.62, 2582.53, 136.63, 4236.42, 1527.24, 575.33, 662.32, 1929.59],
      2008: [2626.41, 3709.78, 8701.34, 4242.36, 4376.19, 7158.84, 3097.12, 4319.75, 6085.84, 16993.34, 11567.42, 4198.93, 5318.44, 3554.81, 17571.98, 10259.99, 5082.07, 5028.93, 18502.2, 3037.74, 423.55, 3057.78, 5823.39, 1370.03, 2452.75, 115.56, 3861.12, 1470.34, 557.12, 609.98, 2070.76]
    })
  }

  function dataFormatter(obj) {
    var pList = citys
    var temp;
    for (var year = 2008; year <= 2011; year++) {
      var max = 0;
      var sum = 0;
      temp = obj[year];
      for (var i = 0, l = temp.length; i < l; i++) {
        max = Math.max(max, temp[i]);
        sum += temp[i];
        obj[year][i] = {
          name: pList[i],
          value: temp[i]
        }
      }
      obj[year + 'max'] = Math.floor(max / 100) * 100;
      obj[year + 'sum'] = sum;
    }
    return obj;
  }

  export default {
    data() {
      return {
        option: {
          baseOption: {
            timeline: {
              // y: 0,
              axisType: 'category',
              // realtime: false,
              // loop: false,
              autoPlay: true,
              // currentIndex: 2,
              playInterval: 2000,
              // controlStyle: {
              //     position: 'left'
              // },
              data: [
                '2008-01-01', '2009-01-01', '2010-01-01',
                {
                  value: '2011-01-01',
                  tooltip: {
                    formatter: function (params) {
                      return params.name + 'GDP达到又一个高度';
                    }
                  },
                  symbol: 'diamond',
                  symbolSize: 18
                },
              ],


              label: {
                formatter: function (s) {
                  return (new Date(s)).getFullYear();
                }
              }
            },
            title: {
              subtext: '数据来自国家统计局'
            },
            tooltip: {},
            legend: {
              x: 'right',
              data: ['安装', '维修'],
            },
            calculable: true,
            grid: {
              top: 80,
              bottom: 100,
              tooltip: {
                trigger: 'axis',
                axisPointer: {
                  type: 'shadow',
                  label: {
                    show: true,
                    formatter: function (params) {
                      return params.value.replace('\n', '');
                    }
                  }
                }
              }
            },
            xAxis: [
              {
                'type': 'category',
                'axisLabel': {'interval': 0},
                'data': citys,
                'splitLine': {show: false}
              }
            ],
            yAxis: [
              {
                type: 'value',
                name: '人数'
              }
            ],
            series: [
              {name: '安装', type: 'bar'},
              {name: '维修', type: 'bar'},
              {
                name: '人数占比',
                type: 'pie',
                center: ['75%', '35%'],
                radius: '28%',
                z: 100
              }
            ]
          },
          options: [
            {
              title: {text: '2008全国安装维修人员'},
              series: [
                {
                  data: dataMap.dataPI['2008']
                }, {
                  data: dataMap.dataSI['2008']
                }, {
                  data: [{
                    name: '安装',
                    value: dataMap.dataPI['2008sum']
                  }, {
                    name: '维修',
                    value: dataMap.dataSI['2008sum']
                  }]
                }
              ]
            }, {
              title: {text: '2009全国安装维修人员'},
              series: [
                {data: dataMap.dataPI['2009']},
                {data: dataMap.dataSI['2009']},
                {data: [
                  {name: '安装', value: dataMap.dataPI['2009sum']},
                  {name: '维修', value: dataMap.dataSI['2009sum']}
                ]
                }


              ]
            }, {
              title: {text: '2010全国安装维修人员'},
              series: [
                {data: dataMap.dataPI['2010']},
                {data: dataMap.dataSI['2010']},
                {
                  data: [
                    {name: '安装', value: dataMap.dataPI['2010sum']},
                    {name: '维修', value: dataMap.dataSI['2010sum']}
                  ]
                }
              ]
            }, {
              title: {text: '2011全国安装维修人员'},
              series: [
                {data: dataMap.dataPI['2011']},
                {data: dataMap.dataSI['2011']},
                {
                  data: [
                    {name: '安装', value: dataMap.dataPI['2011sum']},
                    {name: '维修', value: dataMap.dataSI['2011sum']}
                  ]
                }
              ]
            }
          ]
        }
      }
    },
    mounted() {
      this.drawLine(this.$data.option);
    },
    created() {

    },
    methods: {
      drawLine(option) {
        // 基于准备好的dom，初始化echarts实例// 绘制图表
        echarts.init(document.getElementById('chart')).setOption(option);
      },
    }
  }
</script>
