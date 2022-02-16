import {Box, Typography} from '@mui/material'
import {NavLink} from 'react-router-dom'

const ArticleTitle = (props) => {
    return (
        <Box sx={{pt: '3ch', pb: '2ch'}}>
            <NavLink to='/article' style={{textDecoration: 'none'}}>
                <Typography sx={{fontSize: '4ch', fontWeight: 600, color: '#512da8'}}>
                    {props.title}
                </Typography>
            </NavLink>
        </Box>
    )
}

export default ArticleTitle
