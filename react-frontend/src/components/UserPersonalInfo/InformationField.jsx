import {Box, TextField} from '@mui/material'

const InformationField = (props) => {
    const onTextFieldChange = (value) => {
        props.updateCurrentTextField(props.currentProperty, value.target.value)
    }

    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pb: '3ch'}}>
            <TextField label={props.label} value={props.currentValue} onChange={onTextFieldChange}
                       sx={{minWidth: '50ch'}}/>
        </Box>
    )
}

export default InformationField
