package org.example.cdctest.service;


import java.util.List;
import org.example.cdctest.model.MyModel;

public interface MyService {

    public List<MyModel> findAll();
    public MyModel findById(Integer id);
    public MyModel save(MyModel model);
    public void delete(Integer id);
}
