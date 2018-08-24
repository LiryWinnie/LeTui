import React,{ Component } from 'react';
// import CSSTransitionGroup from 'react-addons-css-transition-group';
// import {IMAGE_LIST} from '../../config/images'
import CarouselImage from './carouselImage';
import CarouselNav from './carouselNav';
import ButtonGroup from './buttonGroup';
import './carousel.css';

class Carousel extends Component{

    render(){
        // console.log(this.props);
        return(
            <div className="carousel">
               <CarouselImage
                  imageSrc={this.props.imageSrc}
                  currentIndex={this.props.currentIndex}
                  enterDelay={1500}
                  leaveDelay={1500}
                  name={"carousel-image-item"}
               />

               <CarouselNav
                  carouselNavItems={this.props.imageSrc}
                  currentIndex={this.props.currentIndex}
                  selectImage={this.props.selectImage}
               />
               <ButtonGroup
                   prevImage={this.props.prevImage}
                   nextImage={this.props.nextImage}
                 />
            </div>
        );
    }

};

export default Carousel;