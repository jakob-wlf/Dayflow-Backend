package de.jakob.dayflow.controller

import de.jakob.dayflow.entity.Todo
import de.jakob.dayflow.entity.User
import de.jakob.dayflow.service.TodoService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User as SecurityUser
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService,
    private val userRepository: de.jakob.dayflow.repository.UserRepository
) {

    @GetMapping
    fun getTodos(@AuthenticationPrincipal user: SecurityUser): List<Todo> {
        val appUser = userRepository.findByEmail(user.username).get()
        return todoService.getTodosForUser(appUser)
    }

    @PostMapping
    fun createTodo(
        @AuthenticationPrincipal user: SecurityUser,
        @RequestBody todo: Todo
    ): Todo {
        val appUser = userRepository.findByEmail(user.username).get()
        todo.user = appUser
        return todoService.createTodo(todo)
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @AuthenticationPrincipal user: SecurityUser,
        @PathVariable id: Long,
        @RequestBody todo: Todo
    ): Todo {
        val appUser = userRepository.findByEmail(user.username).get()

        val existingTodo = todoService.getTodosForUser(appUser).find { it.id == id }
            ?: throw IllegalArgumentException("Todo not found")

        // Update mutable fields
        existingTodo.title = todo.title
        existingTodo.description = todo.description
        existingTodo.dueDate = todo.dueDate
        existingTodo.completed = todo.completed

        return todoService.updateTodo(existingTodo)
    }


    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long) = todoService.deleteTodo(id)
}
