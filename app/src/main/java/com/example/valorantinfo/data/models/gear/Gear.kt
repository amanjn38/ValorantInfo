package com.example.valorantinfo.data.models.gear

import com.google.gson.annotations.SerializedName

data class Gear(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("displayIcon")
    val displayIcon: String?,
    @SerializedName("assetPath")
    val assetPath: String,
    @SerializedName("descriptions")
    val descriptions: List<String>?,
    @SerializedName("details")
    val details: List<GearDetail>?,
    @SerializedName("shopData")
    val shopData: GearShopData?
)

data class GearDetail(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String
)

data class GearShopData(
    @SerializedName("cost")
    val cost: Int,
    @SerializedName("category")
    val category: String,
    @SerializedName("shopOrderPriority")
    val shopOrderPriority: Int,
    @SerializedName("categoryText")
    val categoryText: String,
    @SerializedName("gridPosition")
    val gridPosition: String?,
    @SerializedName("canBeTrashed")
    val canBeTrashed: Boolean,
    @SerializedName("image")
    val image: String?,
    @SerializedName("newImage")
    val newImage: String?,
    @SerializedName("newImage2")
    val newImage2: String?,
    @SerializedName("assetPath")
    val assetPath: String
) 