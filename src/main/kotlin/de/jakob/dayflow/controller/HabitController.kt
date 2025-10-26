package de.jakob.dayflow.controller

import de.jakob.dayflow.entity.Habit
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.UserRepository
import de.jakob.dayflow.service.HabitService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User as SecurityUser
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/habits")
class HabitController(
    private val habitService: HabitService,
    private val userRepository: UserRepository
) {

    @GetMapping
    fun getHabits(@AuthenticationPrincipal user: SecurityUser): List<Habit> {
        val appUser = userRepository.findByEmail(user.username).get()
        return habitService.getHabitsForUser(appUser)
    }

    @PostMapping
    fun createHabit(@AuthenticationPrincipal user: SecurityUser, @RequestBody habit: Habit): Habit {
        val appUser = userRepository.findByEmail(user.username).get()
        habit.user = appUser
        return habitService.createHabit(habit)
    }

    @PostMapping("/{id}/complete")
    fun completeHabit(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long
    ): Habit {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.completeHabit(habit)
    }

    // Date must be formatted with format: YYYY-MM-DD
    @PostMapping("/{id}/complete/{date}")
    fun completeHabitForDay(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @PathVariable date: String
    ): Habit {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.completeHabit(habit, LocalDate.parse(date))
    }

    @GetMapping("/{id}/history")
    fun getHabitHistory(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long
    ): List<LocalDate> {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.getCompletionHistory(habit)
    }

    @GetMapping("/{id}/completion-map")
    fun getHabitCompletionMap(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @RequestParam("startDate") startDate: String,
        @RequestParam("endDate") endDate: String
    ): Map<String, Boolean> {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        val map = habitService.getHabitCompletionMap(
            habit,
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        )

        // Convert LocalDate keys to string for JSON serialization
        return map.mapKeys { it.key.toString() }
    }



    @PutMapping("/{id}")
    fun updateHabit(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @RequestBody habit: Habit
    ): Habit {
        val appUser = userRepository.findByEmail(user.username).get()
        val existingHabit = habitService.getHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        existingHabit.name = habit.name
        existingHabit.frequency = habit.frequency
        existingHabit.completedToday = habit.completedToday

        return habitService.updateHabit(existingHabit)
    }

    @DeleteMapping("/{id}")
    fun deleteHabit(@PathVariable id: Long) = habitService.deleteHabit(id)
}
