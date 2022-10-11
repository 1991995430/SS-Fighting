package com.ss.song.convert;

import com.ss.song.model.User;
import com.ss.song.vo.CtUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    User toEntity(CtUser ctUser);

    CtUser toVo(User user);
}
