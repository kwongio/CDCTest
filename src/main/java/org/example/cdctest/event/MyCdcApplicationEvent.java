package org.example.cdctest.event;

import lombok.Getter;
import org.example.cdctest.model.MyModel;
import org.example.cdctest.model.OperationType;
import org.springframework.context.ApplicationEvent;

@Getter
public class MyCdcApplicationEvent extends ApplicationEvent {

    private final Integer id;
    private final MyModel myModel;
    private final OperationType operationType;

    public MyCdcApplicationEvent(Object source, Integer id, MyModel myModel, OperationType operationType) {
        super(source);
        this.id = id;
        this.myModel = myModel;
        this.operationType = operationType;
    }
}
