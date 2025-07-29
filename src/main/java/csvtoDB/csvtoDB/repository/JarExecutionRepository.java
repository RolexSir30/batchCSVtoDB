package csvtoDB.csvtoDB.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import csvtoDB.csvtoDB.model.JarExecution;

public interface JarExecutionRepository extends JpaRepository<JarExecution, Long> {}
