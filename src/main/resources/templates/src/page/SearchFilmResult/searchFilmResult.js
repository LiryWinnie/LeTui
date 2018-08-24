import React,{Component} from 'react';
import SearchFilm from '../../components/SearchFilm/searchFilm';
import {IMAGE_LIST} from '../../config/images';
import './searchFilmResult.css';
import {connect} from 'react-redux';
import { searchFilm} from '../../actions/filmsAction';

class SearchFilmResult extends Component{
    constructor(props){
       
        super(props);
         const {match}=this.props;
        this.state={
            searchFilmResult:this.props.filmList?this.props.filmList:[],
            key:match.params.text
            // searchFilmResult:IMAGE_LIST
        };
        // console.log("sjdg",this.props);
    }
componentDidMount(){
        // const {match}=this.props;
        // let keys=match.params.text;
        // this.setState({
        //     key:keys
        // })
        // console.log("sdjg",this.state.key);
        if(this.state.key){
          this.props.searchFilm(this.state.key);
        }
        // console.log("h",this.props.filmList)
      }
componentWillReceiveProps(nextProps) {
        const {match}=this.props;
        this.setState({
            searchFilmResult:nextProps.filmList,
            key:nextProps.match.params.text
        })
    }
    render(){
        // console.log("wdhg",this.state.searchFilmResult)
        const {match}=this.props;
        return(
            <div className="searchFilmResult-page">
                <h1 className="searchFilmResult-title">搜索&emsp;{match.params.text}</h1>
                <SearchFilm
                   filmList={this.state.searchFilmResult.slice(0,10)}
                />
            </div>
        );
    }
}

const  mapStateToProps=(state)=>{
    console.log("lsjd",state.filmList)
    return{
      filmList:state.filmList
    };
  }
export default connect(mapStateToProps,{searchFilm})(SearchFilmResult);