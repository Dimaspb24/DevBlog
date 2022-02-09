import {combineReducers, createStore} from 'redux'
import userPersonalInfoReducer from './user-personal-info-reducer'
import articleReducer from './article-reducer'

const reducers = combineReducers({
    userPersonalInfoReducer: userPersonalInfoReducer,
    articleReducer: articleReducer
})

const store = createStore(reducers)

export default store
