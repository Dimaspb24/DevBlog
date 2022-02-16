import {Box, Typography} from '@mui/material'

const Text = () => {
    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pt: '3ch'}}>
            <Typography sx={{fontSize: '3ch', fontWeight: 600, color: '#512da8'}}>Создание новой статьи</Typography>
        </Box>
    )
}

export default Text
