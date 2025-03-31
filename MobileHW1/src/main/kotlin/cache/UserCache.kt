package cache

import models.User

object UserCache {
    private val cachedUsers = mutableMapOf<String, User>()

    fun addUser(user: User) {
        cachedUsers[user.login] = user
    }

    fun getUser(username: String): User? = cachedUsers[username]

    fun getAllUsers(): List<User> = cachedUsers.values.toList()

    fun searchByUsername(query: String): List<User> =
        cachedUsers.values.filter { it.login.contains(query, ignoreCase = true) }

    fun searchByRepoName(query: String): List<User> =
        cachedUsers.values.filter { user ->
            user.repositories.any { repo ->  // Changed from public_repos to repositories
                repo.name.contains(query, ignoreCase = true)
            }
        }
}