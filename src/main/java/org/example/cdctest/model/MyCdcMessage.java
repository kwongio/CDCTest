package org.example.cdctest.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCdcMessage {
    private int id;
    private Payload payload;
    private OperationType operationType;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private int userId;
        private int userAge;
        private String userName;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    /**
     *  C: before null -> after 0000
     *  U: before 0000 -> after 0000
     *  D: before 0000 -> after null
     */
}
