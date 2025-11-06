package com.upc.tukuntechmsiam.iam.application.mapper;


import com.upc.tukuntechmsiam.iam.application.dto.RegisterRequest;
import com.upc.tukuntechmsiam.iam.domain.entity.UserIdentity;

public class UserMapper {

    public static UserIdentity toEntity(RegisterRequest request) {
        UserIdentity user = new UserIdentity();

        // Solo atributos gestionados por IAM
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setEnabled(true);

        return user;
    }
}

