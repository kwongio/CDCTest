package org.example.cdctest.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.cdctest.MyEntity;
import org.example.cdctest.MyJpaRepository;
import org.example.cdctest.event.MyCdcApplicationEvent;
import org.example.cdctest.model.MyModel;
import org.example.cdctest.model.MyModelConverter;
import org.example.cdctest.model.OperationType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MyServiceImpl implements MyService {

    private final MyJpaRepository myJpaRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<MyModel> findAll() {
        List<MyEntity> entities = myJpaRepository.findAll();
        return entities.stream().map(MyModelConverter::toModel).toList();
    }

    @Override
    public MyModel findById(Integer id) {
        Optional<MyEntity> entity = myJpaRepository.findById(id);
        return entity.map(MyModelConverter::toModel).orElse(null);
    }

    @Override
    @Transactional
    public MyModel save(MyModel model) {
        OperationType operationType = model.getId() == null ? OperationType.CREATE : OperationType.UPDATE;
        MyEntity entity = myJpaRepository.save(MyModelConverter.toEntity(model));
        MyModel resultModel = MyModelConverter.toModel(entity);
        applicationEventPublisher.publishEvent(
                new MyCdcApplicationEvent(
                        this,
                        resultModel.getId(),
                        resultModel,
                        operationType
                )
        );
        return resultModel;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        myJpaRepository.deleteById(id);
        applicationEventPublisher.publishEvent(
                new MyCdcApplicationEvent(
                        this,
                        id,
                        null,
                        OperationType.DELETE
                )
        );
    }
}
