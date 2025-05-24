package com.example.fitsphere.util

object TipUtil {

    private val tips = listOf(
        "💧 Stay hydrated throughout your day.",
        "🧘‍♀️ Take 5 minutes to stretch every hour.",
        "🥗 Eat more greens and whole foods.",
        "🏃 Move at least 30 minutes a day.",
        "😴 Aim for 7–8 hours of sleep each night.",
        "📴 Take a break from screens every hour.",
        "🚶 Try a short walk to boost your energy.",
        "🪑 Don’t sit for more than an hour without moving.",
        "📵 Disconnect for 15 minutes and breathe.",
        "🎯 Set a mini fitness goal and achieve it today!"
    )


    fun generateTip(): String {
        return tips.random()
    }


    fun getAllTips(): List<String> = tips
}
