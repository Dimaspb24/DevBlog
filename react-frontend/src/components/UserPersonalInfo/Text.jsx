import {Box, Typography} from '@mui/material'

const Text = () => {
    return (
        <Box sx={{textAlign: 'center', py: '3ch'}}>
            <Typography sx={{fontSize: '3ch', fontWeight: 600, color: '#512da8'}}>Персональная информация</Typography>
        </Box>
    )
}

export default Text
