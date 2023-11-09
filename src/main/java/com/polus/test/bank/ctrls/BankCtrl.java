package com.polus.test.bank.ctrls;

import com.polus.test.bank.modals.Bank;
import com.polus.test.bank.repositories.BankRepository;
import com.polus.test.bank.services.BankService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/bank")
public class BankCtrl {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    BankService bankService;

    @GetMapping("/test")
    private ResponseEntity<Object> createBank() {
        Bank bank = new Bank();
        bank.setEmail("icic@icic.com");
        bank.setName("ICIC");
        bankRepository.save(bank);
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity<Object> getAllBankDetails() {
        return new ResponseEntity<>(bankRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{bankId}")
    private ResponseEntity<Object> getBankDetails(@PathVariable("bankId") Integer bankId) {
        return new ResponseEntity<>(bankRepository.findById(bankId), HttpStatus.OK);
    }

    @PostMapping(produces="application/json")
    private ResponseEntity<Bank> saveBank(@RequestBody Bank bank) {
        return new ResponseEntity<>(bankService.saveBankDetails(bank), HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<Object> updateBank(@RequestBody Bank bank) {
        if (bank.getId() == null) {
            throw new ObjectNotFoundException(bank.getId(), "Id must not be null");
        }
        Optional<Bank> responseData = bankRepository.findById(bank.getId());
        if (responseData.isEmpty()) {
            return new ResponseEntity<>("No record found", HttpStatus.NO_CONTENT);
        }
        Bank bankObj = responseData.get();
        bankObj.setName(bank.getName());
        bankObj.setEmail(bank.getEmail());
        bankObj.setPhoneNumber(bank.getPhoneNumber());
        bankObj = bankRepository.save(bankObj);
        return new ResponseEntity<>(bankObj, HttpStatus.OK);
    }
}
