import React,{Component} from 'react';
import './personalInfo.css';
import {connect} from 'react-redux';
import classnames from 'classnames';
import {withRouter,Link,Redirect} from 'react-router-dom';
import {fetchUser,updateUser} from '../../actions/usersActions';

class PersonalInfo extends Component{
  constructor(props){
    super(props);
    this.state={
      male:this.props.user.gender==="男"?true:false,
      gender:this.props.user?this.props.user.gender:"",
      age:this.props.user?this.props.user.age:0,
      readonly:true
    }
    // console.log("adh",this.props);
  }
  ageEdit=(e)=>{
    this.setState({
      readonly:false
    })
  }
  ageChange=(e)=>{
    this.setState({
      age:e.target.value
    })
  }
  ageBlur=(e)=>{
    this.setState({
      readonly:true
    })
  }
  handleGender=(e)=>{
    let male=!!(e.target.value==="男");
    this.setState({
      male:male,
      gender:male?"男":"女"
    })
  }
  onSubmit=(e)=>{
    const{match}=this.props;
    e.preventDefault();
    this.props.updateUser(match.params.uid,this.state)
    // this.props.history.push(`/personalinfo/${match.params.uid}`)
      

  }


  componentDidMount(){
    const{match}=this.props;
    this.props.fetchUser(match.params.uid);
    // console.log("adh",this.props)
  };
  componentWillReceiveProps(nextProps){
    this.setState({
      age:nextProps.user.age,
      gender:nextProps.user.gender
    })
  }
    render(){
      const {match,user}=this.props;
      // console.log("shf",this.props)
      const pwdLink=`/personalinfo/${match.params.uid}/passwordChange`;
        return(
            <form onSubmit={this.onSubmit}>
              <div className="personalInfo-page">
              <div className="left">
                <div className="personalInfo-icon-username">
                  <div className="icon">
                      <img src={require('../../static/images/user-icon.png')} />
                  </div>
                  <div className="username">
                      {user.userName}
                  </div>
               </div>
              </div>           
             <div className="right">
               <div className="personalInfo-logo">
                  <img src={require('../../static/images/personal-info.gif')} alt="personalInfo-logo" />
               </div>
               <div className="personalInfo-gender">
                  <span className="tag">性别：</span>
                   <label htmlFor="male">男
                     <input
                       type="radio" 
                       value="男" 
                       id="male" 
                       name="gender"
                       checked={this.state.gender==="男"?true:false}
                       onChange={this.handleGender}/>
                   
                  </label>
                  <label htmlFor="female">女
                     <input
                       type="radio" 
                       value="女" 
                       id="female" 
                       name="gender"
                       checked={this.state.gender==="女"?true:false}
                       onChange={this.handleGender}/>
                  </label>
               </div>
               <div className="personalInfo-age">
                  <span className="tag">年龄：</span>
                  <input 
                    type="text" 
                    value={this.state.age} 
                    readOnly={this.state.readonly} 
                    onChange={this.ageChange}
                    onBlur={this.ageBlur}
                    className={classnames({'edit':!this.state.readonly})}/>
                  <span  onClick={this.ageEdit}><img src={require('../../static/images/edit.png')} alt="edit"/></span>
               </div>
               <div className="personalInfo-password">
                  <span className="tag">密码：</span>
                  <input type="password" value={user.password} readOnly="readonly"/>
                   <Link to={pwdLink}><img src={require('../../static/images/edit.png')} alt="edit"/></Link>
               </div>
               <div className="personalInfo-btn">
                 <input type="submit" value="确定修改"/>
               </div>
             </div>
         </div>
      </form>
        );
    }
}

const mapStateToProps=(state)=>{
  return{
    user:state.user,
    auth:state.auth
  }
}

export default withRouter(connect(mapStateToProps,{fetchUser,updateUser})(PersonalInfo));