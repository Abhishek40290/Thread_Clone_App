package com.example.threadclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadclone.screens.AddThreads
import com.example.threadclone.screens.BottomNav
import com.example.threadclone.screens.Home
import com.example.threadclone.screens.Login
import com.example.threadclone.screens.Notification
import com.example.threadclone.screens.OtherUsers
import com.example.threadclone.screens.Profile
import com.example.threadclone.screens.Register
import com.example.threadclone.screens.Search
import com.example.threadclone.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Splash.routes) {

        composable(Routes.Splash.routes) {
            Splash(navController)
        }

        composable(Routes.Home.routes) {
            Home(navController)
        }

        composable(Routes.Notification.routes) {
            Notification()
        }

        composable(Routes.Search.routes) {
            Search(navController)
        }

        composable(Routes.Profile.routes) {
            Profile(navController)
        }

        composable(Routes.AddThread.routes) {
            AddThreads(navController)
        }

        composable(Routes.BottomNav.routes) {
            BottomNav(navController)
        }

        composable(Routes.Login.routes) {
            Login(navController)
        }

        composable(Routes.Login.routes) {
            Register(navController)
        }

        composable(Routes.OtherUsers.routes) {
           val data = it.arguments!!.getString("data")
            OtherUsers(navController, data!!)
        }

        //3:26

    }
}