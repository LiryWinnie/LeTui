import React,{Component} from 'react';
import './filmInfo.css';
import {connect} from 'react-redux';
import classnames from 'classnames';
import {fetchFilm,ratingFilm,fetchUserScore} from '../../actions/filmsAction';
class filmInfo extends Component {
    constructor(props){
        super(props);
        this.state={
            did:this.props.film?this.props.film.did:null,
            name:this.props.film?this.props.film.name:'',
            img:this.props.film?this.props.film.img:'',
            director:this.props.film?this.props.film.director:'',
            casts:this.props.film?this.props.film.casts:'',
            genres:this.props.film?this.props.film.genres:'',
            year:this.props.film?this.props.film.year:'',
            summary:this.props.film?this.props.film.summary:'',
            rate:this.props.film?this.props.film.rate:'',
            scoringItem:'none',
            userScore:0,
            myScore:0,
        }
    }
    handleScoringBtn=(e)=>{
        this.setState({
            scoringItem:"block"
        })
    }

    onChange=(e)=>{
        this.setState({
            userScore:e.target.value
        })
    }

    onSubmitScore=(e)=>{
        e.preventDefault();
        const {user}=this.props.auth;
        const {match}=this.props;
        // const scoreData={
        //     "did":match.params.did,
        //     "userId":user.userId,
        //     "userScore":this.state.userScore
        // };
        console.log("sk",match.params.did)
        this.props.ratingFilm(match.params.did,user.userId,this.state.userScore);

        this.setState({
            scoringItem:"none",
            myScore:this.state.userScore
        })
    }

    componentDidMount(){
        const {match}=this.props;
         const {user}=this.props.auth;
        if(true){
        this.props.fetchFilm(match.params.did);
        this.props.fetchUserScore(match.params.did,user.userId)
          .then(
              res=>{
                if(res.data.code===0){
                this.setState({
                    myScore:res.data.data.score
                });
              }
              }
            )
        // console.log("dhfi",this.props);
        }
        
      }
      componentWillReceiveProps(nextProps) {
        this.setState({
          did: nextProps.film.did,
          name: nextProps.film.name,
          img: nextProps.film.img,
          director:nextProps.film.director,
          casts:nextProps.film.casts,
          genres:nextProps.film.genres,
          year:nextProps.film.year,
          summary:nextProps.film.summary,
          rate:nextProps.film.rate,
        })
      }
    render(){
        // const f;
        // console.dir(this.state);
        const {isLogin}=this.props.auth;
        return(
            <div className="filmInfo-page">
                <h1 className="film-name">{this.state.name}</h1>
                <div className="film-details">
                 <div className="film-img">
                   <img src={this.state.img} alt="film-img"/>
                 </div>
                 <div className="film-infos">
                    <div>
                        <span className="tag">导演：</span>
                        <span className="director-name">{this.state.director}</span>
                    </div>
                    <div>
                        <span className="tag">主演：</span>
                        <span className="actor-name">{this.state.casts}</span>
                    </div>
                    <div>
                        <span className="tag">类型：</span>
                        <span className="film-type">{this.state.genres}</span>
                    </div>
                    <div>
                        <span className="tag">上映年份：</span>
                        <span className="release-date">{this.state.year}</span>
                    </div>
                    <div>
                        <span className="tag">剧情简介:</span>
                        <span className="plot-intro">{this.state.summary}</span>
                    </div>
                 </div>
                 <div className="film-score">
                    <div className="average">
                        <span className="tag">平均得分:</span>
                        <span className="average-score">{this.state.rate}分</span>
                    </div>
                    <div className="my" style={{display:isLogin?"block":"none"}}>
                        <span className="tag" >我的评分:</span>
                        <span className="my-score">{this.state.myScore}分</span>
                    </div>
                 
                 <div className="scoring" style={{display:isLogin?"block":"none"}}>
                    <button onClick={this.handleScoringBtn}>我要评分/修改评分</button>
                 </div>
                 <div className="scoring-change" style={{display:this.state.scoringItem}}>
                  <form onSubmit={this.onSubmitScore}>
                     <div className="tag">输入评分（1-10）:</div>
                     <div className="score-input">
                       <input 
                          type="number"  
                          value={this.state.userScore } 
                          onChange={this.onChange}
                          min="1" 
                          max="10"/>
                     </div>
                     <div className="score-sub">
                         <input type="submit" value="提交"/>
                     </div>
                     </form>
                 </div>
                 </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps=(state,props)=>{
  const { match } = props;
  if (match.params.did) {
    //   let p=state.films.find(item => item._id === match.params._id);
       //console.log("sdhsuifhd",p);
   return{
       film:state.films.find(item => item.did === match.params.did),
       auth:state.auth
   };
 }
 return { film: null }; 
     auth:state.auth

}


export default connect(mapStateToProps,{fetchFilm,ratingFilm,fetchUserScore})(filmInfo);