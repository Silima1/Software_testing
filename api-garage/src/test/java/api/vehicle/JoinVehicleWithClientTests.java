package api.vehicle;

import api.TestsBase;
import api.mappings.client.Client;
import api.mappings.vehicle.Vehicle;
import okhttp3.ResponseBody;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import retrofit2.Response;

import static api.retrofit.client.ClientRequests.getClient;
import static api.retrofit.vehicle.VehicleRequests.*;
import static api.validators.ResponseHttpCodeValidator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JoinVehicleWithClientTests extends TestsBase {
    private Integer vehicleRemoveClient = -1;


    @AfterMethod()
    public void deleteTestData() {
        //clean up data after each test (prevent data to stay created if test fails
        if (vehicleRemoveClient != -1) {
            removeVehicleFromClient(vehicleRemoveClient);
            vehicleRemoveClient = -1;
        }
    }

    @Test(description = "check if vehicle is correctly associated to client")
    public void associateVehicleToClientTest() {
        Integer vehicleIdToCheck = TestsBase.vehiclesWithoutClients.get(0);
        Integer clientIdToCheck = TestsBase.clientsWithoutVehicles.get(0);

        Response<Vehicle> getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        Vehicle vehicleData = getVehicleResponse.body();

        Response<Client> getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        Client clientData = getClientResponse.body();

        Integer initialQuantityOfVehicles = clientData.getVehicles().size();

        //check that client is not associated to vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), nullValue());
        assertThat("client.vehicles is not the expected", clientData.getVehicles().toArray(), not(hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck)))));

        Response<ResponseBody> response = associateVehicleToClient(vehicleIdToCheck, clientIdToCheck);
        assertNoContent(response);
        vehicleRemoveClient = vehicleIdToCheck;

        //get updated data
        getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        vehicleData = getVehicleResponse.body();

        getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        clientData = getClientResponse.body();

        //check that client is associated to vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), is(clientIdToCheck));
        assertThat("client.vehicles is not the expected", clientData.getVehicles().toArray(), hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck))));
        assertThat("client.vehicles size is not the expected", clientData.getVehicles().toArray(), arrayWithSize(initialQuantityOfVehicles + 1));
    }

    @Test(description = "check if vehicle is correctly associated to same client")
    public void reassociateVehicleToClientTest() {
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

        Response<ResponseBody> response = associateVehicleToClient(vehicleIdToCheck, clientIdToCheck);
        assertNoContent(response);
        vehicleRemoveClient = vehicleIdToCheck;

        //get updated data
        getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        vehicleData = getVehicleResponse.body();

        getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        clientData = getClientResponse.body();

        //check that client is still associated to vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), is(clientIdToCheck));
        assertThat("client.vehicles is not the expected", clientData.getVehicles().toArray(), hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck))));
        assertThat("client.vehicles size is not the expected", clientData.getVehicles().toArray(), arrayWithSize(initialQuantityOfVehicles));
    }

    @Test(description = "check if error is correct when association vehicle to client")
    public void associateVehicleToClientErrorTest() {
        Integer vehicleIdToCheck = TestsBase.vehiclesWithoutClients.get(0);
        Integer deletedVehicleIdToCheck = TestsBase.deletedVehicles.get(0);
        Integer clientIdToCheck = TestsBase.clientsWithoutVehicles.get(0);
        Integer deletedClientIdToCheck = TestsBase.deletedClients.get(0);

        Response<ResponseBody> response = associateVehicleToClient(vehicleIdToCheck, deletedClientIdToCheck);
        assertNotFound(response);

        response = associateVehicleToClient(deletedVehicleIdToCheck, clientIdToCheck);
        assertNotFound(response);

        response = associateVehicleToClient(deletedVehicleIdToCheck, deletedClientIdToCheck);
        assertNotFound(response);
    }

    @Test(description = "check if change of owner is correctly done")
    public void associateVehicleToOtherClientTest() {
        Integer vehicleIdToCheck = TestsBase.vehiclesWithClients.get(0);
        Integer clientIdToCheck = TestsBase.clientsWithoutVehicles.get(0);

        Response<Vehicle> getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        Vehicle vehicleData = getVehicleResponse.body();
        Integer oldClientIdToCheck = vehicleData.getClient();

        Response<Client> getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        Client clientData = getClientResponse.body();

        getClientResponse = getClient(oldClientIdToCheck);
        assertOk(getClientResponse);
        Client oldClientData = getClientResponse.body();

        Integer newClient_initialQuantityOfVehicles = clientData.getVehicles().size();
        Integer oldClient_initialQuantityOfVehicles = oldClientData.getVehicles().size();

        //check that client is not associated to vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), is(oldClientIdToCheck));
        assertThat("oldClient.vehicles is not the expected", oldClientData.getVehicles().toArray(), hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck))));
        assertThat("newClient.vehicles is not the expected", clientData.getVehicles().toArray(), not(hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck)))));

        //associate vehicle to new client
        Response<ResponseBody> response = associateVehicleToClient(vehicleIdToCheck, clientIdToCheck);
        assertNoContent(response);

        //get updated data
        getVehicleResponse = getVehicle(vehicleIdToCheck);
        assertOk(getVehicleResponse);
        vehicleData = getVehicleResponse.body();

        getClientResponse = getClient(clientIdToCheck);
        assertOk(getClientResponse);
        clientData = getClientResponse.body();

        getClientResponse = getClient(oldClientIdToCheck);
        assertOk(getClientResponse);
        oldClientData = getClientResponse.body();

        //check that client is not associated to vehicle
        assertThat("vehicle.client is not the expected", vehicleData.getClient(), is(clientIdToCheck));
        assertThat("newCLient.vehicles is not the expected", clientData.getVehicles().toArray(), hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck))));
        assertThat("oldClient.vehicles is not the expected", oldClientData.getVehicles().toArray(), not(hasItemInArray(hasProperty("id", equalTo(vehicleIdToCheck)))));
        assertThat("newCLient.vehicles size is not the expected", clientData.getVehicles().toArray(), arrayWithSize(newClient_initialQuantityOfVehicles + 1));
        assertThat("oldClient.vehicles size is not the expected", oldClientData.getVehicles().toArray(), arrayWithSize(oldClient_initialQuantityOfVehicles - 1));
    }
}
