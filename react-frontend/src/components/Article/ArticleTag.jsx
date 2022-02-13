import {Box, Chip} from '@mui/material'

const ArticleTag = (props) => {
    return (
        <Box sx={{pr: '2ch'}}>
            <Chip label={props.name} color='secondary'/>
        </Box>
    )
}

export default ArticleTag
