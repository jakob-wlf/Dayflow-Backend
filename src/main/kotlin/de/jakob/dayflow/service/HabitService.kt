package de.jakob.dayflow.service

import de.jakob.dayflow.entity.Habit
import de.jakob.dayflow.entity.HabitCompletion
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.HabitRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

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
        val lastDate = habit.completions.maxOfOrNull { it.date }
        habit.streak = if (lastDate == today.minusDays(1)) habit.streak + 1 else 1

        return habitRepository.save(habit)
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
