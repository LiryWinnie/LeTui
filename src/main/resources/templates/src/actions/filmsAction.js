import {SET_POPFILMS,FILM_FETCHED,FILM_SEARCHED} from '../constants';
import axios from 'axios';

export const setPopfilms=(popfilms)=>{
    return {
        type:SET_POPFILMS,
        popfilms
    }
};
export const filmFetched=(film)=>{
    return{
        type:FILM_FETCHED,
        film
    }
}

 export const fetchPopfilms=()=>{
     return dispatch=>{
         fetch('/movie/hot')
           .then(res => res.json())
           .then(res=>dispatch(setPopfilms(res)))
//           .then(res=>console.log("dhf",res))
     }
 };         

export const fetchFilm=(did)=>{
    return dispatch=>{
        fetch(`/movie/searchID/${did}`)
          .then(res => res.json())
//          .then(res=>console.log("sd",res))
          .then(res=>dispatch(filmFetched(res)))
    }
};


export const filmSearched=(filmList)=>{
    return{
        type:FILM_SEARCHED,
        filmList
    }
}
//export const searchFilm=(text)=>{
//    return dispatch=>{
//        fetch(`/movie/search/${text}`)
//       .then(res=>res.json())
//    //    .then(res=>console.log("dhfd",res))
//       .then(data=>dispatch(filmSearched(data.film)))
//       
//    }
//}

export const searchFilm=(text)=>{
	return dispatch=>{
		return axios.post(`/movie/search/${text}`)
		       .then(res=>{
                console.log("jsjg",res);
                dispatch(filmSearched(res.data));}
                )
	}
}

export const ratingFilm=(did,userId,score)=>{
    return dispatch=>{
        return axios.post(`/movie/score/${did}/${userId}/${score}`)
         .then(res=>console.log("sdh",res))
    }
}

export const fetchUserScore=(did,userId)=>{
    return dispatch=>{
        return axios.get(`/movie/score/${did}/${userId}`)
         .then(res=>{
            return(res);
        })
    }
}