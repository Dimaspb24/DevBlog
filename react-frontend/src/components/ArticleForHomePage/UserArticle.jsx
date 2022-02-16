import {Box, Paper} from '@mui/material'
import UserAvatar from './UserAvatar'
import Nickname from './Nickname'
import Date from './Date'
import ArticleTitle from './ArticleTitle'
import ArticleTag from './ArticleTag'
import Body from './Body'

const UserArticle = (props) => {

    const tagElements = props.article.tags.map(tag => <ArticleTag id={tag.id} name={tag.name} key={tag.id}/>)
    const tagWrapper = <Box sx={{display: 'flex'}}>{tagElements}</Box>

    return (
        <Box sx={{display: 'flex', justifyContent: 'center', pt: '5ch', pb: '10ch'}}>
            <Paper elevation={10} sx={{backgroundColor: '#fff7ff', maxWidth: 800, px: '20ch'}}>
                <UserAvatar photo={props.article.photo}/>
                <Nickname nickname={props.article.nickname}/>
                <Date publicationDate={props.article.publicationDate}/>
                <ArticleTitle title={props.article.title}/>
                {tagWrapper}
                <Body body={props.article.body}/>
            </Paper>
        </Box>
    )
}

export default UserArticle
