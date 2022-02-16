import {Box, Typography} from '@mui/material'

const Date = (props) => {
    return (
        <Box>
            <Typography sx={{fontSize: '2ch', fontWeight: 600, color: '#512da8'}}>
                Опубликовано: {props.publicationDate}
            </Typography>
        </Box>
    )
}

export default Date
