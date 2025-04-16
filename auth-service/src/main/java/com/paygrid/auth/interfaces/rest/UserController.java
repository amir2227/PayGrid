package com.paygrid.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.paygrid.auth.interfaces.rest.UserController.BASE_URL;

@Tag(name = "Users ", description = "Endpoints for user management")
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class UserController {
    public final static String BASE_URL = "/api/v1/user";
}
