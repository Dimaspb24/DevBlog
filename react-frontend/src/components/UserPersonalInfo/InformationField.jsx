import {Box, TextField} from '@mui/material'

const InformationField = (props) => {
    const onTextFieldChange = (value) => {
        props.updateCurrentTextField(props.currentProperty, value.target.value)
    }

    return (
        <Box>
            <TextField label={props.label} value={props.currentValue} onChange={onTextFieldChange}/>
        </Box>
    )
}

export default InformationField
