package ru.netology.util

import kotlin.math.floor

fun Int.displayFormat(): String {
    return when {
        ((this in 1000..9999) && (((this / 100) % 10) == 0)) -> (this / 1000).toString() + "K"
        (this in 1000..9999 && (((this / 100) % 10) != 0)) -> ((floor((this / 1000.0).toFloat() * 10.0)) / 10.0).toString() + "K"
        (this in 10000..999999) -> (this / 1000).toString() + "K"
        ((this > 999_999) && (((this / 100_000) % 10) == 0)) -> (this / 1_000_000).toString() + "M"
        ((this > 999_999) && (((this / 100_000) % 10) != 0)) -> ((floor(
            (
                    this / 1_000_000.0).toFloat() * 10.0
        )) / 10.0).toString() + "M"
        else -> this.toString()
    }
}