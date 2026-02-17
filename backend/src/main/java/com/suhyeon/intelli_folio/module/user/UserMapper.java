package com.suhyeon.intelli_folio.module.user;

import com.suhyeon.intelli_folio.module.user.domain.User;
import com.suhyeon.intelli_folio.module.user.dto.CreateUserCommand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);
    Long insert(CreateUserCommand cmd);
}
