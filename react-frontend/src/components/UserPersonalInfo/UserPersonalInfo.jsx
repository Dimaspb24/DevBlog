import {Box} from '@mui/material'
import Text from './Text'
import SaveButton from './SaveButton'
import UserAvatarContainer from './UserAvatarContainer'
import InformationField from './InformationField'

const UserPersonalInfo = (props) => {
    return (
        <Box sx={{
            display: 'flex', flexDirection: 'column', maxWidth: 600, border: '3px solid #512da8',
            borderRadius: 3
        }}>
            <Text/>
            <UserAvatarContainer/>

            <InformationField label="Firstname" currentProperty="firstname" currentValue={props.personalInfo.firstname}
                              updateCurrentTextField={props.updateCurrentTextField}/>
            <InformationField label="Lastname" currentProperty="lastname" currentValue={props.personalInfo.lastname}
                              updateCurrentTextField={props.updateCurrentTextField}/>
            <InformationField label="Nickname" currentProperty="nickname" currentValue={props.personalInfo.nickname}
                              updateCurrentTextField={props.updateCurrentTextField}/>
            <InformationField label="Information" currentProperty="info" currentValue={props.personalInfo.info}
                              updateCurrentTextField={props.updateCurrentTextField}/>
            <InformationField label="Phone number" currentProperty="phone" currentValue={props.personalInfo.phone}
                              updateCurrentTextField={props.updateCurrentTextField}/>

            <SaveButton/>
        </Box>
    )
}

export default UserPersonalInfo
