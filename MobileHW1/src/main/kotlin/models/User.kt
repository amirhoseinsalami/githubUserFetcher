package models

data class User(
    val login: String,
    val followers: Int,
    val following: Int,
    val created_at: String,
    val public_repos: Int,  // Changed from List<Repository> to Int
    var repositories: List<Repository> = emptyList()  // Added separate field for repos
)