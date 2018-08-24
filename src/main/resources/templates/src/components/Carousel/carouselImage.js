import React,{Component} from 'react';
import CSSTransitionGruop from 'react-addons-css-transition-group';
import {Link} from 'react-router-dom';


class CarouseImage extends Component{
    constructor(props){
        super(props);
    };
    render(){
        return (
            <ul className="carousel-image">
               <CSSTransitionGruop
                   transitionName={this.props.name}
                   transitionEnterTimeout={this.props.enterDelay}
                   transitionLeaveTimeout={this.props.leaveDelay}
                   className={this.props.name}
               >
               <Link to={`/filminfo/${this.props.imageSrc[this.props.currentIndex].did}`}> 
                <li>
                     <div className="des">
                         {this.props.imageSrc[this.props.currentIndex].movieName}
                     </div>
                 <img
						 src={this.props.imageSrc[this.props.currentIndex].img}  
						 key={this.props.imageSrc[this.props.currentIndex].did} 
					/>
                 </li>
                </Link>
               </CSSTransitionGruop>
            </ul>
        );
    };
};


export default CarouseImage;