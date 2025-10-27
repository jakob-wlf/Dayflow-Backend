package de.jakob.dayflow.dto

import java.time.LocalDate

data class HabitResponseDTO (
    val id: Long,
    val name: String,
    val frequency: Long,
    val streak: Int,
    val completions: List<LocalDate>
)