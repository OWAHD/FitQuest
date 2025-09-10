package com.example.fitquest.ui.screens.utils

import java.lang.StringBuilder
import kotlin.random.Random

object StringUtils{
    fun generateUserId(length: Int): String {
        return generateRandomString(length)
    }

    private fun generateRandomString(length: Int): String {

        val randomString = StringBuilder()

        for (i in 1..length){
            val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            randomString.append(alphabet[Random.nextInt(alphabet.length)])
        }
        return randomString.toString()
    }
}
