package com.springbootjpa.demo.repository;

import com.springbootjpa.demo.model.Account;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //슬라이스테스트시 사용하는 스프링부트에서 제공하는 애노테이션
    //데이터 관련 bean만 생성한다
    //h2인메모리를 사용하기 때문에 기존 postgres는 사용x 이므로 영향x
class AccountJpaRepositoryTest {
    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Test
    @DisplayName("객체 저장 후 값 확인하기")
    public void crudRepository(){
        //Given
        Account account = new Account();
        account.setUsername("young");
        assertNull(account.getId());

        //When
        Account newAcoount = accountJpaRepository.save(account);

        //Then
        assertNotNull(newAcoount.getId());

        //when
        List<Account> accounts = accountJpaRepository.findAll();

        //Then
        assertEquals(accounts.size(),1);
        assertTrue((accounts).contains(newAcoount));
    }

    @Test
    @DisplayName("페이징테스트")
    public void pageTest(){
        //Given
        Account account = new Account();
        account.setUsername("youngggggg");
        accountJpaRepository.save(account);

        //when
        Page<Account> page = accountJpaRepository.findAll(PageRequest.of(0,10));
        //db에 있는 account를 0번 페이지부터 받고 각 페이지에는 10개의 Account가 들어있도록

        //then
        assertTrue(page.getTotalElements()==1l); //page안에 들어있는 Account 개수
        assertTrue(page.getNumber()==0); //가져온 현재 page의 번호
        assertTrue(page.getSize()==10); //앞에서 설정한 들어갈 수 있는 개수
        System.out.println("========================");
        System.out.println(page.getTotalElements());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());

    }

    @Test
    @DisplayName("common 쿼리연습")
    public void queryPractice (){
        //Given
        Account account = new Account();
        account.setUsername("young1");
        Account account1 = new Account();
        account1.setUsername("young1");
        accountJpaRepository.save(account);
        accountJpaRepository.save(account1);

        //when
        List<Account> accounts = accountJpaRepository.findByUsernameContainsIgnoreCase("y");
        List<Account> accounts1 = accountJpaRepository.findDistinctAccountByUsernameContainsIgnoreCase("y");
        List<Account> accounts2 = accountJpaRepository.findAccountDistinctByUsernameContainsIgnoreCase("y");
        System.out.println("========accounts==========");
        accounts1.stream().forEach(System.out::println);
        accounts2.stream().forEach(System.out::println);


        //then
        assertTrue(accounts.contains(account));
    }
}