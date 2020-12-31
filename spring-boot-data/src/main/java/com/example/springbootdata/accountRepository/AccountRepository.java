package com.example.springbootdata.accountRepository;

import com.example.springbootdata.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUsername(String username);
}
