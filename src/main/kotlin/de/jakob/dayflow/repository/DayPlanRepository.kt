package de.jakob.dayflow.repository

import de.jakob.dayflow.entity.DayPlan
import de.jakob.dayflow.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DayPlanRepository : JpaRepository<DayPlan, Long> {
    fun findByUserAndDate(user: User, date: LocalDate): DayPlan?
    fun findAllByUser(user: User): List<DayPlan>
}
