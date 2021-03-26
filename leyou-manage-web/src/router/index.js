import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

function route (path, file, name, children) {
  return {
    exact: true,
    path,
    name,
    children,
    component: () => import('../pages' + file)
}
}

export default new Router({
  routes: [
    route("/login",'/Login',"Login"),// 登录路由
    {
      path:"/", // 根路由
      component: () => import('../pages/Layout'),
      redirect:"/index/dashboard",
      children:[
        route("/index/dashboard","/Dashboard","Dashboard"),
        route("/item/test","/item/test","Test"),
        route("/item/brand","/item/Brand","Brand"),
        route("/item/list",'/item/Goods',"Goods"),
        route("/item/category","/item/Category","Category"),
        route("/item/specification",'/item/specification/Specification',"Specification"),
        route("/user/statistics",'/item/Statistics',"Statistics"),
        route("/trade/promotion",'/trade/Promotion',"Promotion"),
]



}
]
})
