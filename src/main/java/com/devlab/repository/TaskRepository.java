package com.devlab.repository;

import com.devlab.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Task> findRandomTask();
}

