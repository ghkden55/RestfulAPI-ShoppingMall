package com.example.product.user;

import com.example.product.error.exception.Exception400;
import com.example.product.error.exception.Exception401;
import com.example.product.security.CustomUserDetails;
import com.example.product.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public User join(UserRequest.JoinDTO requestDTO, Error error) {

        //  동일한 email이 존재하는 지 확인.
        Optional<User> byEmail =  userRepository.findByEmail(requestDTO.getEmail());

        // email이 존재한다면 error를 발생
        if(byEmail.isPresent()) {
            throw new Exception400("이미 존재하는 email입니다." + requestDTO.getEmail());
        }

        // password encoding
        String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);

        return userRepository.save(requestDTO.toEntity());

    }


    public String login(UserRequest.JoinDTO requestDTO, Error error) {

        String jwt = "";

        // ** 인증 작업.
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());

            // anonymousUser = 미인증
            Authentication authentication =  authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken);

            // ** 인증 완료 값을 받아온다.
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();

            // ** 토큰 발급.
            jwt = JwtTokenProvider.create(customUserDetails.getUser());

        } catch (Exception exception) {
            // 401 반환.
            throw new Exception401("인증 되지 않음.");
        }

        return jwt;

    }


}
