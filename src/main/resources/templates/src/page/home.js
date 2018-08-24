import React,{Component} from 'react';
import Carousel from '../components/Carousel/carousel';
import PopularFilm from '../components/PopularFilm/popularFilm';
import {IMAGE_LIST} from '../config/images';
import {connect} from 'react-redux';
import {fetchPopfilms} from '../actions/filmsAction';
class Home extends Component{

    constructor(props){
        super(props);
        this.state={
            carouselFilms:IMAGE_LIST,
            currentIndex:0,
        };
        /*定时器引用 */
        this.timer=null;

        // 绑定事件中的this
        this.selectImage=this.selectImage.bind(this);
        this.prevImage=this.prevImage.bind(this);
        this.nextImage=this.nextImage.bind(this);
        this._updateIndex=this._updateIndex.bind(this);

    }
 
/**
 * 展示选中的图片
 */
    selectImage(index){
        let len=this.state.carouselFilms.length;
        this._updateIndex(index,len);
    }

/**
 * 展示前一张图片
 */
    prevImage(){
        let currentIndex=this.state.currentIndex;
        var len=this.state.carouselFilms.length;
        currentIndex=(currentIndex-1)>0? (currentIndex-1)%len:len-1;
        this._updateIndex(currentIndex,len);
    }
/**
*展示下一张图片 
*/
    nextImage(){
        let currentIndex=this.state.currentIndex;
        var len=this.state.carouselFilms.length;
        currentIndex=(currentIndex+1)%len;
        this._updateIndex(currentIndex,len);
    }
/**
* 工具函数，用于更新state，以及刷新定时器
* @param  {number} index 将要展示图片的索引
* @param  {number} len   展示图片总张数
* @param  {number} delay 动画持续时间
*/
    _updateIndex(index,len,delay=4000){
        /*清楚定时器 */
        this.timer&&clearInterval(this.timer);
        /*设置当前展示图片*/
        this.setState({
            currentIndex:index
        });
        /*打开定时器 */
        this.timer=setInterval(
            ()=>{
                let currentIndex=this.state.currentIndex;
                this.setState({
                    currentIndex:(currentIndex+1)%len
                });
            },
            delay
        );
    }
/**
 * 组件加载完毕后，图片自动播放
 */
    componentDidMount(){
        this.props.fetchPopfilms();
        this.timer=setInterval(
            ()=>{
                this.setState({
                    currentIndex:(this.state.currentIndex+1)%4
                });
            },
            4000
        );
    }
    componentWillReceiveProps(nextProps) {
        this.setState({
            carouselFilms:nextProps.popularFilms.slice(0,4)
        })
    }
/**
 * 组件卸载时，清理定时器
 */
    componentWillUnmount(){
        this.timer&&clearInterval(this.timer);
    }
    render(){
        const {popularFilms}=this.props;
        const popFilmList=popularFilms.slice(4);
        return(
            <div>
                <Carousel
                  imageSrc={this.state.carouselFilms}
                // imageSrc={popularFilms}
                  currentIndex={this.state.currentIndex}
                  selectImage={this.selectImage}
                  prevImage={this.prevImage}
                  nextImage={this.nextImage}
                />
                <PopularFilm
                   popularFilm={popFilmList}
                />
                
            </div>
     
        );
    }
}

 const mapStateToProps=(state)=>{
     return{
         popularFilms:state.films
     }
 }
export default connect(mapStateToProps,{fetchPopfilms})(Home);
