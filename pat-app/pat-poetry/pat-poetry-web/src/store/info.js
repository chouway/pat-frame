import { defineStore } from "pinia"
//https://blog.csdn.net/u014678583/article/details/123812188?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_paycolumn_v3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_paycolumn_v3&utm_relevant_index=2
//vue3 pinia的安装和使用
export const userStore = defineStore({
    id: "info", // id是唯一的，如果有多个文件，ID不能重复
    state: () => {
        return {
            userinfo: null
        }
    },
    actions: {
        setInfo(data) {
            this.userinfo = data
        },
        // 用户退出，清除本地数据
        logout() {
            this.userinfo = null
            sessionStorage.clear()
            localStorage.clear()
        },
    },
    // 开启数据缓存，在 strategies 里自定义 key 值，并将存放位置由 sessionStorage 改为 localStorage
    // 默认所有 state 都会进行缓存，你可以通过 paths 指定要持久化的字段，其他的则不会进行持久化，如：paths: ['userinfo'] 替换key的位置
    persist: {
        enabled: true,
        strategies: [
            {
                key: "poetry",
                storage: localStorage,
            },
        ],
    },
})