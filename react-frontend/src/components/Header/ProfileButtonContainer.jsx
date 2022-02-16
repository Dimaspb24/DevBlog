import {connect} from 'react-redux'
import ProfileButton from './ProfileButton'
import {updatePassedPropertyActionCreator} from '../../redux/user-personal-info-reducer'

const mapStateToProps = (state) => {
    return {
        photo: state.userPersonalInfoReducer.photo
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        updateCurrentTextField: (currentProperty, updatedValue) => {
            const action = updatePassedPropertyActionCreator(currentProperty, updatedValue)
            dispatch(action)
        }
    }
}

const ProfileButtonContainer = connect(mapStateToProps, mapDispatchToProps)(ProfileButton)

export default ProfileButtonContainer
