package utils

import models.User

object ConsoleUtils {
    fun displayMenu() {
        println("\n=== GitHub User Info ===")
        println("1. دریافت اطلاعات کاربر")
        println("2. نمایش لیست کاربران موجود")
        println("3. جستجو بر اساس نام کاربری")
        println("4. جستجو بر اساس نام ریپوزیتوری")
        println("5. خروج")
        print("لطفاً گزینه مورد نظر را انتخاب کنید: ")
    }

    fun displayUserInfo(user: User) {
        println("\n=== اطلاعات کاربر ===")
        println("نام کاربری: ${user.login}")
        println("تعداد فالوورها: ${user.followers}")
        println("تعداد فالووینگ: ${user.following}")
        println("تاریخ ایجاد حساب: ${user.created_at}")
        println("تعداد ریپوزیتوری‌ها: ${user.public_repos}")

        if (user.repositories.isNotEmpty()) {
            println("\n=== ریپوزیتوری‌های اخیر ===")
            user.repositories.take(5).forEach { repo ->
                println("- ${repo.name} (⭐ ${repo.stargazers_count})")
            }
        }
    }

    fun displayError(message: String) {
        println("!خطا: $message")
    }
}