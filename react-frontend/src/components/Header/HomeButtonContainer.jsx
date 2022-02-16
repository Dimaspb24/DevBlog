import {updateStateActionCreator} from '../../redux/home-reducer'
import {connect} from 'react-redux'
import HomeButton from './HomeButton'

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

export const HomeButtonContainer = connect(mapStateToProps, mapDispatchToProps)(HomeButton)

export default HomeButtonContainer
