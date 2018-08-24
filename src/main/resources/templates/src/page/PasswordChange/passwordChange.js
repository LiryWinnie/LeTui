import React,{Component} from 'react';
import './passwordChange.css'
import validateInput from '../../config/validations/passwordChange';
import {connect} from 'react-redux';
import {userPasswordChangeRequest} from '../../actions/usersActions';
class PasswordChange extends Component{
    constructor(props){
        super(props);
        this.state={
            oldpassword:"",
            newpassword:"",
            newpasswordconfirmation:"",
            errors:{},
        }
    }
    onChange=(e)=>{
        this.setState({
            [e.target.name]:e.target.value
        })
    }
    isValid=()=>{
        const {errors,isValid}=validateInput(this.state);
        if(!isValid){
            this.setState({errors})
        }
        return isValid;
    }
    onSubmit=(e)=>{
      console.log("sdjg",this.props);
      const { match}=this.props;
      e.preventDefault();
      const {oldpassword,newpassword}=this.state;
      if(this.isValid()){
        // console.log("sgfd",match.params.uid);
        // console.log("ttt",oldpassword);
          this.props.userPasswordChangeRequest(match.params.uid,this.state)
          .then(
                res=>{
                    if(res.data.code!==0){
                        this.setState({
                            errors:res.data,
                            isLoading:false
                        });

                    }else{
                        this.props.history.push('/')
                    }
                }
               
            );

          }
        }
    
    render(){

        const {errors}=this.state;
        return(
         <form onSubmit={this.onSubmit}>
            <div className="passwordChange-page">
            <div className="passwordChange-logo">
               <img src={require('../../static/images/pwd-change.gif')} alt="pwd-change"/>
            </div>
               <div className="old-pwd">
                  <label htmlFor="">原&emsp;密&emsp;码：</label> 
                  <input 
                    type="password" 
                    name="oldpassword"
                    id="oldpassword"
                    value={this.state.oldpassword}
                    onChange={this.onChange}
                    />
                {errors.oldpassword&& <span className="error">{errors.oldpassword}</span>}
                {errors.msg&&<div className="error">{errors.msg}</div>}
                </div>

              <div className="new-pwdf">
                  <label htmlFor="">新&emsp;密&emsp;码：</label> 
                  <input 
                     type="password" 
                     name="newpassword"
                     id="newpassword" 
                     value={this.state.newpassword}
                     onChange={this.onChange}
                     />
             {errors.newpassword&& <span className="error">{errors.newpassword}</span>}     
              </div>
              <div className="new-pwds">
                  <label htmlFor="">确认新密码：</label> 
                  <input 
                    type="password"
                    name="newpasswordconfirmation"  
                    id="newpasswordconfirmation"  
                    value={this.state.newpasswordconfirmation}
                    onChange={this.onChange}
                    />
               {errors.newpasswordconfirmation&& <span className="error">{errors.newpasswordconfirmation}</span>}
              </div>
              <div className="password-change">
                  <input type="submit" value="修改"/>
              </div>
            </div>
            </form>
        );
    }
}

// const mapStateToProps = (state) => {

//     return {
//       auth: state.auth
//     };
//   };
export default connect(null,{userPasswordChangeRequest})(PasswordChange);