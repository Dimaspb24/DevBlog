import {Avatar, Box, IconButton, Tooltip} from '@mui/material'

const ProfileButton = (props) => {
    return (
        <Box>
            <Tooltip title='Открыть профиль'>
                <IconButton>
                    <Avatar alt='Avatar' src={props.photo}/>
                </IconButton>
            </Tooltip>
        </Box>
    )
}

export default ProfileButton
