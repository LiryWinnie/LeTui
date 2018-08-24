import {SET_CURRENT_USER} from '../constants';
import isEmpty from 'lodash/isEmpty';

const initialState={
    isLogin:false,
    user:{}
}
const auth=(state=initialState,action={})=>{
    switch(action.type){
        case SET_CURRENT_USER:
          return{
              isLogin:!isEmpty(action.user),
              user:action.user
          }
        default:return state;
    }
}

export default auth;
      

