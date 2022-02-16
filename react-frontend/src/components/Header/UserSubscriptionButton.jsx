import {Box, IconButton, Tooltip} from '@mui/material'
import SubscriptionsIcon from '@mui/icons-material/Subscriptions'

const UserSubscriptionButton = () => {
    return (
        <Box>
            <Tooltip title="Открыть статьи ваших подписок">
                <IconButton>
                    <SubscriptionsIcon color="secondary" sx={{fontSize: 40}}/>
                </IconButton>
            </Tooltip>
        </Box>
    )
}

export default UserSubscriptionButton
