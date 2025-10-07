package de.jakob.dayflow.service

import de.jakob.dayflow.entity.DayPlan
import de.jakob.dayflow.entity.Todo
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.DayPlanRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DayPlanService(
    private val dayPlanRepository: DayPlanRepository
) {

    fun createOrUpdateDayPlan(user: User, date: LocalDate, todos: List<Todo>): DayPlan {
        val existingPlan = dayPlanRepository.findByUserAndDate(user, date)
        return if (existingPlan != null) {
            existingPlan.todos = todos.toMutableList()
            dayPlanRepository.save(existingPlan)
        } else {
            val newPlan = DayPlan(date = date, user = user, todos = todos.toMutableList())
            dayPlanRepository.save(newPlan)
        }
    }

    fun getDayPlan(user: User, date: LocalDate): DayPlan? =
        dayPlanRepository.findByUserAndDate(user, date)

    fun getAllPlans(user: User): List<DayPlan> =
        dayPlanRepository.findAllByUser(user)
}
