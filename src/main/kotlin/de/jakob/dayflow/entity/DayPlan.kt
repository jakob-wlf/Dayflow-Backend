package de.jakob.dayflow.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "day_plans")
data class DayPlan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var date: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToMany
    @JoinTable(
        name = "dayplan_todos",
        joinColumns = [JoinColumn(name = "dayplan_id")],
        inverseJoinColumns = [JoinColumn(name = "todo_id")]
    )
    var todos: MutableList<Todo> = mutableListOf()
)
