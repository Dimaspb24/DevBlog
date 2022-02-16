import {AppBar, Box, Toolbar} from '@mui/material'
import Logo from './Logo'
import HomeButton from './HomeButton'
import CreateButton from './CreateButton'
import ProfileButtonContainer from './ProfileButtonContainer'
import UserSubscriptionButton from './UserSubscriptionButton'

const Header = () => {
    return (
        <Box>
            <AppBar position="static">
                <Toolbar color="#512da8" sx={{display: 'flex'}}>
                    <Logo/>
                    <UserSubscriptionButton/>
                    <HomeButton/>
                    <CreateButton/>
                    <ProfileButtonContainer/>
                </Toolbar>
            </AppBar>
        </Box>
    )
}

export default Header
