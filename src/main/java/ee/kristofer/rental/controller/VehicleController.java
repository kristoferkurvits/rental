package ee.kristofer.rental.controller;

import ee.kristofer.rental.model.UpdateVehicleRequest;
import ee.kristofer.rental.model.VehicleResponse;
import ee.kristofer.rental.model.Vehicle;
import ee.kristofer.rental.serviceImpl.VehicleServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;

@Log4j2
@RestController
@RequiredArgsConstructor
@ApiImplicitParams({
        @ApiImplicitParam(name = AUTHORIZATION, value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class)})
public class VehicleController {

    private final VehicleServiceImpl vehicleService;

    @PostMapping("/vehicle")
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    @PutMapping("/vehicle")
    public ResponseEntity<VehicleResponse> updateVehicle(@Valid @RequestBody UpdateVehicleRequest vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle));
    }
}
