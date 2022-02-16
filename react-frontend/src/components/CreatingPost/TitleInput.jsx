import {Box, TextField} from '@mui/material'

const TitleInput = () => {
    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pt: '3ch'}}>
            <TextField label="Название статьи" variant="outlined" color="secondary" sx={{minWidth: '50ch'}}/>
        </Box>
    )
}

export default TitleInput
