package api.vehicle;

import api.mappings.generic.ErrorResponse;
import api.mappings.vehicle.Vehicle;
import okhttp3.ResponseBody;
import org.testng.annotations.Test;
import retrofit2.Response;

import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.retrofit.vehicle.VehicleRequests.deleteVehicle;
import static api.retrofit.vehicle.VehicleRequests.getVehicle;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpCodeValidator.assertNoContent;
import static api.validators.ResponseHttpCodeValidator.assertNotFound;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class DeleteVehicleTests extends VehicleDeleteTestBase {
    @Test(description = "check if vehicle is correctly deleted")
    public void deleteVehicleTest() {
        Integer vehicleIdToCheck = VehicleDeleteTestBase.vehicles.get(0);

        Response<ResponseBody> response = deleteVehicle(vehicleIdToCheck);
        assertNoContent(response);

        //remove vehicle from list
        VehicleDeleteTestBase.vehicles.remove(vehicleIdToCheck);

        //check if can get vehicle after delete (should not)
        Response<Vehicle> getResponse = getVehicle(vehicleIdToCheck);
        assertNotFound(getResponse);
    }

    @Test(description = "check if vehicle delete error is correct")
    public void deleteVehicleErrorTest() {
        Integer vehicleIdToCheck = VehicleDeleteTestBase.deletedVehicles.get(0);

        Response<ResponseBody> response = deleteVehicle(vehicleIdToCheck);
        assertNotFound(response);

        ErrorResponse error = getErrorsResponse(response);
        assertErrorResponse(error, "Vehicle not found", String.format("/vehicle/%d", vehicleIdToCheck), HTTP_NOT_FOUND, "Not Found");
    }
}
