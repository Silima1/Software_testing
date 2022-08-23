package api.vehicle;

import api.BaseData;
import api.mappings.generic.ErrorResponse;
import api.mappings.vehicle.Vehicle;
import lombok.SneakyThrows;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.retrofit.vehicle.VehicleRequests.*;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpBodyValidator.assertVehicle;
import static api.validators.ResponseHttpCodeValidator.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;

public class CreateVehicleTests extends BaseData {
    private List<Integer> vehiclesToDelete;

    private enum BadRequests {
        invalidPlate,
        invalidYear,
        idNotNull,
        clientNotNull
    }
    
    @BeforeMethod()
    public void resetData(){
        vehiclesToDelete = new ArrayList();
    }

    @AfterMethod()
    public void cleanData() {
        //clean up data after each test (prevent data to stay created if test fails
        for (Integer vehicleToDelete: vehiclesToDelete ) {
            deleteVehicle(vehicleToDelete);
        }
    }

    @Test(description = "check if vehicle is correctly created")
    public void createVehicleTest() {
        Response<Integer> createResponse = createVehicle(this.baseVehicle);
        assertCreated(createResponse);
        Integer vehicleToDelete = createResponse.body();
        vehiclesToDelete.add(vehicleToDelete);

        Response<Vehicle> response = getVehicle(vehicleToDelete);
        assertOk(response);
        Vehicle vehicle = response.body();

        Vehicle expectedVehicle = this.baseVehicle.clone();
        expectedVehicle.setId(vehicleToDelete);

        assertVehicle(vehicle, expectedVehicle);
    }

    @SneakyThrows
    public void validateCreateVehicleBadRequest(Response<Integer> createResponse, BadRequests expectedMessage, Object usedValue) {
        if (createResponse.code() == HTTP_CREATED) {
            //make sure that if vehicle is created for some reason, its deleted after test
            Integer vehicleToDelete = createResponse.body();
            vehiclesToDelete.add(vehicleToDelete);
        }

        assertBadRequest(createResponse);
        ErrorResponse error = getErrorsResponse(createResponse);

        String message = "";
        switch (expectedMessage) {
            case invalidPlate:
                message = "Invalid Plate";
                break;

            case invalidYear:
                message = "Invalid Year";
                break;
                
            case idNotNull:
                message = "Id should be null on creation";
                break;

            case clientNotNull:
                message = "Client should be null on creation";
                break;

            default:
                throw new Exception("BadRequest type unsuported");
        }

        assertErrorResponse(error, message, "/vehicle", HTTP_BAD_REQUEST, "Bad Request", usedValue);
    }

    @Test(description = "check if vehicle plate is correctly validated", dataProvider = "testValidPlates")
    public void createVehicleValidPlateTest(String plate) {
        Vehicle vehicleData = this.baseVehicle.clone();
        Response<Integer> createResponse;
        
            vehicleData.setPlate(plate);
            createResponse = createVehicle(vehicleData);
            assertCreated(createResponse);
            vehiclesToDelete.add(createResponse.body());
    }
    
    @Test(description = "check if vehicle plate is correctly validated", dataProvider = "testInvalidPlates")
    public void createVehicleInvalidPlateTest(String plate) {
        Vehicle vehicleData = this.baseVehicle.clone();
        Response<Integer> createResponse;
        
            vehicleData.setPlate(plate);
            createResponse = createVehicle(vehicleData);
            validateCreateVehicleBadRequest(createResponse, BadRequests.invalidPlate, plate);
    }

    @Test(description = "check if vehicle year is correctly validated", dataProvider = "testYears")
    public void createVehicleInvalidYearTest(Integer year) {
        Vehicle vehicleData = this.baseVehicle.clone();

        vehicleData.setYear(year);
        Response<Integer> createResponse = createVehicle(vehicleData);
        validateCreateVehicleBadRequest(createResponse, BadRequests.invalidYear, year);
    }

    @Test(description = "check if vehicle id is correctly validated")
    public void createVehicleIdNotNullTest() {
        Vehicle vehicleData = this.baseVehicle.clone();

        vehicleData.setId(1);
        Response<Integer> createResponse = createVehicle(vehicleData);
        validateCreateVehicleBadRequest(createResponse, BadRequests.idNotNull, "id is not null");
    }

    @Test(description = "check if vehicle client is correctly validated")
    public void createVehicleClientNotNullTest() {
        Vehicle vehicleData = this.baseVehicle.clone();

        vehicleData.setClient(1);
        Response<Integer> createResponse = createVehicle(vehicleData);
        validateCreateVehicleBadRequest(createResponse, BadRequests.clientNotNull, "client is not null");
    }
}
