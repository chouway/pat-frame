import {createStore} from 'vuex'

// 创建一个新的 store 实例
const store = createStore({
    state() {
        return {
            state: {
                metaInfo: {
                    title: '百诗',
                    keywords: '百诗，baish，中华古典文集，搜索',
                    description: '百诗搜索，baish search，中华古典文集，经典咏流传'
                }
            },
            mutation:{
                CHANGE_META_INFO(state,metaInfo){
                    state.metaInfo = metaInfo;
                }
            }
        }
    }
});

export default store;
