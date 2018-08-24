import React,{ Component } from 'react';
import './footer.css';
class Footer extends Component{
    render(){
        return(
            <div className="components-footer">
               <div className="copyright-info">
                &nbsp;版权信息
                <br/>
                Copyright
               </div>
            </div>
        );
    }
};

export default Footer;