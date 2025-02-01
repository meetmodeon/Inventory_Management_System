package com.meet.service;

import com.meet.dto.LoginRequest;
import com.meet.dto.RegisterRequest;
import com.meet.dto.Response;
import com.meet.dto.UserDto;
import com.meet.entity.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getCurrentLoggedInUser();
    Response updateUser(Long id, UserDto userDto);

    Response deleteUser(Long id);
    Response getUserTransaction(Long id);
}
