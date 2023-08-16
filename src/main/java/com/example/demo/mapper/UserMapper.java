package com.example.demo.mapper;
import com.example.demo.model.User;
import com.example.demo.dto.UserDto;
import com.example.demo.Constants;
import com.example.demo.utils.CollectionUtils;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class UserMapper {
    public static final UserDto mapToDto(User user){
        final UserDto userDto = new UserDto() ;
        if (Objects. isNull (user)) {
            return userDto;}
        userDto.setId(user.getId());
        userDto.setUsername (user.getUsername()) ;
        //userDto.setIsAdmin(user.getEnabled());
        userDto.setIsAdmin(user.getRoles().stream().anyMatch(role -> role.getName().equals(Constants.Roles.ADMIN)));
        return userDto;

    }
    public static final List<UserDto> mapToDto(List<User> userlist){
        if(CollectionUtils.isEmpty(userlist)){
            return Collections.emptyList();
        }
        return userlist.stream().map(user -> mapToDto(user)).toList();


    }
}

