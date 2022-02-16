import {updateStateActionCreator} from '../../redux/home-reducer'
import {connect} from 'react-redux'
import HomePage from './HomePage'

const mapStateToProps = (state) => {
    return {
        articles: state.homeReducer
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        updateState: newState => {
            const action = updateStateActionCreator(newState)
            dispatch(action)
        }
    }
}

export const HomePageContainer = connect(mapStateToProps, mapDispatchToProps)(HomePage)

export default HomePageContainer
