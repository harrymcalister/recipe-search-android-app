# recipe-search-android-app

An Android app which uses the 'Tasty' API to retrieve recipes based on a user search.
(https://rapidapi.com/apidojo/api/tasty)

Any feedback about the code or architecture is much appreciated!

This app was made in an effort to practice creating a full-stack Android app using Jetpack compose rather than Views and XML Layouts. The goal was to make a recipe search app, which can find you recipes based on an ingredient or keyword. Additionally, any recipes that are of particular interest can be saved to a 'favourites' list and browsed later even without an internet connection.

Currently the main page of the app allows you to search for a recipe. An API call will retrieve related recipes to this search term and list a certain amount in a LazyColumn (configurable via the settings page). Recipes can then be browsed at your leisure, or saved to your list of favourite recipes by clicking the heart icon.

API calls are made to the 'Tasty' API.

Working on this app allowed me to practice using the following features, libraries and principles:

Jetpack Compose
Coil (Compose AsyncImageLoader)
SQL/SQLite3
Retrofit/OKHttp
LiveData
Coroutines
Room
LazyRow/LazyColumn
Jetpack Navigation
ViewModel/MVVM architecture
Respository pattern
