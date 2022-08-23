package api.vehicle;

import api.TestsBase;
import api.mappings.client.Client;
import api.mappings.generic.ErrorResponse;
import api.mappings.vehicle.Vehicle;
import okhttp3.ResponseBody;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import retrofit2.Response;

import static api.retrofit.client.ClientRequests.getClient;
import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.retrofit.vehicle.VehicleRequests.*;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpCodeValidator.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DisjoinVehicleWithClientTests extends TestsBase {
    Integer vehicleToReassociate = -1;
    Integer clientToReassociate = -1;
    
    @AfterMethod
    public void reassociateVehiclesToClients(){
        if(vehicleToReassociate != -1 && clientToReassociate != -1){
            associateVehicleToClient(vehicleToReassociate, clientToReassociate);
        }
        vehicleToReassociate = -1;
        clientToReassociate = -1;
    }
    
    @Test(description = "check if vehicle is correctly desassociated from client")
    public void desassociateVehicleFromClientTest() {
        Integer vehicleIdToCheck = TestsBase.vehiclesWithClients.get(0);

        Response<Vehicle> getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        Vehicle vehicleData = getVehicleResponse.body();
        Integer clientIdToCheck = vehicleData.getClient();

        Response<Client> getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        Client clientData = getClientResponse.body();

        Integer initialQuantityOfVehicles = clientData.getVehicles().size();

        //check that client is associated to vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), is(clientIdToCheck));
        assertThat("client.vehicles is not the expected", clientData.getVehicles().toArray(), hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck))));

        Response<ResponseBody> response = removeVehicleFromClient(vehicleIdToCheck);
        assertNoContent(response);
        vehicleToReassociate = vehicleIdToCheck;
        clientToReassociate = clientIdToCheck;

        //get updated data
        getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        vehicleData = getVehicleResponse.body();

        getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        clientData = getClientResponse.body();

        //check that client is desassociated from vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), nullValue());
        assertThat("client.vehicles is not the expected", clientData.getVehicles().toArray(), not(hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck)))));
        assertThat("client.vehicles size is not the expected", clientData.getVehicles().toArray(), arrayWithSize(initialQuantityOfVehicles - 1));
    }

    @Test(description = "check if vehicle is correctly desassociated from no one (is not associated to a client already)")
    public void desassociateVehicleFromNoOneTest() {
        Integer vehicleIdToCheck = TestsBase.vehiclesWithoutClients.get(0);

        Response<Vehicle> getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        Vehicle vehicleData = getVehicleResponse.body();

        //check that client is associated to vehicle
        assertThat("client is not the expected", vehicleData.getClient(), nullValue());
        
        Response<ResponseBody> response = removeVehicleFromClient(vehicleIdToCheck);
        assertNoContent(response);

        //get updated data
        getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        vehicleData = getVehicleResponse.body();

        //check that client is desassociated from vehicle
        assertThat("client is not the expected", vehicleData.getClient(), nullValue());
    }

    @Test(description = "check if vehicle desassociation error is correct")
    public void desassociateVehicleFromClientErrorTest() {
        Integer vehicleIdToCheck = TestsBase.deletedVehicles.get(0);
        Response<ResponseBody> response = removeVehicleFromClient(vehicleIdToCheck);
        assertNotFound(response);

        ErrorResponse error = getErrorsResponse(response);
        assertErrorResponse(error, "Vehicle not found", String.format("/vehicle/%d/client", vehicleIdToCheck), HTTP_NOT_FOUND, "Not Found");
    }
}
