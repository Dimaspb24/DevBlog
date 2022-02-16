import {updatePassedPropertyActionCreator} from '../../redux/article-reducer'
import {connect} from 'react-redux'
import CreatingPost from './CreatingPost'

const mapStateToProps = (state) => {
    return {
        article: state.articleReducer
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        updatePassedProperty: (currentProperty, updatedValue) => {
            const action = updatePassedPropertyActionCreator(currentProperty, updatedValue)
            dispatch.action(action)
        }
    }
}

const CreatingPostContainer = connect(mapStateToProps, mapDispatchToProps)(CreatingPost)

export default CreatingPostContainer
