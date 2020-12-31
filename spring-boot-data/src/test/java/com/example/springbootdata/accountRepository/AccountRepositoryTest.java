package com.example.springbootdata.accountRepository;

import com.example.springbootdata.account.Account;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void di(){
        Account account = new Account();
        account.setUsername("young");
        account.setPassword("pass");

        Account newAccount = accountRepository.save(account); // 저장된 객체 return됨
        Assert.assertNotNull(newAccount); // null인지 확인
        System.out.println(newAccount.getUsername());

        Account existingAccount = accountRepository.findByUsername(newAccount.getUsername());
        Assert.assertNotNull(newAccount); //database에 있는지 확인
        System.out.println(existingAccount.getUsername());

        existingAccount = accountRepository.findByUsername("young");
        Assert.assertNotNull(newAccount); //database에 있는지 확인
        System.out.println(existingAccount.getUsername());

        //이상한 값으로 찾아보기 -> result 못찾음
//        existingAccount = accountRepository.findByUsername("aelaksej");
//        Assert.assertNotNull(newAccount); //database에 있는지 확인
//        System.out.println(existingAccount.getUsername());
    }
}