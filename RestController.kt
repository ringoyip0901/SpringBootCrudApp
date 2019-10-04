package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class EntryPoint {

    @Autowired
    lateinit var todoRepo: TodoRepository

    @CrossOrigin
    @GetMapping("/showAll")
    fun getAllTodos(): List<Todo> = todoRepo.findAll()

    @CrossOrigin
    @PostMapping("/saveItem")
    fun saveItem(@RequestBody todo: Todo): ResponseEntity<HttpStatus> {
        todoRepo.save(todo)
        return ResponseEntity.ok(HttpStatus.ACCEPTED)
    }

    @GetMapping("/items/{id}")
    fun getItemById(@PathVariable(value = "id") todoId: Long): ResponseEntity<Todo> {
        return todoRepo.findById(todoId).map{
            item -> ResponseEntity.ok(item)
        }.orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/title/{title}")
    fun getItemByTitle(@PathVariable(value = "title") todoTitle: String): List<Todo> {
        return todoRepo.findByTitle(todoTitle) //gives you back a LIST
    }

    //findById can return more than one item
    @PutMapping("/items/{id}")
    fun updateItemById(@PathVariable(value = "id") todoId: Long,
            @Valid @RequestBody newItem: Todo): ResponseEntity<Todo> {
        return todoRepo.findById(todoId).map{ existingItem ->
            val updatedItem: Todo = existingItem
                    .copy(title = newItem.title, content = newItem.content)
            ResponseEntity.ok().body(todoRepo.save(updatedItem))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/delete/{id}")
    fun deleteItemById(@PathVariable(value = "id") todoId: Long): ResponseEntity<Void> {
        return todoRepo.findById(todoId).map{
            todoToDelete ->  todoRepo.delete(todoToDelete)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }
}