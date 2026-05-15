package com.example.grama_vaxihealthcare.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Register : Screen("register")
    object Home : Screen("home")
    object Animals : Screen("animals")
    object AddAnimal : Screen("add_animal")
    object AnimalDetail : Screen("animal_detail/{animalId}") {
        fun createRoute(animalId: Long) = "animal_detail/$animalId"
    }
    object Calendar : Screen("calendar")
    object Inbox : Screen("inbox")
    object Alerts : Screen("alerts")
    object DiseaseReport : Screen("disease_report")
    object Profile : Screen("profile")
}
