const UPDATE_PASSED_PROPERTY = 'UPDATE_PASSED_PROPERTY'

const articleInitialState = {
    id: 42,
    title: 'Петли маршрутизации',
    body: 'Как вы, возможно, знаете, цикл маршрутизации - это ситуация, когда некоторый пакет маршрутизируется' +
        'бесконечно или почти бесконечно в цикле. И такая ситуация может возникнуть при конвергенции протоколов' +
        'динамической маршрутизации.',
    status: 'PUBLISHED',
    articleDescription: 'Петли маршрутизации',
    publicationDate: '01:01:2022',
    modificationDate: '01:01:2022',
    authorId: 1,
    nickname: 'Leha-Chel',
    photo: 'https://sun9-32.userapi.com/impf/c830209/v830209268/120d18/lqU5_fb3DKo.jpg?size=1080x810&quality=' +
        '96&sign=af1665c70b3ecfbc34e610daaac4f67e&type=album',
    tags: [
        {
            id: 23,
            name: 'Наука'
        },
        {
            id: 38,
            name: 'Образование'
        }
    ]
}

const articleReducer = (state = articleInitialState, action) => {
    switch (action.type) {
        case UPDATE_PASSED_PROPERTY: {
            return {
                ...state,
                [action.currentProperty]: action.updatedValue
            }
        }
        default:
            return state
    }
}

export const updatePassedPropertyActionCreator = (currentProperty, updatedValue) => ({
    type: UPDATE_PASSED_PROPERTY,
    currentProperty: currentProperty,
    updatedValue: updatedValue
})

export default articleReducer
