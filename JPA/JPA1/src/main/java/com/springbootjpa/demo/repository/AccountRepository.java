package com.springbootjpa.demo.repository;

import com.springbootjpa.demo.model.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class AccountRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Account addAccount(Account account){
        entityManager.persist(account);
        return account; //저장된 객체가 return된다.
    }

    public void delete(Account account){
        entityManager.remove(account);
    }

    public List<Account> findAll(){
        return entityManager.createQuery(
                "select a from Account as a",
                Account.class).getResultList();
        // Account 테이블에서 account를 찾는 쿼리로
        // 결과를 list로 return
    }
}
