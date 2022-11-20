package ee.kristofer.rental.controller;

import ee.kristofer.rental.model.*;
import ee.kristofer.rental.serviceImpl.RentServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;

@RestController
@RequestMapping("/rent")
@RequiredArgsConstructor
@ApiImplicitParams({
        @ApiImplicitParam(name = AUTHORIZATION, value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class)})
public class RentController {

    private final RentServiceImpl rentService;

    @PostMapping(value="/start", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StartRentResponse> startRent(@Valid @RequestBody StartRentRequest startRentRequest) {
        return ResponseEntity.ok(rentService.startRent(startRentRequest));
    }

    @PostMapping(value="/end", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EndRentResponse> endRent(@Valid @RequestBody EndRentRequest endRentRequest) {
        return ResponseEntity.ok(rentService.endRent(endRentRequest));
    }
}
