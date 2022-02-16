import {Avatar, Box, IconButton, Tooltip} from '@mui/material'
import {NavLink} from 'react-router-dom'

const ProfileButton = (props) => {
    return (
        <Box>
            <NavLink to="/user-personal-info">
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
