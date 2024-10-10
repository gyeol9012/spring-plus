package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@AllArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Todo findByIdWithUser(Long todoId){
        return jpaQueryFactory
                .selectFrom(todo)
                .where(todo.id.eq(todoId))
                .leftJoin(todo.user, user)
                .fetchJoin()
                .fetchOne();

    }

}
