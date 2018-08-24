import React,{Component} from 'react';
import './register.css';
import {connect} from 'react-redux';
import {userRegisterRequest} from '../../actions/usersActions';
import {Link} from 'react-router-dom';
import classnames from 'classnames';
import validateInput from '../../config/validations/register'
import {withRouter} from 'react-router-dom';
class Register extends Component{
    constructor(props){
        super(props);
        this.state={
            username:'',
            password:'',
            passwordConfirmation:'',
            errors:{},
            isLoading:false
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
        e.preventDefault();
        // console.log(this.state)
        // this.setState({errors:{},isLoading:true})
        
        if(this.isValid()){
         this.setState({isLoading:true});
         this.props.userRegisterRequest(this.state).then(
            res=>{
              if(res.data.code===-101){
                this.setState({
                  errors:res.data
                });
              }else{
                alert("注册成功，请登录")
                 this.props.history.push('/login');
              }
            }
            
        );
    }
    }
    render(){
        const {errors}=this.state;
        // console.dir(errors.username)
        return(
            <div className="register-page" >
            <div className="register-logo">
               <img src={require('../../static/images/register.gif')} alt="register"/>
            </div>
            <form onSubmit={this.onSubmit}>
               <div className="user-name">
                  <label>用&nbsp;户&nbsp;名&nbsp;：</label> 
                  <input 
                    type="text"
                    name="username"
                    className={classnames(`input`,{'is-invalid':errors.username})}
                    value={this.state.username}
                    onChange={this.onChange} />
                 {errors.username && <span className="error">{errors.username}</span>}
                 {errors.msg && <span className="error">{errors.msg}</span>}
                </div>

              <div className="user-password">
                  <div className="input-password">
                     <label>密&emsp;&emsp;码：</label> 
                     <input 
                        type="password"
                        name="password"
                        className={classnames(`input`,{'is-invalid':errors.username})}
                        value={this.state.password}
                       onChange={this.onChange}/>
                    {errors.password && <span className="error">{errors.password}</span>}
                  </div>
                  <div className="confirm-password">
                     <label>密码确认：</label> 
                     <input 
                        type="password"
                        name="passwordConfirmation"
                        className={classnames(`input`,{'is-invalid':errors.username})}
                        value={this.state.passwordConfirmation}
                        onChange={this.onChange}/>
                    {errors.passwordConfirmation && <span className="error">{errors.passwordConfirmation}</span>}
                  </div>
              </div>

              <div className="user-register">
                  <input disabled={this.state.isLoading} type="submit" value="注册"/>
              </div>
            </form>
               <div  className="account-already">
                     <Link to="/login" className="account-already">已有账号？立即登录</Link>
               </div>
            </div>
        );
    }
}

export default  withRouter(connect(null,{userRegisterRequest})(Register));
