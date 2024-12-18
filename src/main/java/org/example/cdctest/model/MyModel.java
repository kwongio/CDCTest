package org.example.cdctest.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyModel {
    private final Integer id;
    private final Integer userId;
    private final Integer userAge;
    private final String userName;
    private String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MyModel create(
        Integer userId,
        Integer userAge,
        String userName,
        String content
    ) {
        return new MyModel(
            null,
            userId,
            userAge,
            userName,
            content,
            null,
            null
        );
    }
}
