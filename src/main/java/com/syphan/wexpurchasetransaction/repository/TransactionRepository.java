package com.syphan.wexpurchasetransaction.repository;

import com.syphan.wexpurchasetransaction.model.entity.Transaction;
import com.syphan.wexpurchasetransaction.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUser(UUID id, User user);

    Page<Transaction> findAllByUser(User user, Pageable pageable);
}
