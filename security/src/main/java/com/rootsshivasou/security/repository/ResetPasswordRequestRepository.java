package com.rootsshivasou.security.repository;

import org.springframework.data.repository.CrudRepository;
import com.rootsshivasou.moduleCommun.model.ResetPasswordRequest;

public interface ResetPasswordRequestRepository extends CrudRepository<ResetPasswordRequest, Integer> {
    // ResetPasswordRequest findByToken(String token);
}