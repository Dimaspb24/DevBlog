import {Box, Typography} from '@mui/material'

const Body = (props) => {
    return (
        <Box sx={{pt: '1ch', pb: '15ch'}}>
            <Typography>{props.body}</Typography>
        </Box>
    )
}

export default Body
