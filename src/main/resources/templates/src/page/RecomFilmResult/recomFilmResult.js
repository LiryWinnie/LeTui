import React,{Component} from 'react';
import SearchFilm from '../../components/SearchFilm/searchFilm';
import './recomFilmResult.css';
import {connect} from 'react-redux';
import {userRecomRequest} from '../../actions/usersActions';

class RecomFilmResult extends Component{
	constructor(props){
		super(props);
		this.state={
			recomFilmResult:[]
		}
	}
	componentDidMount(){
		const {match}=this.props;

		this.props.userRecomRequest(match.params.uid)
		.then(
			 res=>{
				 this.setState({
					recomFilmResult:res.data
				});
				 return(res);
			}
		)
	}
	componentWillReceiveProps(nextProps) {

	}
	render(){
		return(
			<div className="recomFilmResult-page">
               <h1 className="searchFilmResult-title">推荐结果如下：</h1>
                <SearchFilm
                   filmList={this.state.recomFilmResult}
                />
            </div>
			);
	}
}

export default connect(null,{userRecomRequest})(RecomFilmResult);