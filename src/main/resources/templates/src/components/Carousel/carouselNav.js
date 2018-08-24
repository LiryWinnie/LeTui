import React,{ Component }from 'react';

class CarouselNav extends Component{
    constructor(props){
        super(props);
    };
    render(){
        let listNav=null;
        listNav=this.props.carouselNavItems.map((item,i)=>{
            if(i===this.props.currentIndex){
                return(
                    <li 
                    className="carousel-nav-item carousel-nav-item-active"
                    key={i}
                    onClick={()=>this.props.selectImage(i)}>
                    </li>
                );
            }else{
                return(
                    <li
                      className="carousel-nav-item"
                      key={i} 
                      onClick={()=>this.props.selectImage(i)}>
                    </li>
                );
            }
        });
        return(
            <ul className="carousel-nav">
            {/* 轮播图文字说明
              {this.props.carouselNavItems[this.props.currentIndex].title} */}
            <br/>
               {listNav}
            </ul>
        );
    }

};

export default CarouselNav;