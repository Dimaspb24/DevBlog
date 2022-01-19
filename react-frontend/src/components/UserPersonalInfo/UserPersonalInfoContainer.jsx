import {connect} from 'react-redux'
import UserPersonalInfo from './UserPersonalInfo'
import {updatePassedPropertyActionCreator} from '../../redux/user-personal-info-reducer'

const mapStateToProps = (state) => {
    return {
        personalInfo: state.userPersonalInfoReducer
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

const UserPersonalInfoContainer = connect(mapStateToProps, mapDispatchToProps)(UserPersonalInfo)

export default UserPersonalInfoContainer
