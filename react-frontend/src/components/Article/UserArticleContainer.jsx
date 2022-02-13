import {updatePassedPropertyActionCreator} from '../../redux/article-reducer'
import {connect} from 'react-redux'
import UserArticle from './UserArticle'

const mapStateToProps = (state) => {
    return {
        article: state.articleReducer
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        updateCurrentProperty: (currentProperty, updatedValue) => {
            const action = updatePassedPropertyActionCreator(currentProperty, updatedValue)
            dispatch(action)
        }
    }
}

const UserArticleContainer = connect(mapStateToProps, mapDispatchToProps)(UserArticle)

export default UserArticleContainer
