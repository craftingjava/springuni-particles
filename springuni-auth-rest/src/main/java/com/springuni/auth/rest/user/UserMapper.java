package com.springuni.auth.rest.user;

import com.springuni.auth.domain.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by lcsontos on 5/23/17.
 */
@Mapper
public interface UserMapper {

  User toUser(UserDto userDto);

  @Mappings({
      @Mapping(target = "contactData.email", ignore = true),
      @Mapping(target = "screenName", ignore = true)
  })
  User toUserWithoutEmailAndScreenName(UserDto userDto);

  @Mappings(
      @Mapping(target = "password", ignore = true)
  )
  UserDto toUserDto(User user);


}
