const initState = {

}

const patientReducer = (state = initState, action) => {

    switch (action.type) {
        case 'CREATE_PATIENT':
        console.log('created patient', action.patient);
        return state;

        case 'CREATE_PATIENT_ERROR':
            console.log('create patient error', action.err);
            return state;

        case 'UPDATE_PATIENT':
            console.log('updated patient', action.patient);
            return state;

        case 'UPDATE_PATIENT_ERROR':
            console.log('update patient error', action.err);
            return state;
        
        default:
            return state;
    }


}


export default patientReducer ///()?
