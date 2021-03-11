package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LogedinUserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-03-11T16:51:24+0100",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 15.0.2 (AdoptOpenJDK)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public User convertUserPostDTOtoEntity(UserPostDTO userPostDTO) {
        if ( userPostDTO == null ) {
            return null;
        }

        User user = new User();

        user.setName( userPostDTO.getName() );
        user.setPassword( userPostDTO.getPassword() );
        user.setUsername( userPostDTO.getUsername() );
        user.setToken( userPostDTO.getToken() );

        return user;
    }

    @Override
    public User convertLogedinUserPostDTOtoEntity(LogedinUserPostDTO logedinUserPostDTO) {
        if ( logedinUserPostDTO == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( logedinUserPostDTO.getPassword() );
        user.setUsername( logedinUserPostDTO.getUsername() );

        return user;
    }

    @Override
    public UserGetDTO convertEntityToUserGetDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserGetDTO userGetDTO = new UserGetDTO();

        userGetDTO.setName( user.getName() );
        userGetDTO.setId( user.getId() );
        userGetDTO.setUsername( user.getUsername() );
        userGetDTO.setStatus( user.getStatus() );
        userGetDTO.setPassword( user.getPassword() );
        userGetDTO.setDateCreated( user.getDateCreated() );
        userGetDTO.setToken( user.getToken() );

        return userGetDTO;
    }
}
