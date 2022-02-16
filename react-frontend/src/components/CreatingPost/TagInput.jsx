import {Box, TextField} from '@mui/material'

const TagInput = () => {
    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pt: '2ch'}}>
            <TextField label="Теги статьи через пробел" variant="outlined" color="secondary" sx={{minWidth: '50ch'}}/>
        </Box>
    )
}

export default TagInput
