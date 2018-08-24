import axios from 'axios';
import setAuthorizationToken from '../config/setAuthorizationToken';
import jwtDecode from 'jwt-decode';
import {SET_CURRENT_USER,USER_FETCHED} from '../constants';

export const userRegisterRequest=(userData)=>{
    return dispatch=>{
        return axios.post('/movie/user/add',userData)
          // .then(res=>console.log(res.data))
    }
}

export const setCurrentUser =(user)=>{
    return{
        type:SET_CURRENT_USER,
        user
    }
}
export const userLoginRequest=(userData)=>{
    return dispatch=>{
        return axios.post('/movie/user/login',userData)
        .then(res=>{
            // console.log("sdh",res.data);
            const user=res.data.data;
            localStorage.setItem("user",user);
            setAuthorizationToken(user);
            dispatch(setCurrentUser(user));
            return(res);
         })
    }
}

export const userLogoutRequest=()=>{
    return dispatch=>{
        localStorage.removeItem("user");
        setAuthorizationToken(false);
        dispatch(setCurrentUser({}))
    }
}

export const userFetched=(user)=>{
    return{
        type:USER_FETCHED,
        user
    }
}
export const fetchUser=(id)=>{
    return dispatch=>{
        axios.get(`/movie/user/${id}`)
        .then(res=>{
            console.log(res.data);
            dispatch(userFetched(res.data));
            return (res);
        })
    }
}

export const updateUser=(id,userData)=>{
    return dispatch=>{
        axios.post(`/movie/user/update/${id}`,userData)
        // .then(res=>console.log(res))
    }
}

export const userPasswordChangeRequest=(id,passwordData)=>{
    return dispatch=>
        axios.post(`/movie/user/update/password/${id}`,passwordData)
        .then(res=>{
            return(res);
        })
    
}

export const userRecomRequest=(id)=>{
    return dispatch=>
        axios.get(`/movie/recommend/${id}`)
          .then(res=>{
            return(res);
        })
    
}


// 