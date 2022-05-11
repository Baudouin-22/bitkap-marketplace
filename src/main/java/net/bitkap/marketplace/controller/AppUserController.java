package net.bitkap.marketplace.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="AppUserController", description="Rest API for App User operations.")
@RestController
@CrossOrigin
@RequestMapping(value = "/v1/user")
public class AppUserController {

    @ApiOperation(value = "Test the method.", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Validation Exception"),
            @ApiResponse(code = 404, message = "Resource Not Found Exception"),
            @ApiResponse(code = 500, message = "Internal Exception")

    })
    @GetMapping(value="/test")
    public String testMethod(){
        return "test réussi!";
    }
}
