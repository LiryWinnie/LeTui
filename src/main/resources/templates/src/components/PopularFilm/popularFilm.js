import React,{Component} from 'react';
import './popularFilm.css';
import {Link} from 'react-router-dom'
class PopularFilm extends Component{
    render(){
        let listPop=null;
        listPop=this.props.popularFilm.map((item)=>{
            return (
                <li className="components-popularfilmitem" key={item.id}>
               <Link to={`/filminfo/${item.did}` }><img src={item.img} alt="cover"/></Link>
                <div className="film-name">{item.movieName}</div>
               </li>
               )
        });
        return(
            <div className="components-popularfilm">
            <div className="pop-log"> <img src={require('../../static/images/remen.gif')} alt="pop-logo"/></div>
            <ul>
                { listPop }
            </ul>
            </div>
        );
    }
}

export default PopularFilm;