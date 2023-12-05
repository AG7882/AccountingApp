package com.accounting.accountingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.accounting.accountingservice.models.Graph;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Graph, Integer> {
    List<Graph> findByTitleStartingWith(String title);
}