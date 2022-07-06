package com.gmail.devpelegrino.pomodorotimer.util

fun Long.getSecondsByMillis(): Int {
    return (this / Constants.MILLISECONDS_TO_ONE_SECOND_LONG).toInt()
}

fun Int.getSecondsInMinuteByTotalSeconds(): Int {
    return this.mod(Constants.SECONDS_TO_ONE_MINUTE)
}

fun Int.getMinuteByTotalSeconds(): Int {
    return this.div(Constants.SECONDS_TO_ONE_MINUTE)
}

fun Int.getSecondsByMinutes(): Int {
    return this.times(Constants.SECONDS_TO_ONE_MINUTE)
}

fun Int.getMillisBySecond(): Long {
    return this.times(Constants.MILLISECONDS_TO_ONE_SECOND_LONG)
}