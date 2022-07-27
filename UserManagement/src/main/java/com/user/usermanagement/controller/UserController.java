package com.user.usermanagement.controller;

import com.user.usermanagement.model.ApplicationUserDTO;
import com.user.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "(A) User Information Manager", description = "User info manager")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get all users",
            description = "To get all users"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Get all users",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ApplicationUserDTO.class)))
            )
        }
    )
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApplicationUserDTO> getAll() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Get user",
            description = "To get a single user",
            tags = "(A) User Registration Manager"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Get a single user",
                    content = @Content(schema = @Schema(implementation = ApplicationUserDTO.class))
            )
        }
    )
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationUserDTO get(@RequestHeader String userId) {
        return userService.getUser(userId);
    }

    @Operation(
            summary = "Delete user",
            description = "To delete a single user",
            tags = "(A) User Registration Manager"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "delete a single user"
                    )
            }
    )
    @DeleteMapping(value = "/user")
    public ResponseEntity<Void> delete(@RequestHeader String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Update user",
            description = "To update a single user",
            tags = "(A) User Registration Manager"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "update a single user"
                    )
            }
    )
    @PatchMapping(value = "/user")
    public ResponseEntity<Void> update(@RequestHeader String userId, @RequestBody @Valid ApplicationUserDTO user) {
        userService.updateUser(userId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO / PATCH

    @PostMapping(value = "/updatedUser")
    public ResponseEntity<Void> updateForTest(@RequestHeader String userId, @RequestBody @Valid ApplicationUserDTO user) {
        userService.updateUser(userId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
