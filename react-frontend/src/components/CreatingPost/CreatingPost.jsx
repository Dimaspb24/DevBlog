import {Box} from '@mui/material'
import TitleInput from './TitleInput'
import TagInput from './TagInput'
import BodyInput from './BodyInput'
import SendButton from './SendButton'
import Text from './Text'

const CreatingPost = () => {
    return (
        <Box sx={{display: 'flex', flexDirection: 'column'}}>
            <Text/>
            <TitleInput/>
            <TagInput/>
            <BodyInput/>
            <SendButton/>
        </Box>
    )
}

export default CreatingPost
