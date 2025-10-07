package de.jakob.dayflow.controller

import de.jakob.dayflow.entity.DayPlan
import de.jakob.dayflow.entity.Todo
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.TodoRepository
import de.jakob.dayflow.repository.UserRepository
import de.jakob.dayflow.service.DayPlanService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User as SecurityUser
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/dayplans")
class DayPlanController(
    private val dayPlanService: DayPlanService,
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository
) {

    @GetMapping("/{date}")
    fun getDayPlan(@AuthenticationPrincipal user: SecurityUser, @PathVariable date: String): DayPlan? {
        val appUser = userRepository.findByEmail(user.username).get()
        return dayPlanService.getDayPlan(appUser, LocalDate.parse(date))
    }

    @PostMapping("/{date}")
    fun createOrUpdateDayPlan(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable date: String,
        @RequestBody todoIds: List<Long>
    ): DayPlan {
        val appUser = userRepository.findByEmail(user.username).get()
        val todos: List<Todo> = todoRepository.findAllById(todoIds)
        return dayPlanService.createOrUpdateDayPlan(appUser, LocalDate.parse(date), todos)
    }

    @GetMapping
    fun getAllPlans(@AuthenticationPrincipal user: SecurityUser): List<DayPlan> {
        val appUser = userRepository.findByEmail(user.username).get()
        return dayPlanService.getAllPlans(appUser)
    }
}
