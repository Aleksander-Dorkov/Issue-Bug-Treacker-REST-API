package com.secure.secure_back_end.controllers.users;

import com.secure.secure_back_end.configuration.exceptions.PasswordMissMatchException;
import com.secure.secure_back_end.configuration.exceptions.UserAlreadyExistsException;
import com.secure.secure_back_end.dto.user.binding.UserChangePasswordForm;
import com.secure.secure_back_end.dto.user.binding.UserDeleteAccountForm;
import com.secure.secure_back_end.dto.user.binding.UserRegistrationForm;
import com.secure.secure_back_end.dto.user.view.UserViewModel;
import com.secure.secure_back_end.services.interfaces.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Validated
public class UserController
{
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ApiOperation(value = "action must equal to \"single\" or \"by-authority\" If action equals \"by-authority\" you have to provide authority id. Example GET /users?action=by-authority&id=2")
    public List<UserViewModel> getUsers(@RequestParam("action") @Pattern(regexp = "^single$|^by-authority$") String action,
                                        @RequestParam("id") @Min(1) Long id)
    {
        switch (action)
        {
            case "single":
                return Collections.singletonList(this.userService.getSingleUser(id));
            case "by-authority":
                return this.userService.getAllByAuthority(id);
            default: return new ArrayList<>();
        }
    }

    @PostMapping(value = "/users/register")
    @ApiOperation(value = "creates a new account")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationForm form)
    {
        try
        {
            this.userService.register(form);
        } catch (UserAlreadyExistsException e)
        {
            return new ResponseEntity<>("User Already exists", HttpStatus.CONFLICT);
        }
        this.logger.debug("debug log");
        return new ResponseEntity<>("registered", HttpStatus.OK);
    }

    @DeleteMapping("/users/account/{userId}")
    @ApiOperation(value = "deletes an account if you provided a correct password")
    public ResponseEntity<String> deleteAccount(@Valid @RequestBody UserDeleteAccountForm form,
                                                @PathVariable("userId") @Min(1) Long userId)
    {
        try
        {
            this.userService.lockAccount(form, userId);
        } catch (PasswordMissMatchException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Successful delete", HttpStatus.OK);
    }


    @PutMapping("/users/password/{userId}")
    @ApiOperation(value = "changes the password of the given user")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserChangePasswordForm form,
                                                 @PathVariable("userId") @Min(1) Long userId)
    {
        try
        {
            this.userService.changePassword(form, userId);
        } catch (PasswordMissMatchException e)
        {
            return new ResponseEntity<>("New and Old Passwords do not match", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Successfully changed passwords", HttpStatus.OK);
    }
}
