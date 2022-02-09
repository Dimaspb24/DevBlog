import UserPersonalInfoContainer from './components/UserPersonalInfo/UserPersonalInfoContainer'
import {Box} from '@mui/material'
import Header from './components/Header/Header'

function App() {
    return (
        <Box sx={{display: 'flex', flexDirection: 'column'}}>
            <Header/>
            <UserPersonalInfoContainer/>
        </Box>
    )
}

export default App
