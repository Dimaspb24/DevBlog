import {connect} from 'react-redux'
import ProfileButton from './ProfileButton'

const mapStateToProps = (state) => {
    return {
        photo: state.userPersonalInfoReducer.photo
    }
}

const ProfileButtonContainer = connect(mapStateToProps)(ProfileButton)

export default ProfileButtonContainer
