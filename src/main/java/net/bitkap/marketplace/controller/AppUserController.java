package net.bitkap.marketplace.controller;

import io.swagger.annotations.*;
import net.bitkap.marketplace.maviance.AccessDetails;
import org.maviance.s3pjavaclient.ApiClient;
import org.maviance.s3pjavaclient.ApiException;
import org.maviance.s3pjavaclient.api.HealthcheckApi;
import org.maviance.s3pjavaclient.model.Ping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="AppUserController")
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
        ApiClient apiClient = new ApiClient(AccessDetails.BASE_URL, AccessDetails.ACCESS_TOKEN, AccessDetails.ACCESS_SECRET);

        HealthcheckApi checksApi = new HealthcheckApi(apiClient);

        try {
            Ping ping = checksApi.pingGet(AccessDetails.VERSION);
            System.out.println(ping);
        } catch (ApiException e) {
            System.out.println("An error occurred: \n");
            System.out.println(e.getResponseBody());
        }
        return "test réussi!";
    }
}
