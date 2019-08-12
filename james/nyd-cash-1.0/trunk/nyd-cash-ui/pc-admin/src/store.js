import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);
let store = new Vuex.Store({
	state : {
		stateObject : {
			isShowSort : true, //是否展开
			fullscreenLoading : false,//loading
			manageName : "后台管理系统",
			headerTheme : {
				theme : ''
			},
			sidebarTheme : {
				theme : ''
			}
		}
	},
	mutations : {
		setState ( state , source){
			let stateObject = state.stateObject;
			for(let i in source){
				let key = stateObject[ i ];
				if( key ){
          //这样深拷贝没有getter,setter属性
					let newObj = deepClone({} ,state.stateObject[i] , source[i]);
					let o = {};
					o[ i ] = newObj;
          //这样就有了getter,setter属性
					Object.assign(state.stateObject, o);
				}
			}
		}
	}
})


/* ================ 深拷贝 ================ */
function _type( obj , name){
	return Object.prototype.toString.call(obj) === "[object "+name+"]";
}
function isPlainObject(obj) {
	return !!obj && _type(obj,'Object') && _type(obj.isPrototypeOf,'Function');
}
function deepClone() {
    var target = arguments[0] || {}, i = 1, length = arguments.length, deep = false, options, name, src, copy;
	if ( typeof target === "boolean" ) {
		deep = target;
		target = arguments[1] || {};
		i = 2;
	}
	if ( typeof target !== "object" && !_type(target,'Function')) {
		target = {};
	}
	if ( length === i ) {
		target = this;
		--i;
	}
	for ( ; i < length; i++ ) {
		if ( (options = arguments[ i ]) != null ) {
			for ( name in options ) {
				src = target[ name ];
				copy = options[ name ];
				if ( target === copy ) {
					continue;
				}
				if ( deep && copy && ( isPlainObject(copy) || _type(copy,'Array') ) ) {
					var clone =( src && ( isPlainObject(src) || _type(src,'Array') ) ) ? src : (_type(copy,'Array') ? [] : {});
					target[ name ] = deepClone( deep, clone, copy );
				} else if ( copy !== undefined ) {
					target[ name ] = copy;
				}
			}
		}
	}
	return target;
}
/* ================ 深拷贝end ================ */
export default store

/*
外部使用:{{ stateObject.tep1.a && stateObject.tep1.a.author}}
computed: mapState({
	'stateObject' : state => state.stateObject //简写。完整的:this.$store.state.stateObject访问
}),
methods: {
	setText ( msg ){
	  	var obj = {
		    tep1 : {
		      a : {
		        author : msg
		      }
		    }
	  	};
	  	this.$store.commit('stateFn',obj);
	}
}
*/
