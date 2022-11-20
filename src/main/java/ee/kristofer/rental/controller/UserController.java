package ee.kristofer.rental.controller;

import ee.kristofer.rental.model.UserReservationsRequest;
import ee.kristofer.rental.serviceImpl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@ApiImplicitParams({
        @ApiImplicitParam(name = AUTHORIZATION, value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class)})
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping(value="/reservations", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getReservations(
            @Valid @RequestBody UserReservationsRequest userReservationsRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(userService.getReservations(userReservationsRequest,page,size));
    }
}
