const UPDATE_PASSED_PROPERTY = 'UPDATE-PASSED-PROPERTY'

const testPersonalInfoInitialState = {
    firstname: 'firstname',
    lastname: 'lastname',
    nickname: 'nickname',
    photo: 'string',
    info: 'info',
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
