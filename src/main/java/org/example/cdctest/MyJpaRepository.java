package org.example.cdctest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyJpaRepository extends JpaRepository<MyEntity, Integer> { }
