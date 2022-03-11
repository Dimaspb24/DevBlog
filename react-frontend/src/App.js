import UserPersonalInfoContainer from './components/UserPersonalInfo/UserPersonalInfoContainer'
import {Box} from '@mui/material'
import Header from './components/Header/Header'
import UserArticleContainer from './components/Article/UserArticleContainer'
import HomePageContainer from './components/Home/HomePageContainer'
import CreatingPostContainer from './components/CreatingPost/CreatingPostContainer'
import {Route} from 'react-router-dom'
import Login from './components/Login/Login'
import LoginContainer from './components/Login/LoginContainer'

function App() {
    return (
        <Box sx={{display: 'flex', flexDirection: 'column'}}>
            <Route path="/login">
                <LoginContainer/>
            </Route>
            <Route path="/user-personal-info">
                <Header/>
                <UserPersonalInfoContainer/>
            </Route>
            <Route path="/article">
                <Header/>
                <UserArticleContainer/>
            </Route>
            <Route path="/home" component={HomePageContainer}>
                <Header/>
                <HomePageContainer/>
            </Route>
            <Route path="/creating-post">
                <Header/>
                <CreatingPostContainer/>
            </Route>
        </Box>
    )
}

export default App
