package com.meet.service.impl;

import com.meet.dto.LoginRequest;
import com.meet.dto.RegisterRequest;
import com.meet.dto.Response;
import com.meet.dto.UserDto;
import com.meet.entity.User;
import com.meet.enums.UserRole;
import com.meet.exceptions.InvalidCredentialException;
import com.meet.exceptions.NotFoundException;
import com.meet.repository.UserRepository;
import com.meet.security.CustomUserDetailsService;
import com.meet.security.JwtUtils;
import com.meet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        UserRole userRole=UserRole.MANAGER;
        if(registerRequest.getRole() != null){
            userRole=registerRequest.getRole();
        }
        User userToSave= User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(userRole)
                .build();
        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("user created successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        try {

            System.out.println(loginRequest.toString());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            final UserDetails userDetails=customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
            System.out.println(userDetails.getUsername());
            User user=userRepository.findByEmail(userDetails.getUsername()).orElseThrow(()->new NotFoundException("Email Not Found"));


            if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
                throw new InvalidCredentialException("Password does not match");
            }
            String token=jwtUtils.generateToken(user.getEmail());
            return Response.builder()
                    .status(200)
                    .message("user logged in successfully")
                    .role(user.getRole())
                    .token(token)
                    .expirationTime("6 month")
                    .build();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidCredentialException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response getAllUsers() {
        List<User> users=userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        users.forEach(user-> user.setTransactions(null));
        List<UserDto> userDtos=modelMapper.map(users,new TypeToken<List<UserDto>>() {}.getType());


        userDtos.forEach(userDto -> userDto.setTransactions(null));
        return Response.builder()
                .status(200)
                .message("success")
                .users(userDtos)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();

        User user= userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User Not found"));
        user.setTransactions(null);

        return user;
    }

    @Override
    public Response updateUser(Long id, UserDto userDto) {
        User existingUser= userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User Not Found"));

        if(userDto.getEmail()!= null) existingUser.setEmail(userDto.getEmail());
        if(userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getPhoneNumber() != null) existingUser.setPhoneNumber(userDto.getPhoneNumber());
        if(userDto.getRole() != null) existingUser.setRole(userDto.getRole());

        if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User Successfully update")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {
       userRepository.findById(id)
               .orElseThrow(()->new NotFoundException("User Not Found"));
       userRepository.deleteById(id);

       return Response.builder()
               .status(200)
               .message("User Successfully Deleted")
               .build();
    }

    @Override
    public Response getUserTransaction(Long id) {
        User user= userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("User Not Found"));

        UserDto userDto= modelMapper.map(user,UserDto.class);
        userDto.getTransactions().forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setSupplier(null);;
        });

        return Response.builder()
                .status(200)
                .user(userDto)
                .message("success")
                .build();
    }
}
