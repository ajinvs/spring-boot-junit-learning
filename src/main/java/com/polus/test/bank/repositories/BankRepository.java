package com.polus.test.bank.repositories;

import com.polus.test.bank.modals.Bank;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BankRepository extends JpaRepository<Bank, Integer> {
}
