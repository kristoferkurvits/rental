package ee.kristofer.rental.controller;

import ee.kristofer.rental.model.UpdateVehicleRequest;
import ee.kristofer.rental.model.VehicleResponse;
import ee.kristofer.rental.model.CreateVehicleRequest;
import ee.kristofer.rental.serviceImpl.VehicleServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@ApiImplicitParams({
        @ApiImplicitParam(name = AUTHORIZATION, value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class)})
public class VehicleController {

    private final VehicleServiceImpl vehicleService;

    @PostMapping(value="/vehicle", consumes=MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody CreateVehicleRequest createVehicleRequest) {
        return ResponseEntity.ok(vehicleService.createVehicle(createVehicleRequest));
    }

    @PutMapping(value="/vehicle", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleResponse> updateVehicle(@Valid @RequestBody UpdateVehicleRequest vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle));
    }
}
