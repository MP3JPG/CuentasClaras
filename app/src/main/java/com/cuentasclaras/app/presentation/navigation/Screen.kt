package com.cuentasclaras.app.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Groups : Screen("groups")
    data object GroupDetail : Screen("group_detail/{groupId}/{groupName}") {
        fun createRoute(groupId: String, groupName: String): String {
            val encodedName = Uri.encode(groupName)
            return "group_detail/$groupId/$encodedName"
        }
    }
}