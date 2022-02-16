import {Box, TextField} from '@mui/material'

const BodyInput = () => {
    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pt: '2ch'}}>
            <TextField label="Содержание статьи" variant="outlined" color="secondary" sx={{minWidth: '100ch'}}
                       multiline rows={15}/>
        </Box>
    )
}

export default BodyInput
