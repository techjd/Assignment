package com.myjar.jarassignment.data.local

import com.google.gson.annotations.SerializedName
import com.myjar.jarassignment.data.model.ComputerItem

data class LocalData(
    @SerializedName("myitems")
    val computerItems: List<ComputerItem>
)
