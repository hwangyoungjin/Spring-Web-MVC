package com.example.springbootsecurity.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account createAccont(String username, String password){
        Account accout = new Account();
        accout.setUsername(username);
        accout.setPassword(passwordEncoder.encode(password));
        return accountRepository.save(accout);//만들쪽에서 받아서 사용가능하도록 return
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //spring security 로그인 처리시 호줄되는 메서드
        //로그인시 입력하는 id가 매개변수 username으로 들어와
        //username에 해당하는 user정보를 확인해서 해당하는 password와 입력한 pw가 같은지 확인
        //같다면 로그인처리, 다르다면 예외던진다. 해당 예외를 처리하면서 비번 틀리다는 메시지 보낼 수 있다.

        //Optional 클래스는 null이나 null이 아닌 값을 담을 수 있는 클래스
        Optional<Account> byUsername = accountRepository.findByUsername(username);

        //찾은 username이 존재하지 않는 다면 UsernameNotFoundException 예외발생시키기, 존재하면 account 타입으로 받기
        Account account = byUsername.orElseThrow(()->new UsernameNotFoundException(username));

        //UserDetails 타입으로 return을 해야한다.
        //스프링시큐리티에서 UserDetails타입의 기본구현체를 User라는 이름으로 제공한다.
        //이를통해 인증된 객체를 UserDetails타입으로 변환하여여리턴
        //cf. UserDetails 인터페이스를 상속받아 커스텀 가능하다.
        return new User(account.getUsername(),account.getPassword(),authorities());
    }

    private Collection<? extends GrantedAuthority> authorities() {
        //이러한 권한을 가진 유저라는것을 셋팅
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
