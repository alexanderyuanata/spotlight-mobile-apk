package com.mobile.entertainme.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

	@field:SerializedName("movies")
	val movies: List<MovieDataItem?>? = null,

	@field:SerializedName("movie_count")
	val movieCount: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class MovieDataItem(

	@field:SerializedName("cover_url")
	val coverUrl: String? = null,

	@field:SerializedName("star")
	val star: String? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("director")
	val director: String? = null,

	@field:SerializedName("genre")
	val genre: String? = null,

	@field:SerializedName("rating")
	val rating: Any? = null,

	@field:SerializedName("runtime")
	val runtime: Int? = null,

	@field:SerializedName("votes")
	val votes: Int? = null,

	@field:SerializedName("movie_name")
	val movieName: String? = null
)
