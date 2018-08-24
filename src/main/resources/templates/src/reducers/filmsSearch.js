import {FILM_SEARCHED} from '../constants';

const filmList=(state=[],action={})=>{
	switch(action.type){
	 case FILM_SEARCHED:
        console.log("sdhs",action.filmList)
        return action.filmList
	 default:return state;
	}
}

export default filmList;