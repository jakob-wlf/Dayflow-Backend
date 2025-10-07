package de.jakob.dayflow.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "habit_completions")
data class HabitCompletion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id")
    var habit: Habit,

    var date: LocalDate
)
