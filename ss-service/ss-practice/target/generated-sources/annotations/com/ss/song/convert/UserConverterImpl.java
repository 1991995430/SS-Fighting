package com.ss.song.convert;

import com.ss.song.model.User;
import com.ss.song.vo.CtUser;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-17T17:35:07+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_60 (Oracle Corporation)"
)
public class UserConverterImpl implements UserConverter {

    @Override
    public User toEntity(CtUser ctUser) {
        if ( ctUser == null ) {
            return null;
        }

        User user = new User();

        user.setId( ctUser.getId() );
        user.setName( ctUser.getName() );
        user.setAge( ctUser.getAge() );
        user.setSex( ctUser.getSex() );
        user.setEnglishScore( ctUser.getEnglishScore() );
        user.setMathScore( ctUser.getMathScore() );
        user.setAddress( ctUser.getAddress() );
        user.setSchool( ctUser.getSchool() );
        user.setCity( ctUser.getCity() );
        user.setRegion( ctUser.getRegion() );
        user.setProvince( ctUser.getProvince() );

        return user;
    }

    @Override
    public CtUser toVo(User user) {
        if ( user == null ) {
            return null;
        }

        CtUser ctUser = new CtUser();

        ctUser.setId( user.getId() );
        ctUser.setName( user.getName() );
        ctUser.setAge( user.getAge() );
        ctUser.setSex( user.getSex() );
        ctUser.setEnglishScore( user.getEnglishScore() );
        ctUser.setMathScore( user.getMathScore() );
        ctUser.setAddress( user.getAddress() );
        ctUser.setSchool( user.getSchool() );
        ctUser.setCity( user.getCity() );
        ctUser.setRegion( user.getRegion() );
        ctUser.setProvince( user.getProvince() );

        return ctUser;
    }
}
