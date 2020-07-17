package net.alex9849.cocktailmaker.payload.request;

import net.alex9849.cocktailmaker.payload.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UpdateUserRequest {

    private boolean updatePassword;

    @NotNull
    @Valid
    private UserDto userDto;

    public boolean isUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(boolean updatePassword) {
        this.updatePassword = updatePassword;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}