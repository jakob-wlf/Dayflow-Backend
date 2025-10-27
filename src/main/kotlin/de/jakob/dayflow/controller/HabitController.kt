package de.jakob.dayflow.controller

import de.jakob.dayflow.dto.HabitResponseDTO
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
    fun getHabits(@AuthenticationPrincipal user: SecurityUser): List<HabitResponseDTO> {
        val appUser = userRepository.findByEmail(user.username).get()
        return habitService.getHabitsForUser(appUser)
    }

    @PostMapping
    fun createHabit(@AuthenticationPrincipal user: SecurityUser, @RequestBody habit: Habit): HabitResponseDTO {
        val appUser = userRepository.findByEmail(user.username).get()
        habit.user = appUser
        return habitService.createHabit(habit)
    }

    @PostMapping("/{id}/complete")
    fun completeHabit(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long
    ): HabitResponseDTO {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getRawHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.completeHabit(habit)
    }

    // Date must be formatted with format: YYYY-MM-DD
    @PostMapping("/{id}/complete/{date}")
    fun completeHabitForDay(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @PathVariable date: String
    ): HabitResponseDTO {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getRawHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.completeHabit(habit, LocalDate.parse(date))
    }

    // Date must be formatted with format: YYYY-MM-DD
    @PostMapping("/{id}/uncomplete/{date}")
    fun unCompleteHabitForDay(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @PathVariable date: String
    ): HabitResponseDTO {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getRawHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.unCompleteHabit(habit, LocalDate.parse(date))
    }

    @PostMapping("/{id}/uncomplete")
    fun unCompleteHabit(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long
    ): HabitResponseDTO {
        val appUser = userRepository.findByEmail(user.username).get()
        val habit = habitService.getRawHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        return habitService.unCompleteHabit(habit)
    }

    @PutMapping("/{id}")
    fun updateHabit(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @RequestBody habit: Habit
    ): HabitResponseDTO {
        val appUser = userRepository.findByEmail(user.username).get()
        val existingHabit = habitService.getRawHabitsForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Habit not found")

        existingHabit.name = habit.name
        existingHabit.frequency = habit.frequency

        return habitService.updateHabit(existingHabit)
    }

    @DeleteMapping("/{id}")
    fun deleteHabit(@PathVariable id: Long) = habitService.deleteHabit(id)
}
