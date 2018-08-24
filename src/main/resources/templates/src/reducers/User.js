import {USER_FETCHED} from '../constants';

const user=(state={},action={})=>{
    switch(action.type){
    	case USER_FETCHED:
    	   return action.user;
        default:return state;
    }
};

export default user;