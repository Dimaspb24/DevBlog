import {Box, Typography} from '@mui/material'

const ArticleTitle = (props) => {
    return (
        <Box sx={{pt: '3ch', pb: '2ch'}}>
            <Typography sx={{fontSize: '4ch', fontWeight: 600, color: '#512da8'}}>{props.title}</Typography>
        </Box>
    )
}

export default ArticleTitle
