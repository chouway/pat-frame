import {defineStore} from "pinia"
import {ElMessage} from "element-plus";
import axios from 'axios'
//https://blog.csdn.net/u014678583/article/details/123812188?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_paycolumn_v3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1.pc_relevant_paycolumn_v3&utm_relevant_index=2
//vue3 pinia的安装和使用
export const userStore = defineStore({
    id: "info", // id是唯一的，如果有多个文件，ID不能重复
    state: () => {
        return {
            userinfo: null,//(token 12小时有效  refresh token 30天有效)
            exp: -1 //token失效时间点  单位毫秒
        }
    },
    actions: {
        check(){
          if(new Date().getTime()<this.exp){
              return;
          }
          this.refresh();

        },
        setInfo(data) {
            this.userinfo = data
            this.exp = new Date().getTime()+1000*(data.expires_in) - 60*1000;
        },
        setCode(code) {
            getToken(code);
        },
        fresh(){//刷新token
            refresh();
        },
        logout(){
            logout();
        },
        // 用户退出，清除本地数据
        logoutLocal() {
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

/**
 * code换取token
 * @param code
 */
function getToken(code){
    if (!code) {
        return;
    }
    const us = userStore();
    axios.post("/api/client/token", "code=" + code)
        .then(
            (res) => {
                console.info("token:" + res);
                if (res.data.success) {
                    if (res.data.info.error) {
                        ElMessage.warning(res.data.info.error);
                    } else {
                        us.setInfo(res.data.info);
                    }
                    window.location.href = "/";

                } else {
                    ElMessage.warning(res.data.message);
                }
            }
        ).catch(
        (err) => {
            ElMessage.error("server error:" + err);
        }
    )
}

/**
 * 刷新token
 */
function refresh(){
    const us = userStore();
    if(!us.userinfo){
        return;
    }
    if(new Date().getTime()< us.exp){
        return;
    }
    if(!us.userinfo.refresh_token){
        return;
    }
    axios.post("/api/client/refreshToken", "refreshToken=" + this.userinfo.refresh_token)
        .then(
            (res) => {
                console.info("refreshToken:" + res);
                if (res.data.success) {
                    if (res.data.info.error) {
                        this.logout();
                        ElMessage.warning(res.data.info.error);
                    } else {
                        us.setInfo(res.data.info);
                    }
                    window.location.href = "/";
                } else {
                    ElMessage.warning(res.data.message);
                }
            }
        ).catch(
        (err) => {
            ElMessage.error("server error:" + err);
        }
    )
}

/**
 * 退出登录
 */
function logout(){
    const us = userStore();
    if(!us.userinfo){
        return;
    }
    if(new Date().getTime()< us.exp){
        return;
    }
    if(!us.userinfo.refresh_token){
        return;
    }
    axios.delete("/oauth/logoutByToken", "token=" + this.userinfo.access_token)
        .then(
            (res) => {
                console.info("logout:" + res);
                if (res.data.success) {
                    if (res.data.info.error) {
                        ElMessage.warning(res.data.info.error);
                    } else {
                        us.logoutLocal();
                    }
                    window.location.href = "/";
                } else {
                    ElMessage.warning(res.data.message);
                }
            }
        ).catch(
        (err) => {
            ElMessage.error("server error:" + err);
        }
    )
}