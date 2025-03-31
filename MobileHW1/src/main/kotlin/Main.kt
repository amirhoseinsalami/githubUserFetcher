import api.GitHubApiService
import utils.ConsoleUtils
import cache.UserCache
import models.User

suspend fun main() {
    val apiService = GitHubApiService.create()
    var running = true

    while (running) {
        ConsoleUtils.displayMenu()
        when (readlnOrNull()) {
            "1" -> fetchUserInfo(apiService)
            "2" -> displayAllUsers()
            "3" -> searchByUsername()
            "4" -> searchByRepoName()
            "5" -> {
                running = false
                println("برنامه خاتمه یافت.")
            }
            else -> println("گزینه نامعتبر!")
        }
    }
}

private suspend fun fetchUserInfo(apiService: GitHubApiService) {
    print("نام کاربری گیت‌هاب را وارد کنید: ")
    val username = readlnOrNull() ?: return

    try {
        // Check cache first
        UserCache.getUser(username)?.let {
            ConsoleUtils.displayUserInfo(it)
            return@let
        }

        // Fetch user data from API
        val userResponse = apiService.getUser(username)
        if (!userResponse.isSuccessful) {
            ConsoleUtils.displayError("کاربر یافت نشد.")
            return
        }

        val user = userResponse.body()!!.apply {
            // Fetch repositories separately
            repositories = apiService.getUserRepos(username).body() ?: emptyList()
        }

        UserCache.addUser(user)
        ConsoleUtils.displayUserInfo(user)
    } catch (e: Exception) {
        ConsoleUtils.displayError("خطا در ارتباط با API: ${e.message}")
    }
}


private fun displayAllUsers() {
    val users = UserCache.getAllUsers()
    if (users.isEmpty()) {
        println("هیچ کاربری در حافظه وجود ندارد.")
        return
    }

    println("\n=== لیست کاربران ===")
    users.forEach { user ->
        println("- ${user.login} (${user.public_repos} ریپوزیتوری)")
    }
}

private fun searchByUsername() {
    print("عبارت جستجو برای نام کاربری: ")
    val query = readlnOrNull() ?: return
    val results = UserCache.searchByUsername(query)
    displaySearchResults(results)
}

private fun searchByRepoName() {
    print("عبارت جستجو برای نام ریپوزیتوری: ")
    val query = readlnOrNull() ?: return
    val results = UserCache.searchByRepoName(query)
    displaySearchResults(results)
}

private fun displaySearchResults(results: List<User>) {
    if (results.isEmpty()) {
        println("نتیجه‌ای یافت نشد.")
        return
    }

    println("\n=== نتایج جستجو ===")
    results.forEach { user ->
        ConsoleUtils.displayUserInfo(user)
        println("---")
    }
}