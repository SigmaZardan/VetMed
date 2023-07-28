package com.example.vetmed.feature_profile.domain.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("picture") var picture: String? = null,

    )
