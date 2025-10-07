package de.jakob.dayflow.service

import de.jakob.dayflow.entity.Todo
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.repository.TodoRepository
import org.springframework.stereotype.Service

@Service
class TodoService(
    private val todoRepository: TodoRepository
) {

    fun createTodo(todo: Todo): Todo = todoRepository.save(todo)

    fun getTodosForUser(user: User): List<Todo> = todoRepository.findAllByUser(user)

    fun updateTodo(todo: Todo): Todo = todoRepository.save(todo)

    fun deleteTodo(id: Long) = todoRepository.deleteById(id)
}
