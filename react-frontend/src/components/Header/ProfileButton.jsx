import {Avatar, Box, IconButton, Tooltip} from '@mui/material'
import {NavLink} from 'react-router-dom'
import jQuery from 'jquery'

const ProfileButton = (props) => {
    const onClick = () => {
        jQuery.ajax(`http://localhost:8080/v1/users/${props.loginUser.id}`, {
            method: 'GET',
            xhrFields: {
                withCredentials: true
            },
            headers: {
                Authorization: props.loginUser.token
            }
        }).done(ans => {
            props.updateCurrentTextField('firstname', ans.personalInfo.firstname)
            props.updateCurrentTextField('lastname', ans.personalInfo.lastname)
            props.updateCurrentTextField('nickname', ans.personalInfo.nickname)
            props.updateCurrentTextField('photo', ans.personalInfo.photo)
            props.updateCurrentTextField('info', ans.personalInfo.info)
            props.updateCurrentTextField('phone', ans.personalInfo.phone)
        }, [])
    }

    return (
        <Box>
            <NavLink to="/user-personal-info" onClick={onClick}>
                <Tooltip title="Открыть профиль">
                    <IconButton>
                        <Avatar alt="Avatar" src={props.photo}/>
                    </IconButton>
                </Tooltip>
            </NavLink>
        </Box>
    )
}

export default ProfileButton
