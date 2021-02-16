package com.quoteme.qmservice.mapper;

import com.quoteme.qmservice.domain.User;
import com.quoteme.qmservice.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User mapToUser(UserDto userDto);

    UserDto mapToUserDto(User user);

}
