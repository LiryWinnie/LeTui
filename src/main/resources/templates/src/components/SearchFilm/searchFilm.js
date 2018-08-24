import React,{Component} from 'react';
import SearchFilmItem from './searchFilmItem';
import './searchFilm.css';

class SearchFilm extends Component {
    render(){
        let listFilm=null;
        listFilm=this.props.filmList.map((item)=>{
            return(
                <SearchFilmItem
                  key={item.did}
                  searchFilmItem={item}
                />
            );
        });
        return(
            <ul>
                {listFilm}
            </ul>
        );
    }
}

export default SearchFilm;