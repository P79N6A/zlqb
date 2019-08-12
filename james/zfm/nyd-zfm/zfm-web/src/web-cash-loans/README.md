
1 项目文件目录
	1.1 项目目录文件说明
	|── build                            项目构建(webpack)相关代码             
	  ── build.js                      生产环境构建代码
	── check-versions.js           检查node&npm等版本
	── utils.js                        构建配置公用工具
	── webpack.base.conf.js       webpack基础环境配置
	── webpack.dev.conf.js        webpack开发环境配置
	── webpack.prod.conf.js       webpack生产环境配置
	|── config                        项目开发环境配置相关代码
	  ── dev.env.js                   开发环境变量
	  ── index.js                     项目一些配置变量
	  ── prod.env.js                  生产环境变量
	|──node_modules                 项目依赖的模块
	|── src// 源码目录                 
	  ── assets                     资源目录、存放images svg icon 资源文件
	  ── api                        接口api 配置
	── components               项目公共组件
	── pages                     页面目录
	── filters                     公共过滤器组件
	── plugins                    第三方插件目录
	── store                      vuex状态管理
	── stylus                     css样式目录
	  ── router                    前端路由
	── untils                     项目工具类
	  ── main.js                   项目入口文件
	|── static                      静态文件、存放json数据等
	|── test                       测试文件
	|── .babelrc.js                 ES6语法编译配置
	|── .editorconfig.js            定义代码格式
	|── . eslintignore.js            定义代码规范校验配置
	|── .gitignore.js               git上传需要忽略的文件格式
	|── .postcssrc.js               css插件系统工程配置
	|── index.html                 项目入口页面
	|── package.json               项目所有依赖包配置文件
	|── README.md                项目说明

	1.2 项目其他配置规范说明
		1.2.1 此基础框架，配置了elsint语法检测，但未开启。elsint语法检测可以很好地约束前端的代码开发规范，比如对一些空格的缩进、函数、变量使用及其他的约束，但它会阻碍前端的开发效率，建议根据项目的紧急程度来使用。强烈建议一旦开启elsint语法检测后，不要关闭它的使用。eslint配置项参考博客https://blog.csdn.net/hsl0530hsl/article/details/78594973；
		官网地址：
		中文：http://eslint.cn
		英文：http://eslint.org
		关闭eslint方法如下：

		1.2.2 当更改了build文件的配置后，一定要npm run dev重启项目才会生效；
		1.2.3 项目进行跨域时，需要配置代理后，也可以配置浏览器进行代理：如下

		将目标里面的内容替换成："C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" --disable-web-security --user-data-dir=D:\MyChromeDevUserData

2 Stylus文件说明
	样式的使用说明
	1.Stylus主要存放页面css样式
	2.可以使用less、sass、stylus 这里不强制使用哪一种，可根据自己自己熟悉的css预处理器：（注意：这里只配置了less，sass，stylus 暂无配置）；
	3.统一定义字体大小如： medium normal large small mini 类，全局统一定义字体大小（大多数地方）；
	4.图标建议统一使用字体文件或者SVG文件；
	5.在组件里面写样式的时候，强烈建议在style加上scoped，避免全局的样式污染；
	6. 其他要求参照另一份规范文档有详细说明；

3 Router路由文件目录
	3.1 路由使用说明
	 1. rouers文件夹 包含index.js 、routes.js 文件  所有路由都要写在routes.js 里   、所有路由都要有name属性，尽量路由使用懒加载模式
	 2. index.js文件
   
4 api接口文件
	1. api文件夹 包含config.js 、index.js 文件  config.js主要是对开发环境及生产环境接口的定义转换
	2. 所有的接口请求都写在index.js文件中，包括对接口请求拦截器、接口请求参数的序列化、配置好接口的请求规范才能更换好的跟后端进行交互联调

5 Vuex状态管理
	5.1 vuex状态管理的说明
		1. store文件夹 包含actions.js 、index.js 、getters.js、mutations.js、types.js 等主要文件 
		2. actions.js 提交的是 mutation，包含任意异步操作
		3. getters.js store数据中心的计算属性
		4 type.js 这个js文件里面只是一些变量，把action和mutation文件里面相同变量名的链接起来
		5 index.js vuex的配置入口文件
		6 mutation.js  里面装着一些改变数据方法的集合，把处理数据逻辑方法全部放在mutations里面，使得数据和视图分离，Vuex中store数据改变的唯一方法就是mutation

6 项目的启动
	6.1 项目启动
		开发模式 npm run dev
		生成模式 npm run start
		项目打包 npm run build
	6.2 项目打包
		当开发项目完成时，此处需要注意前端打包的路径，这个路径要根据后台需要来路径。（注意：打包路径一般是在webapp/static下面，一定要根据后端来放打包的路径）
7 项目开发过程及结束后，遇到的问题及总结，要记录下来：
  (此处写项目问题及总结)

