package api.vehicle;

import api.TestsBase;
import api.mappings.generic.ErrorResponse;
import api.mappings.vehicle.Vehicle;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.List;

import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.retrofit.vehicle.VehicleRequests.*;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpBodyValidator.assertVehicle;
import static api.validators.ResponseHttpCodeValidator.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetVehicleTests extends TestsBase {
    private int vehicleToDelete = -1;

    @AfterMethod()
    public void cleanData() {
        //clean up data after each test (prevent data to stay created if test fails
        if (vehicleToDelete != -1) {
            deleteVehicle(vehicleToDelete);
            vehicleToDelete = -1;
        }
    }

    @Test(description = "validate number of vehicles")
    public void numberOfVehiclesTest() {
        //get current number of vehicles
        Response<List<Vehicle>> response = getVehicles();
        assertOk(response);
        List<Vehicle> body = response.body();
        int initialSize = body.size();

        //create a dummy vehicle
        Response<Integer> createResponse = createVehicle(TestsBase.baseVehicle);
        assertCreated(createResponse);
        vehicleToDelete = createResponse.body(); //check vehicle to delete

        //get current number of vehicles
        response = getVehicles();
        assertOk(response);
        body = response.body();

        assertThat("quantity of vehicles is not the expected", body.size(), is(initialSize + 1));
    }

    @Test(description = "return vehicle values correctly")
    public void vehicleValuesTest() {
        Integer vehicleIdToCheck = TestsBase.vehiclesWithoutClients.get(0);
        Response<Vehicle> response = getVehicle(vehicleIdToCheck);
        assertOk(response);
        Vehicle vehicle = response.body();

        Vehicle expectedVehicle = TestsBase.baseVehicle.clone();
        expectedVehicle.setId(vehicleIdToCheck);

        assertVehicle(vehicle, expectedVehicle);
    }

    @Test(description = "check error if vehicle does not exist")
    public void vehicleNotFoundTest() {
        Integer vehicleIdToCheck = deletedVehicles.get(0);
        Response<Vehicle> response = getVehicle(vehicleIdToCheck);
        assertNotFound(response);

        ErrorResponse error = getErrorsResponse(response);
        assertErrorResponse(error, "Vehicle not found", String.format("/vehicle/%d", vehicleIdToCheck), HTTP_NOT_FOUND, "Not Found");
    }
}
