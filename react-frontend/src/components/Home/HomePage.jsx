import {Box} from '@mui/material'
import UserArticle from '../Article/UserArticle'
import SortButton from './SortButton'

const HomePage = (props) => {
    const articles = props.articles.map(article => <UserArticle article={article} key={article.id}/>)

    return (
        <Box>
            <Box sx={{display: 'flex', py: '2ch'}}>
                <SortButton title="По убыванию даты"/>
                <SortButton title="По возрастанию даты"/>
                <SortButton title="По убыванию рейтинга"/>
                <SortButton title="По возрастанию рейтинга"/>
            </Box>
            {articles}
        </Box>
    )
}

export default HomePage
