package com.springbootjpa.demo;

import com.springbootjpa.demo.model.Account;
import com.springbootjpa.demo.model.Course;
import com.springbootjpa.demo.model.Student;
import com.springbootjpa.demo.model.Study;
import com.springbootjpa.demo.repository.AccountJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    AccountJpaRepository accountJpaRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Account account = new Account(); // account 객체 생성
        account.setUsername("young");
        account.setPassword("pass");

        Study study = new Study(); // study 객체 생성
        study.setName("study");

        study.setOwner(account); // study 객체에 account 연결
        account.getStudies().add(study); // account 객체에 study 추가


        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.ASC,"String"));

        Study study1 = new Study();
        study1.setOwner(account);
        study1.setName("study1");
        account.getStudies().add(study1);
        entityManager.persist(account);

        System.out.println("============================");
        accountJpaRepository.findAll().forEach(System.out::println);


//        Student student = new Student();
//        Course course = new Course();
//
//        student.getLikedCourses().add(course);
//        course.getLikes().add(student);
//
//        entityManager.persist(course);

    }
}
