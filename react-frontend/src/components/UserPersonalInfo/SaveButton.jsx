import {Box, Button} from '@mui/material'

const SaveButton = () => {
    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pb: '5ch'}}>
            <Button variant="contained" color='secondary' size='large' sx={{minWidth: '25ch'}}>Сохранить</Button>
        </Box>
    )
}

export default SaveButton
