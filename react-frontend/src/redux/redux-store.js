import {combineReducers, createStore} from 'redux'
import userPersonalInfoReducer from './user-personal-info-reducer'
import articleReducer from './article-reducer'
import homeReducer from './home-reducer'

const reducers = combineReducers({
    userPersonalInfoReducer: userPersonalInfoReducer,
    articleReducer: articleReducer,
    homeReducer: homeReducer
})

const store = createStore(reducers)

export default store
