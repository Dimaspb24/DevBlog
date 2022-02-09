import {Box, IconButton, Tooltip} from '@mui/material'
import NoteAddIcon from '@mui/icons-material/NoteAdd'

const CreateButton = () => {
    return (
        <Box>
            <Tooltip title="Создать статью">
                <IconButton>
                    <NoteAddIcon color='secondary' sx={{fontSize: 40}}/>
                </IconButton>
            </Tooltip>
        </Box>
    )
}

export default CreateButton
