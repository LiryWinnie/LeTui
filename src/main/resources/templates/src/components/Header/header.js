import React,{ Component } from 'react';
import {Link} from 'react-router-dom';
import './header.css';
import { Redirect,withRouter} from 'react-router-dom';
import {connect} from 'react-redux';
import {userLogoutRequest} from '../../actions/usersActions';

class Header extends Component{
    constructor(props,context){
        super(props,context);
        this.state={
            searchItem:"",
            searchDone:false,
        };
    }
    handleChange(event){
        let name,obj;
        name=event.target.name;
        this.setState((
            obj={},
            obj[""+name]=event.target.value,
            obj
        ))
    }
    valid(){
        return this.state.searchItem;
    }
    handleSubmit(event){
        event.preventDefault();
      // console.log(this.state.searchItem);
        this.setState({
            searchDone:true,
            //  searchItem:true
        });
        // console.dir(this.props);
        this.props.history.push(`/searchres/${this.state.searchItem}`);
        
    }

    recomClick=(e)=>{
      e.preventDefault();
      const {user}=this.props.auth;
      this.props.history.push(`/recommend/${user.userId}`);
    }

    logout=(e)=>{
        e.preventDefault();
        this.props.userLogoutRequest();
    }

    render(){
        const {isLogin,user}=this.props.auth;
        const recomButton=(
            <button onClick={this.recomClick}>推荐</button>
        );
        const avatorLinks=isLogin? `/personalinfo/${user.userId}` : `/login`;
        const out=(
            <a className="logout" onClick={this.logout}>
               退出
             </a>
        )
        return(
            <div className="components-header">
            <Link className="logo" to="/">乐推</Link>
            < form className="search" onSubmit={this.handleSubmit.bind(this)}>
               <input type="text"  onChange={this.handleChange.bind(this)} className="search-input" name="searchItem"  placeholder="输入电影名或导演名搜索" value={this.state.searchItem}/> 
              <button type="submit" disabled={!this.valid()}>搜索</button>
             {/* <Link> <button onClick={this.handleSubmit.bind(this)} type="submit" disabled={!this.valid()}>搜索</button></Link> */}
            </form>
            <Link className="user-icon" to={avatorLinks}> 
                 <img src={require('../../static/images/user-icon.png')}/>
             </Link>
             <div className="film-recom-button" style={{display: ``}}>
                {isLogin?recomButton:''}
             </div>
             {isLogin?out:""}
         </div>

        );
    }
};

const mapStateToProps = (state) => {
    return {
      auth: state.auth
    };
  };

export default withRouter(connect(mapStateToProps,{userLogoutRequest})(Header));