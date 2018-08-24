import {SET_POPFILMS, FILM_FETCHED,FILM_SEARCHED} from '../constants';   
const films=(state=[],action={})=>{
    switch(action.type){
      case SET_POPFILMS:
        return action.popfilms;
      // case FILM_SEARCHED:
      //   console.log("sdhs",action.filmList)
      //   return action.filmList
        // const indexs=state.findIndex(item=>item.did===action.filmList.did)
        // if(indexs>-1){
        //   return state.map(item=>{
        //        if(item.did===action.filmList.did) return action.filmList
        //        return item;
        //   })
        //  }else{
        //   return [
        //     ...state,
        //     action.filmList
        //   ]
        // }
        // return action.filmList;
      case FILM_FETCHED:
        const index=state.findIndex(item=>item._id===action.film._id)
        if(index>-1){
          return state.map(item=>{
               if(item._id===action.film._id) return action.film
               return item;
          })
         }else{
          return [
            ...state,
            action.film
          ]
        }
      default:return state;
    }
};

export default films;


