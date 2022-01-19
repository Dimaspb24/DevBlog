import {Avatar, Box} from '@mui/material'

const UserAvatar = (props) => {
    return (
        <Box>
            <Avatar alt="Avatar" src={props.photo} sx={{width: '8ch', height: '8ch'}}/>
        </Box>
    )
}

export default UserAvatar
