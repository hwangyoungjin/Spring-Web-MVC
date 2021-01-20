package com.springbootjpa.demo;

import com.springbootjpa.demo.model.Account;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@Transactional//모든 operation은 하나의 트랜잭션 안에서 일어나야 하므로 해당 기능 사용
//class, method 레벨에서 사용 가능
public class JpaRunner implements ApplicationRunner {

    @PersistenceContext
    EntityManager entityManager; //jpa의 핵심 (스프링의 핵심인 applicationContext 처럼)
    //해당 클래스를 통해 db 영속화(저장) 가능

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account account = new Account();
        account.setUsername("young");
        account.setPassword("pass");
        entityManager.persist(account);
        Account account2 = new Account();
        account2.setUsername("second");
        account2.setPassword("pass");
        entityManager.persist(account2);
    }
}
