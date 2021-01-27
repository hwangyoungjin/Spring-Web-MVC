package com.springbootjpa.demo.repository;

import com.springbootjpa.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

}
