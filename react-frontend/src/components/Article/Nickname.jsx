import {Box, Typography} from '@mui/material'

const Nickname = (props) => {
    return (
        <Box>
            <Typography sx={{fontSize: '3ch', fontWeight: 600, color: '#512da8'}}>
                Автор: {props.nickname}
            </Typography>
        </Box>
    )
}

export default Nickname
