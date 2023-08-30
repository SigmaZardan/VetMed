package com.example.vetmed.core.util

import com.example.vetmed.R


sealed class ProfileOptions(
    val route: String,
    val iconId: Int
) {

       object Share : ProfileOptions(
        route = "Share",
        iconId = R.drawable.share
    )
    object AboutUs : ProfileOptions(
        route = "AboutUs",
        iconId = R.drawable.about_us
    )
    object PrivacyPolicy : ProfileOptions(
        route = "Privacy",
        iconId = R.drawable.privacy_policy
    )

}
