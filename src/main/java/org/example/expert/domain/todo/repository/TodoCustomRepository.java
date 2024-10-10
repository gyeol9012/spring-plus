package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TodoCustomRepository {
    TodoResponse getTodo(Long todoId);
    Page<TodoSearchResponse> searchGetTodos(
            String title, LocalDateTime startDate,LocalDateTime endDate, String nickname , Pageable pageable);
}
