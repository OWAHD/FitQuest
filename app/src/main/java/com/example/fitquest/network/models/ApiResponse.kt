package com.example.fitquest.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("results") val results: List<Exercise>
)

@Serializable
data class Exercise(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String,
    @SerialName("equipment") val equipment: String,
    @SerialName("muscles") val muscles: List<String> = emptyList(),
    @SerialName("description") val description: String,
)