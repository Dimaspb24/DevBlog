import {Box, IconButton, Tooltip} from '@mui/material'
import HomeIcon from '@mui/icons-material/Home'
import {NavLink} from 'react-router-dom'
import jQuery from 'jquery'

const HomeButton = (props) => {
    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWlsMUBtYWlsLnJ1Iiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2NDUwMDU4NDksImV4cCI6MTY0NTAyMDI0OX0.dCJqG6L79Q6oxsDvP-Jf_kzdrZP3ISE_fCdw6LTgFvM'

    const onClick = () => {
        jQuery.ajax('http://localhost:8080/v1/articles', {
            method: 'GET',
            xhrFields: {
                withCredentials: true
            },
            headers: {
                Authorization: `Bearer ${token}`
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
        <Box>
            <NavLink to="/home" onClick={onClick}>
                <Tooltip title="Открыть домашнюю страницу">
                    <IconButton>
                        <HomeIcon color="secondary" sx={{fontSize: 40}}/>
                    </IconButton>
                </Tooltip>
            </NavLink>
        </Box>
    )
}

export default HomeButton
