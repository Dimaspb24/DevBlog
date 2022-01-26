import {combineReducers, createStore} from 'redux'
import userPersonalInfoReducer from './user-personal-info-reducer'

const reducers = combineReducers({
    userPersonalInfoReducer: userPersonalInfoReducer
})

const store = createStore(reducers)

export default store
