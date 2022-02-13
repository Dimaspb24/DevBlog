import {Box} from '@mui/material'
import ReactMarkdown from 'react-markdown'

const Body = (props) => {
    return (
        <Box sx={{pt: '1ch', pb: '15ch'}}>
            <ReactMarkdown>{props.body}</ReactMarkdown>
        </Box>
    )
}

export default Body
