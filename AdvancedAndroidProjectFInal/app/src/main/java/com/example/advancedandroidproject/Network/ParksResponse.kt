package com.example.advancedandroidproject.Network


data class ParksResponse(
    val data: List<Park>
)

data class Park(
    val id: String,
    val fullName: String,
    val description: String,
    val url: String,
    val images:List<ImageObject>,
    val latitude: String,
    val longitude: String,
    val addresses: List<AddressObject>,
)

data class ImageObject(
    val credit: String,
    val title: String,
    val altText: String,
    val caption: String,
    val url: String,
){}

data class AddressObject(
    val id: String,
    val name: String,
    val description: String,
    val postalCode: String,
    val city: String,
    val stateCode: String,
    val line1: String,
    val type: String,
    val line3: String,
    val line2: String
){}