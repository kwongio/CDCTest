package org.example.cdctest.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.cdctest.conumser.MyCdcProducer;
import org.example.cdctest.model.MyModelConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class MyCdcApplicationEventListener {

    private final MyCdcProducer myCdcProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void transactionalEventListenerAfterCommit(MyCdcApplicationEvent event) throws JsonProcessingException {
        // 여기가 실패하면 producer에는 데이터가 없고, DB에는 데이터가 들어감
        myCdcProducer.sendMessage(
            MyModelConverter.toMessage(event.getId(), event.getMyModel(), event.getOperationType())
        );
    }
}
