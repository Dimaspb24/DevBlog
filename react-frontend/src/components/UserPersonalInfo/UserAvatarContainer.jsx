import {connect} from 'react-redux'
import UserAvatar from './UserAvatar'
import {updatePassedPropertyActionCreator} from '../../redux/user-personal-info-reducer'

const mapStateToProps = (state) => {
    return {
        photo: state.userPersonalInfoReducer.photo
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        updateAvatar: (avatarUrl) => {
            const action = updatePassedPropertyActionCreator('photo', avatarUrl)
            dispatch(action)
        }
    }
}

const UserAvatarContainer = connect(mapStateToProps, mapDispatchToProps)(UserAvatar)

export default UserAvatarContainer
