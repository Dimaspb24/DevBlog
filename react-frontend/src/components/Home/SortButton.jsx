import {Box, Button} from '@mui/material'

const SortButton = (props) => {
    return (
        <Box sx={{pl: '3ch'}}>
            <Button variant="contained" size="large" color='secondary'>{props.title}</Button>
        </Box>
    )
}

export default SortButton
