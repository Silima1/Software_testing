package api.vehicle;

import api.TestsBase;
import api.mappings.generic.ErrorResponse;
import api.mappings.vehicle.Vehicle;
import lombok.SneakyThrows;
import org.testng.annotations.Test;
import retrofit2.Response;

import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.retrofit.vehicle.VehicleRequests.getVehicle;
import static api.retrofit.vehicle.VehicleRequests.updateVehicle;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpBodyValidator.assertVehicle;
import static api.validators.ResponseHttpCodeValidator.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class UpdateVehicleTests extends TestsBase {
    private enum BadRequests {
        invalidPlate,
        invalidYear,
        idNotNull,
        clientNotNull
    }

    @SneakyThrows
    public void validateUpdateVehicleBadRequest(Response<Integer> updateResponse, BadRequests expectedMessage, Object usedValue) {
        Integer vehicleIdToCheck = vehicles.get(0);

        assertBadRequest(updateResponse);
        ErrorResponse error = getErrorsResponse(updateResponse);

        String message = "";
        switch (expectedMessage) {
            case invalidPlate:
                message = "Invalid Plate";
                break;

            case invalidYear:
                message = "Invalid Year";
                break;

            case idNotNull:
                message = "Id on body should be null on Update";
                break;

            case clientNotNull:
                message = "Client on body should be null on Update";
                break;

            default:
                throw new Exception("BadRequest type unsuported");
        }

        assertErrorResponse(error, message, String.format("/vehicle/%d", vehicleIdToCheck), HTTP_BAD_REQUEST, "Bad Request", usedValue);
    }

    @Test(description = "check if vehicle update updates all data")
    public void updateVehicleDataTest() {
        Integer vehicleIdToCheck = vehicles.get(0);

        //get current vehicle data
        Response<Vehicle> getResponse = getVehicle(vehicleIdToCheck);
        assertOk(getResponse);
        Vehicle vehicleData = getResponse.body();

        Vehicle newVhicleData = Vehicle.builder()
                .active(!vehicleData.getActive())
                .model(vehicleData.getModel() + "_dummy")
                .brand(vehicleData.getBrand() + "_dummy")
                .type(vehicleData.getType() + "_dummy")
                .plate("22-22-AA")
                .year(vehicleData.getYear() + 1)
                .build();

        Response<Integer> response = updateVehicle(vehicleIdToCheck, newVhicleData);
        assertOk(response);

        getResponse = getVehicle(vehicleIdToCheck);
        assertOk(response);

        Vehicle updatedVehicleData = getResponse.body();

        Vehicle expectedVehicle = newVhicleData.clone();
        expectedVehicle.setId(vehicleIdToCheck);
        expectedVehicle.setClient(vehicleData.getClient());

        assertVehicle(updatedVehicleData, expectedVehicle);
    }

    @Test(description = "check if vehicle update error is correct when vehicle does not exist")
    public void vehicleNotFoundTest() {
        Integer vehicleIdToCheck = deletedVehicles.get(0);

        Response<Integer> response = updateVehicle(vehicleIdToCheck, TestsBase.baseVehicle);
        assertNotFound(response);

        ErrorResponse error = getErrorsResponse(response);

        assertErrorResponse(error, "Vehicle not found", String.format("/vehicle/%d", vehicleIdToCheck), HTTP_NOT_FOUND, "Not Found");
    }

    @Test(description = "check if vehicle plate is correctly validated", dataProvider = "testValidPlates")
    public void updateVehicleValidPlateTest(String plate) {
        Vehicle newVehicleData = TestsBase.baseVehicle.clone();
        Integer vehicleIdToCheck = vehicles.get(0);
        Response<Integer> updateResponse;
        
            newVehicleData.setPlate(plate);
            updateResponse = updateVehicle(vehicleIdToCheck, newVehicleData);
            assertOk(updateResponse);
    }
    
    @Test(description = "check if vehicle plate is correctly validated", dataProvider = "testInvalidPlates")
    public void updateVehicleInvalidPlateTest(String plate) {
        Vehicle newVehicleData = TestsBase.baseVehicle.clone();
        Integer vehicleIdToCheck = vehicles.get(0);
        Response<Integer> updateResponse;
        
            newVehicleData.setPlate(plate);
            updateResponse = updateVehicle(vehicleIdToCheck, newVehicleData);
            validateUpdateVehicleBadRequest(updateResponse, BadRequests.invalidPlate, plate);
    }

    @Test(description = "check if vehicle year is correctly validated", dataProvider = "testYears")
    public void updateVehicleInvalidYearTest(Integer year) {
        Vehicle newVehicleData = TestsBase.baseVehicle.clone();
        Integer vehicleIdToCheck = vehicles.get(0);

        newVehicleData.setYear(year);
        Response<Integer> updateResponse = updateVehicle(vehicleIdToCheck, newVehicleData);
        validateUpdateVehicleBadRequest(updateResponse, BadRequests.invalidYear, year);
    }

    @Test(description = "check if vehicle id is correctly validated")
    public void updateVehicleIdNotNullTest() {
        Vehicle newVehicleData = TestsBase.baseVehicle.clone();
        Integer vehicleIdToCheck = vehicles.get(0);

        newVehicleData.setId(1);
        Response<Integer> updateResponse = updateVehicle(vehicleIdToCheck, newVehicleData);
        validateUpdateVehicleBadRequest(updateResponse, BadRequests.idNotNull, "id is not null");
    }

    @Test(description = "check if vehicle client is correctly validated")
    public void updateVehicleClientNotNullTest() {
        Vehicle newVehicleData = TestsBase.baseVehicle.clone();
        Integer vehicleIdToCheck = vehicles.get(0);
        
        newVehicleData.setClient(1);
        Response<Integer> updateResponse = updateVehicle(vehicleIdToCheck, newVehicleData);
        validateUpdateVehicleBadRequest(updateResponse, BadRequests.clientNotNull, "client is not null");
    }
}
