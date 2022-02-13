import UserPersonalInfoContainer from './components/UserPersonalInfo/UserPersonalInfoContainer'
import {Box} from '@mui/material'
import Header from './components/Header/Header'
import UserArticleContainer from './components/Article/UserArticleContainer'

function App() {
    return (
        <Box sx={{display: 'flex', flexDirection: 'column'}}>
            <Header/>
            {/*<UserPersonalInfoContainer/>*/}
            <UserArticleContainer/>
        </Box>
    )
}

export default App
