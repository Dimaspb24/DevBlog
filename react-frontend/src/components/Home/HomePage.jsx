import {Box} from '@mui/material'
import UserArticle from '../Article/UserArticle'

const HomePage = (props) => {
    const articles = props.articles.map(article => <UserArticle article={article} key={article.id}/>)

    return (
        <Box>
            {articles}
        </Box>
    )
}

export default HomePage
