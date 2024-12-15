package com.mobile.entertainme.response

import com.google.gson.annotations.SerializedName

data class BookResponse(

	@field:SerializedName("title_count")
	val titleCount: Int? = null,

	@field:SerializedName("titles")
	val titles: List<BookDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BookDataItem(

	@field:SerializedName("coverUrl")
	val coverUrl: String? = null,

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("publishYear")
	val publishYear: String? = null,

	@field:SerializedName("infoUrl")
	val infoUrl: String? = null,

	@field:SerializedName("Book")
	val book: String? = null,

	@field:SerializedName("Author")
	val author: String? = null,

	@field:SerializedName("Genres")
	val genres: String? = null,

	@field:SerializedName("Avg_Rating")
	val avgRating: Any? = null
)
