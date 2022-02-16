import {Box, Button} from '@mui/material'

const SendButton = () => {
    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pt: '2ch', pb: '15ch'}}>
            <Button variant="contained" size="large" color="secondary">Опубликовать статью</Button>
        </Box>
    )
}

export default SendButton
