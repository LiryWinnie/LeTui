package com.winnie.domain;

/**
* Created by彭文钰
* 2018年6月11日 上午11:28:04
*/
public class SortMovie<MovieList> {

	private float similarity;
	
	private MovieList recomMovie;

	public float getSimilarity() {
		return similarity;
	}

	public void setSimilarity(float similarity) {
		this.similarity = similarity;
	}

	public MovieList getRecomMovie() {
		return recomMovie;
	}

	public void setRecomMovie(MovieList recomMovie) {
		this.recomMovie = recomMovie;
	}
	
}
