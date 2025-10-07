package de.jakob.dayflow.repository

import de.jakob.dayflow.entity.Habit
import de.jakob.dayflow.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HabitRepository : JpaRepository<Habit, Long> {
    fun findAllByUser(user: User): List<Habit>
}
