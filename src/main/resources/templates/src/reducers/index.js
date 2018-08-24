import {combineReducers} from 'redux';
import films from './Films';
import auth from './Auth';
import user from './User'
import filmList from './filmsSearch';
const rootReducers =combineReducers({
    films,
    auth,
    user,
    filmList
});

export default rootReducers;