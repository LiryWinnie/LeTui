import React,{Component} from 'react';

class ButtonGroup extends Component{
    constructor(props){
        super (props);
    }
    render(){
        return(
            <div className="button-group">
               <span className="button-left" onClick={this.props.prevImage}>{'<'}</span>
               <span className="button-right" onClick={this.props.nextImage}>{'>'}</span>
            </div>
        )
    }
}

export default ButtonGroup;