package com.example.vetmed.core.util

import com.example.vetmed.R

sealed class Options(
    val route: String,
    val iconId: Int
) {

       object Share : Options(
        route = "Share",
        iconId = R.drawable.share
    )
    object AboutUs : Options(
        route = "AboutUs",
        iconId = R.drawable.about_us
    )
    object PrivacyPolicy : Options(
        route = "Privacy",
        iconId = R.drawable.privacy_policy
    )

}
