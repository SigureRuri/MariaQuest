package com.github.shur.mariaquest.extension

fun Long.secondsToDaysAndHoursAndMinutesAndSeconds(): String {
    val days = this / (60 * 60 * 24)
    val hours = this / (60 * 60)
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    val result = StringBuilder()
    if (days != 0.toLong()) {
        result.append("${days}日")
    }
    if (hours != 0.toLong() || days != 0.toLong()) {
        result.append("${hours}時間")
    }
    if (minutes != 0.toLong() || hours != 0.toLong() || days != 0.toLong()) {
        result.append("${minutes}分")
    }
    result.append("${seconds}秒")
    return result.toString()
}