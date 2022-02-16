import {Box} from '@mui/material'
import UserArticle from '../ArticleForHomePage/UserArticle'
import SortButton from './SortButton'
import ArticleSearchInput from './ArticleSearchInput'

const HomePage = (props) => {
    const articles = props.articles.map(article => <UserArticle article={article} key={article.id}/>)

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
