import {Avatar, Box} from '@mui/material'

const UserAvatar = (props) => {
    return (
        <Box sx={{pt: '5ch'}}>
            <Avatar alt="Avatar" src={props.photo} sx={{width: '10ch', height: '10ch'}}/>
        </Box>
    )
}

export default UserAvatar