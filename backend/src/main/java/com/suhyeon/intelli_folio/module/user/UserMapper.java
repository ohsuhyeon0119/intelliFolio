package com.suhyeon.intelli_folio.module.user;

import com.suhyeon.intelli_folio.module.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByEmail(@Param("email") String email);
    int insert(@Param("email") String email,
               @Param("passwordHash") String passwordHash,
               @Param("displayName") String displayName);
    Long lastInsertId();
}
