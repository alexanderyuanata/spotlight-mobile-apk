package com.mobile.entertainme.response

import com.google.gson.annotations.SerializedName

data class TravelResponse(

	@field:SerializedName("location_count")
	val locationCount: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("recommendations")
	val recommendations: List<TravelDataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class TravelDataItem(

	@field:SerializedName("Time_Minutes")
	val timeMinutes: Int? = null,

	@field:SerializedName("coverUrl")
	val coverUrl: String? = null,

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("Categories")
	val categories: String? = null,

	@field:SerializedName("Coordinate")
	val coordinate: String? = null,

	@field:SerializedName("Place_Name")
	val placeName: String? = null,

	@field:SerializedName("Price")
	val price: Int? = null,

	@field:SerializedName("Rating_Count")
	val ratingCount: Int? = null,

	@field:SerializedName("City")
	val city: String? = null,

	@field:SerializedName("Ratings")
	val ratings: Int? = null
)
