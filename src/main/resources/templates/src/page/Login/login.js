import React,{Component} from 'react';
import './login.css';
import {Link} from 'react-router-dom';
import classnames from 'classnames';
import {connect} from 'react-redux';
import validateInput from '../../config/validations/login';
import {userLoginRequest} from '../../actions/usersActions';
import {withRouter} from 'react-router-dom';
class Login extends Component{
    constructor(props){
        super(props);
        this.state={
            username:'',
            password:'',
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
        if(this.isValid()){
            this.setState({errors:{},isLoading:true});
            this.props.userLoginRequest(this.state)
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
        const {errors} =this.state;
        return(
            <div className="login-page">
            <div className="login-logo">
               <img src={require('../../static/images/login.gif')} alt="login"/>
            </div>

            {errors.msg&&<div>{errors.msg}</div>}
            <form onSubmit={this.onSubmit}>
               <div className="user-name">
                  <label>用户名：</label> 
                  <input 
                    type="text" 
                    name="username"
                    value={this.state.username}
                    onChange={this.onChange}
                    className={classnames(`input`,{'is-invalid':errors.username})}/>
                    {errors.username && <span className="error">{errors.username}</span>}
                </div>

              <div className="user-password">
                  <label>密&emsp;码：</label> 
                  <input 
                    type="password"
                    name="password"
                    value={this.state.password}
                    onChange={this.onChange} 
                    className={classnames(`input`,{'is-invalid':errors.username})}/>
                    {errors.password && <span className="error">{errors.password}</span>}
              </div>

              <div className="user-login">
                  <input disabled={this.state.isLoading} type="submit" value="登录"/>
                  <span>忘记密码？</span>
              </div>
            </form>
               <div  to="/register" className="account-none" >
                    <Link to="/register" className="account-none"> 没有账号？立即注册</Link>
               </div>
            </div>
        )
    }
}
export default withRouter(connect(null,{userLoginRequest})(Login));