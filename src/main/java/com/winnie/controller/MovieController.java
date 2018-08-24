package com.winnie.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winnie.domain.Movie;
import com.winnie.domain.MovieList;
import com.winnie.domain.Result;
import com.winnie.domain.Score;
import com.winnie.repository.MovieRepository;
import com.winnie.service.HadoopService;
import com.winnie.service.MovieService;

/**
* Created by彭文钰
* 2018年5月18日 下午5:47:45
*/
@RestController
public class MovieController {
	
	@Autowired
	private MovieService mService;
	
	@Autowired
	private HadoopService hService;
	
	private final static Logger logger = LoggerFactory.getLogger(MovieController.class);
	
	/**
	 *查询20条电影信息
	 */
	@PostMapping(value = "/search/{text}")
	public List<MovieList> movieSearch(@PathVariable("text") String text){	
		logger.info(text);
		return mService.movieSearch(text);
	}
	
	/**
	 *查询电影信息（豆瓣ID查询）
	 */
	@GetMapping(value = "/searchID/{did}")
	public Movie movieInfo(@PathVariable("did") String did) {
		return mService.movieInfo(did);
	}
	/**
	 *保存评分
	 */
	@PostMapping(value = "/score/{did}/{userId}/{score}")
	public Result<Score> movieScore(@PathVariable("did") String did, @PathVariable("userId") String userId,
			@PathVariable("score") String score){
		Integer uid = Integer.valueOf(userId);
		Integer sco = Integer.valueOf(score);
		return mService.movieScore(did, uid, sco);
	}
	/**
	 *获取评分
	 */
	@GetMapping(value = "/score/{did}/{userId}")
	public Result<Score> GetMovieScore(@PathVariable("did") String did, 
			@PathVariable("userId") String userId){
		Integer uid = Integer.valueOf(userId);
		return mService.GetMovieScore(did, uid);
	}
	/**
	 *热门电影查询（返回times最多的前十位）
	 */
	@GetMapping(value = "/hot")
	public List<MovieList> movieHot(){
		return mService.movieHot();
	}
	/**
	 *电影推荐
	 * @throws Exception 
	 */
	@GetMapping(value = "/recommend/{userId}")
	public List<MovieList> movieRecommend(@PathVariable("userId") Integer userId) throws Exception{
		return hService.movieRecommend(userId);
	}
}
