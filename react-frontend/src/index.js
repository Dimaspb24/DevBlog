import React from 'react'
import ReactDOM from 'react-dom'
import './index.css'
import App from './App'
import reportWebVitals from './reportWebVitals'
import {Provider} from 'react-redux'
import reduxStore from './redux/redux-store'

ReactDOM.render(
    <React.StrictMode>
        <Provider store={reduxStore}>
            <App/>
        </Provider>
    </React.StrictMode>,
    document.getElementById('root')
)

reportWebVitals()
