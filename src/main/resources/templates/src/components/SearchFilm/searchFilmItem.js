import React,{Component} from 'react';
import './searchFilmItem.css'
import {Link} from 'react-router-dom';
class SearchFilmItem extends Component{

    render(){
        let searchFilmItem=this.props.searchFilmItem;
        return(
            <li className="components-searchFilmItem">
            <div className="film-cover">
               <Link to={`/filminfo/${searchFilmItem.did}`}> <img src={searchFilmItem.img} alt="film-cover"/></Link>
            </div>
            <div className="film-generalInfo">
                 <div className="film-name">{searchFilmItem.movieName}（{searchFilmItem.year}）</div>
                 <div className="film-score">电影类型：{searchFilmItem.genres}</div>
                 <div className="film-nation">导演：{searchFilmItem.director}</div>
            </div>
            </li>
        )
    }
}

export default SearchFilmItem;