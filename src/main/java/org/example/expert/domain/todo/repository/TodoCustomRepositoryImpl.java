package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@AllArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public TodoResponse getTodo(Long todoId) {
        return jpaQueryFactory
                .select(Projections.constructor(TodoResponse.class,
                        todo.id,
                        todo.title,
                        todo.contents,
                        todo.weather,
                        Projections.constructor(UserResponse.class, // UserResponse 객체 생성
                                user.id,
                                user.email,
                                user.nickname
                        ),
                        todo.createdAt,
                        todo.modifiedAt
                ))
                .from(todo)
                .leftJoin(todo.user, user)
//                .fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne();
    }

    @Override
    public Page<TodoSearchResponse> searchGetTodos(String title,
                                                    LocalDateTime startDate,
                                                    LocalDateTime endDate,
                                                    String nickname,
                                                    Pageable pageable) {
    QTodo todo = QTodo.todo;
    QUser user = QUser.user;
    QComment comment = QComment.comment;

    BooleanExpression predicate = todo.isNotNull();

    // 검색 조건 처리
    if (title != null) {
        predicate = predicate.and(todo.title.containsIgnoreCase(title));
    }
    if (startDate != null && endDate != null) {
        predicate = predicate.and(todo.createdAt.between(startDate, endDate));
    } else if (startDate != null) {
        predicate = predicate.and(todo.createdAt.goe(startDate));
    } else if (endDate != null) {
        predicate = predicate.and(todo.createdAt.loe(endDate));
    }
    if (nickname != null) {
        predicate = predicate.and(user.nickname.containsIgnoreCase(nickname));
    }

    // Projections를 사용해 필요한 필드만 반환
    List<TodoSearchResponse> results = jpaQueryFactory
            .select(Projections.constructor(TodoSearchResponse.class,
                    todo.title,
                    todo.user.count(),  // 담당자 수
                    comment.count()))        // 댓글 수
            .from(todo)
            .leftJoin(todo.user, user)
            .leftJoin(todo.comments, comment)
            .where(predicate)
            .groupBy(todo.id)
            .orderBy(todo.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    // 총 검색 결과 수
    long total = jpaQueryFactory
            .select(todo.count())
            .from(todo)
            .where(predicate)
            .fetchOne();

    return new PageImpl<>(results, pageable, total);
    }

}
