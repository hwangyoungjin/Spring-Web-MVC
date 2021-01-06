package com.example.springbootsecurity;

import com.example.springbootsecurity.account.Account;
import com.example.springbootsecurity.account.AccountRepository;
import com.example.springbootsecurity.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountRunner implements ApplicationRunner {

    @Autowired
    private AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account young = accountService.createAccont("young","1234");
        System.out.println(young.getUsername()+"password: "+ young.getPassword());
    }
}
