import {Box, IconButton, Tooltip} from '@mui/material'
import HomeIcon from '@mui/icons-material/Home'
import {NavLink} from 'react-router-dom'

const HomeButton = () => {
    return (
        <Box>
            <NavLink to='/home'>
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
