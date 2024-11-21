package com.example.Proyecto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.example.Proyecto.layouts.RegistrationScreen.navigateToRegistrationScreen
import com.example.Proyecto.layouts.RegistrationScreen.registrationScreen
import com.example.Proyecto.layouts.forgotPasswordScreen.forgotPasswordScreen
import com.example.Proyecto.layouts.forgotPasswordScreen.navigateToForgotPasswordScreen
import com.example.Proyecto.layouts.loginScreen.LoginDestination
import com.example.Proyecto.layouts.loginScreen.loginScreen

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = LoginDestination,
        modifier = modifier
    ){
        loginScreen(
            onLoginSuccess = {
                navController.navigateToMainGraph(
                    navOptions = NavOptions.Builder().setPopUpTo<LoginDestination>(
                        inclusive = true
                    ).build()
                )
            },
            onForgotPassword = navController::navigateToForgotPasswordScreen,
            onRegister = navController::navigateToRegistrationScreen
        )
        forgotPasswordScreen(onBack = navController::navigateUp)
        registrationScreen(onBack = navController::navigateUp)
        mainNavGraph(navController = navController)
    }
}