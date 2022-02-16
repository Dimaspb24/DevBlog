import {Box, IconButton, Tooltip} from '@mui/material'
import NoteAddIcon from '@mui/icons-material/NoteAdd'
import {NavLink} from 'react-router-dom'

const CreateButton = () => {
    return (
        <Box>
            <NavLink to='/creating-post'>
                <Tooltip title="Создать статью">
                    <IconButton>
                        <NoteAddIcon color="secondary" sx={{fontSize: 40}}/>
                    </IconButton>
                </Tooltip>
            </NavLink>
        </Box>
    )
}

export default CreateButton
