import {Box} from '@mui/material'
import SortButton from './SortButton'
import ArticleSearchInput from './ArticleSearchInput'
import UserArticleContainer from '../ArticleForHomePage/UserArticleContainer'
import UserArticle from '../ArticleForHomePage/UserArticle'

const HomePage = (props) => {
    console.log(props.articles)

    const articles = props.articles.map(article => <UserArticle article={article} loginUser={props.loginUser}
                                                                updateState={props.updateArticleState}
                                                                key={article.id}/>)

    return (
        <Box>
            <Box sx={{display: 'flex', py: '2ch'}}>
                <SortButton title="По убыванию даты"/>
                <SortButton title="По возрастанию даты"/>
                <SortButton title="По убыванию рейтинга"/>
                <SortButton title="По возрастанию рейтинга"/>
                <ArticleSearchInput/>
            </Box>
            {articles}
        </Box>
    )
}

export default HomePage
