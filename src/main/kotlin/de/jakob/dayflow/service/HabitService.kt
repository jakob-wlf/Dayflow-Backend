package de.jakob.dayflow.service

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

    fun createHabit(habit: Habit): Habit = habitRepository.save(habit)

    fun getHabitsForUser(user: User): List<Habit> = habitRepository.findAllByUser(user)

    fun updateHabit(habit: Habit): Habit = habitRepository.save(habit)

    fun deleteHabit(id: Long) = habitRepository.deleteById(id)

    fun completeHabit(habit: Habit): Habit {
        val today = LocalDate.now()

        // Check if already completed today
        if (habit.completions.any { it.date == today }) return habit

        // Add a completion record
        val completion = HabitCompletion(habit = habit, date = today)
        habit.completions.add(completion)

        // Update streak
        updateStreak(habit)

        return habitRepository.save(habit)
    }

    fun completeHabit(habit: Habit, date: LocalDate): Habit {
        // Check if already completed today
        if (habit.completions.any { it.date == date }) return habit

        // Add a completion record
        val completion = HabitCompletion(habit = habit, date = date)
        habit.completions.add(completion)

        // Update streak
        updateStreak(habit)

        return habitRepository.save(habit)
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
        // Go through each completion and check continuity
        for (i in 1 until completions.size) {
            val current = completions[i]
            val daysBetween = ChronoUnit.DAYS.between(current, lastDate)

            if (daysBetween == 1L) {
                streak++
                lastDate = current
            } else if (daysBetween > 1L) {
                break // streak broken
            }
        }

        habit.streak = streak
        habit.lastCompletedDate = completions.first()
    }


    fun getCompletionHistory(habit: Habit): List<LocalDate> =
        habit.completions.map { it.date }

    fun getHabitCompletionMap(habit: Habit, startDate: LocalDate, endDate: LocalDate): Map<LocalDate, Boolean> {
        val completionsSet = habit.completions.map { it.date }.toSet()
        val daysMap = mutableMapOf<LocalDate, Boolean>()
        var date = startDate
        while (!date.isAfter(endDate)) {
            daysMap[date] = completionsSet.contains(date)
            date = date.plusDays(1)
        }
        return daysMap
    }


}
