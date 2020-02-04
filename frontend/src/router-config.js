import list from './components/List.vue'

export default [{
        path: '/',
        redirect: '/list'
    },
    {
        path: '/list',
        component: list,
    }
]