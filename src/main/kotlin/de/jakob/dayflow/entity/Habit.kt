package de.jakob.dayflow.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "habits")
data class Habit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    var frequency: String, // e.g., "daily", "weekly"

    var completedToday: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @OneToMany(mappedBy = "habit", cascade = [CascadeType.ALL], orphanRemoval = true)
    var completions: MutableList<HabitCompletion> = mutableListOf(),


    var streak: Int = 0, // consecutive days the habit was completed
    var lastCompletedDate: LocalDate? = null // to track streak continuity

)
