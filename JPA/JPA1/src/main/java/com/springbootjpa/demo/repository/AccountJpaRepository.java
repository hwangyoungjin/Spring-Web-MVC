package com.springbootjpa.demo.repository;

import com.springbootjpa.demo.model.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    List<Account> findByUsernameContainsIgnoreCase(String username);

    List<Account> findAccountDistinctByUsernameContainsIgnoreCase(String username);

    List<Account> findDistinctAccountByUsernameContainsIgnoreCase(String username);
}
