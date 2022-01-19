const UPDATE_PASSED_PROPERTY = 'UPDATE-PASSED-PROPERTY'

const testPersonalInfoInitialState = {
    firstname: 'Firstname',
    lastname: 'Lastname',
    nickname: 'nickname',
    photo: 'https://sun9-32.userapi.com/impf/c830209/v830209268/120d18/lqU5_fb3DKo.jpg?size=1080x810&quality=96&sign=af1665c70b3ecfbc34e610daaac4f67e&type=album',
    info: 'some info...',
    phone: '89841235488'
}

const userPersonalInfoReducer = (state = testPersonalInfoInitialState, action) => {
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

export default userPersonalInfoReducer
