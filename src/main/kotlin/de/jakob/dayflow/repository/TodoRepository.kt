package de.jakob.dayflow.repository

import de.jakob.dayflow.entity.Todo
import de.jakob.dayflow.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository : JpaRepository<Todo, Long> {
    fun findAllByUser(user: User): List<Todo>
}
