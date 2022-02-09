import {Box, IconButton, Tooltip} from '@mui/material'
import HomeIcon from '@mui/icons-material/Home'

const HomeButton = () => {
    return (
        <Box>
            <Tooltip title="Открыть домашнюю страницу">
                <IconButton>
                    <HomeIcon color='secondary' sx={{fontSize: 40}}/>
                </IconButton>
            </Tooltip>
        </Box>
    )
}

export default HomeButton
