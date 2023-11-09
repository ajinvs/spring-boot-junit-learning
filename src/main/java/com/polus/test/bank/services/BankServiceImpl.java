package com.polus.test.bank.services;

import com.polus.test.bank.modals.Bank;
import com.polus.test.bank.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService{

    @Autowired
    BankRepository bankRepository;

    @Override
    public Bank saveBankDetails(Bank bank) {
        return bankRepository.save(bank);
    }

}
