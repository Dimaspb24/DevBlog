import {AppBar, Box, Toolbar} from '@mui/material'
import Logo from './Logo'
import CreateButton from './CreateButton'
import ProfileButtonContainer from './ProfileButtonContainer'
import UserSubscriptionButton from './UserSubscriptionButton'
import HomeButtonContainer from './HomeButtonContainer'

const Header = () => {
    return (
        <Box>
            <AppBar position="static">
                <Toolbar color="#512da8" sx={{display: 'flex'}}>
                    <Logo/>
                    <UserSubscriptionButton/>
                    <HomeButtonContainer/>
                    <CreateButton/>
                    <ProfileButtonContainer/>
                </Toolbar>
            </AppBar>
        </Box>
    )
}

export default Header
