import UserPersonalInfoContainer from './components/UserPersonalInfo/UserPersonalInfoContainer'
import {Box} from '@mui/material'
import Header from './components/Header/Header'
import UserArticleContainer from './components/Article/UserArticleContainer'
import HomePageContainer from './components/Home/HomePageContainer'
import CreatingPostContainer from './components/CreatingPost/CreatingPostContainer'
import {Route} from 'react-router-dom'

function App() {
    return (
        <Box sx={{display: 'flex', flexDirection: 'column'}}>
            <Header/>
            <Route path="/user-personal-info" component={UserPersonalInfoContainer}/>
            <Route path="/article" component={UserArticleContainer}/>
            <Route path="/home" component={HomePageContainer}/>
            <Route path="/creating-post" component={CreatingPostContainer}/>
        </Box>
    )
}

export default App
