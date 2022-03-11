import {Box, Typography} from '@mui/material'
import {NavLink} from 'react-router-dom'
import jQuery from 'jquery'

const ArticleTitle = (props) => {

    const onClick = () => {
        jQuery.ajax(`http://localhost:8080/v1/article/${props.article.id}`, {
            method: 'GET',
            xhrFields: {
                withCredentials: true
            },
            headers: {
                Authorization: props.loginUser.token
            },
            data: {
                page: 0,
                size: 100,
                sort: []
            }
        }).done(ans => {
            props.updateState(ans.content)
        })
    }

    return (
        <Box sx={{pt: '3ch', pb: '2ch'}}>
            <NavLink to='/article' style={{textDecoration: 'none'}} onClick={onClick}>
                <Typography sx={{fontSize: '4ch', fontWeight: 600, color: '#512da8'}}>
                    {props.article.title}
                </Typography>
            </NavLink>
        </Box>
    )
}

export default ArticleTitle
