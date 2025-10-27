package de.jakob.dayflow.service

import de.jakob.dayflow.dto.HabitResponseDTO
import de.jakob.dayflow.entity.Habit
import de.jakob.dayflow.entity.HabitCompletion
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.HabitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class HabitService(
    private val habitRepository: HabitRepository
) {

    fun createHabit(habit: Habit): HabitResponseDTO = mapToResponse(habitRepository.save(habit))

    fun getHabitsForUser(user: User): List<HabitResponseDTO> = habitRepository.findAllByUser(user).map { mapToResponse(it) }

    fun getRawHabitsForUser(user: User): List<Habit> = habitRepository.findAllByUser(user)

    fun updateHabit(habit: Habit): HabitResponseDTO = mapToResponse(habitRepository.save(habit))

    fun deleteHabit(id: Long) = habitRepository.deleteById(id)

    fun completeHabit(habit: Habit): HabitResponseDTO {
        val today = LocalDate.now()

        return completeHabit(habit, today)
    }

    fun completeHabit(habit: Habit, date: LocalDate): HabitResponseDTO {
        // Check if already completed today
        if (habit.completions.any { it.date == date }) return mapToResponse(habit)

        // Add a completion record
        val completion = HabitCompletion(habit = habit, date = date)
        habit.completions.add(completion)

        // Update streak
        updateStreak(habit)

        return mapToResponse(habitRepository.save(habit))
    }

    fun mapToResponse(habit: Habit): HabitResponseDTO {
        return HabitResponseDTO(
            habit.id,
            habit.name,
            habit.frequency,
            habit.streak,
            getCompletionHistory(habit)
        )
    }

    fun unCompleteHabit(habit: Habit): HabitResponseDTO {
        val today = LocalDate.now()

        return unCompleteHabit(habit, today)
    }

    fun unCompleteHabit(habit: Habit, date: LocalDate): HabitResponseDTO {
        // Check if not completed today
        if (!habit.completions.any { it.date == date }) return mapToResponse(habit)

        habit.completions.removeIf { it.date == date }

        // Update streak
        updateStreak(habit)

        return mapToResponse(habitRepository.save(habit))
    }

    fun updateStreak(habit: Habit) {
        val completions = habit.completions
            .map { it.date }
            .sortedDescending()

        if (completions.isEmpty()) {
            habit.streak = 0
            habit.lastCompletedDate = null
            return
        }

        var streak = 1
        var lastDate = completions.first()

        val interval = habit.frequency

        // Go through each completion and check continuity
        for (i in 1 until completions.size) {
            val current = completions[i]
            val daysBetween = ChronoUnit.DAYS.between(current, lastDate)

            if (daysBetween == interval) {
                streak++
                lastDate = current
            } else if (daysBetween > interval) {
                break // streak broken
            }
        }

        habit.streak = streak
        habit.lastCompletedDate = completions.first()
    }


    fun getCompletionHistory(habit: Habit): List<LocalDate> =
        habit.completions.map { it.date }

}
