import {Box, IconButton, TextField, Tooltip} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search';

const ArticleSearchInput = () => {
    return (
        <Box sx={{pl: '2ch'}}>
            <TextField label="Поиск статей" variant="outlined"/>
            <Tooltip title="Найти статьи">
                <IconButton>
                    <SearchIcon color="secondary" sx={{fontSize: 40}}/>
                </IconButton>
            </Tooltip>
        </Box>
    )
}

export default ArticleSearchInput
