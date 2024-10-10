package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSearchResponse {

    private final String title;
    private final Long managerCount;
    private final Long commentsCount;

    public TodoSearchResponse(String title, Long managerCount, Long commentsCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentsCount = commentsCount;
    }

    public String title(){
        return title;
    }

    public Long managerCount(){
        return managerCount;
    }

    public Long commentsCount(){
        return commentsCount;
    }


}
