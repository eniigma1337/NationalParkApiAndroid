package com.example.advancedandroidproject.models

import android.os.Parcelable
import com.example.advancedandroidproject.Network.AddressObject
import com.example.advancedandroidproject.Network.ImageObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParkAsArg(
    var id: String,
    var fullName: String,
    var address:String,
    var url: String,
    var image:String,
    var description: String
) : Parcelable {}