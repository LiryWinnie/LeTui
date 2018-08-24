package com.winnie.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.query.spi.sql.NativeSQLQueryCollectionReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.winnie.domain.HotMovie;
import com.winnie.domain.Movie;
import com.winnie.domain.MovieList;
import com.winnie.domain.Result;
import com.winnie.domain.Score;
import com.winnie.domain.SortMovie;
import com.winnie.handle.ExceptionHandle;
import com.winnie.repository.MovieRepository;
import com.winnie.repository.ScoreRepository;
import com.winnie.utils.ResultUtil;

import org.springframework.data.domain.Sort;
import net.sf.json.JSONException;
import static com.winnie.enums.ResultEnum.*;

/**
* Created by彭文钰
* 2018年5月18日 下午6:14:05
*/
@Service
public class MovieService {
	
	@Autowired
	MovieSearch movieSearch;
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ScoreRepository scoreRepository;
	
	/**
	 *查询20条电影信息
	 */
	public List<MovieList> movieSearch(String text) {
		String url = "https://api.douban.com/v2/movie/search?q="+text;
		String jsonS = movieSearch.doGet(url);
		
		try {
			return movieSearch.toList(jsonS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *查询电影信息（豆瓣ID查询）
	 */
	public Movie movieInfo(String did) {
		// TODO Auto-generated method stub
		String url = "https://api.douban.com/v2/movie/subject/"+did;
		String jsonString = movieSearch.doGet(url);	
		Movie info = new Movie();
		try {
			info = movieSearch.toInfo(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		HotMovie hot = new HotMovie();
		hot.setDid(did);
		
		if(movieRepository.exists(Example.of(hot)) == false) {
			hot.setTimes(1);
			movieRepository.save(hot);
		}else {
			hot = movieRepository.findOne(Example.of(hot)).get();
			hot.setTimes(hot.getTimes()+1);
			movieRepository.save(hot);
		}
		return info;
	}
	/**
	 *保存评分
	 */

	public Result<Score> movieScore(String did, Integer userId, Integer score) {
		Score mscore = new Score();
		mscore.setDid(did);
		mscore.setUserId(userId);
		if(scoreRepository.exists(Example.of(mscore))) 
			mscore.setSid(scoreRepository.findOne(Example.of(mscore)).get().getSid());
		mscore.setScore(score);
		return ResultUtil.success(scoreRepository.save(mscore));		
	}

	/**
	 *热门电影查询（返回times最多的前十位）
	 */
	public List<MovieList> movieHot() {
		Sort sort = new Sort(Sort.Direction.DESC, "times");
		List<HotMovie> hotmoo = movieRepository.findAll(sort);
		List<MovieList> hotMovie = new ArrayList<MovieList>();
		int leng = 0;
		if(hotmoo.size()<10) {
			leng = hotmoo.size();
		}else {
			leng = 10;
		}
		for(int i = 0; i<leng; i++) {
			Movie m = new Movie();
			MovieList mList =new MovieList();
			String jsons = movieSearch.doGet("https://api.douban.com/v2/movie/subject/"+hotmoo.get(i).getDid());
			m = movieSearch.toInfo(jsons);
			mList.setCasts(m.getCasts());
			mList.setDirector(m.getDirector());
			mList.setImg(m.getImg());
			mList.setMovieName(m.getName());
			mList.setDid(m.getDid());
			mList.setGenres(m.getGenres());
			hotMovie.add(mList);
		}
		return hotMovie;
	}
	/**
	 *获取评分
	 */
	public Result<Score> GetMovieScore(String did, Integer uid) {
		Score score = new Score();
		score.setDid(did);
		score.setUserId(uid);
		if(scoreRepository.exists(Example.of(score)))
			return ResultUtil.success(scoreRepository.findOne(Example.of(score)).get());
		return ResultUtil.error(SCORE_UNEXSIT);
	}
	
}
