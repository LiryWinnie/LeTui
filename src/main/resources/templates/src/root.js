import React,{ Component } from 'react';
import Header from './components/Header/header';
import Footer from './components/Footer/footer';
import Home from './page/home';
import Login from './page/Login/login';
import Register from './page/Register/register';
import FilmInfo from './page/FilmInfo/filmInfo';
import PersonalInfo from './page/PersonalInfo/personalInfo';
import PasswordChange from './page/PasswordChange/passwordChange';
import SearchFilmResult from './page/SearchFilmResult/searchFilmResult';
import RecomFilmResult from './page/RecomFilmResult/recomFilmResult';
import{BrowserRouter as Router,Route,NavLink} from 'react-router-dom';
import rootReducer from '../src/reducers';
import {Provider } from 'react-redux';
import {createStore,applyMiddleware} from 'redux';
import {composeWithDevTools} from 'redux-devtools-extension';
import logger from 'redux-logger';
import thunk from 'redux-thunk';
import filmInfo from './page/FilmInfo/filmInfo';


import setAuthorizationToken from './config/setAuthorizationToken';
import {setCurrentUser} from './actions/usersActions';
import jwtDecode from 'jwt-decode';

const store =createStore(
    rootReducer,{},
    composeWithDevTools(
        applyMiddleware(logger,thunk)
    )
);

if (localStorage.user) {
    setAuthorizationToken(localStorage.user);
    store.dispatch(setCurrentUser(localStorage.user));
  }

class Root extends Component{
    render(){
        // console.log("dfh",this.props);
        return(
          <Provider  store={store}>
          <Router>
        <section>
          <Header/>
          {/* <Route path="/" component={Header}/> */}
           <Route exact path="/" component={Home} />
           <Route path="/login" component={Login} />
           <Route path="/register" component={Register}/>
           <Route path="/filminfo/:did" component={filmInfo} />
           <Route path="/searchres/:text" component={SearchFilmResult}/>
           <Route  exact path="/personalinfo/:uid" component={PersonalInfo}/>
           <Route path="/personalinfo/:uid/passwordChange" component={PasswordChange}/>
           <Route path="/recommend/:uid" component={RecomFilmResult}/>
             <Footer/>
             </section>
             </Router>
             </Provider>
            //   </HashRouter>
        //   </div>
        );
    }
};

export default Root;