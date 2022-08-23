package api.client;

import api.TestsBase;
import api.mappings.client.Client;
import api.mappings.generic.ErrorResponse;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.List;

import static api.retrofit.client.ClientRequests.*;
import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpBodyValidator.assertClient;
import static api.validators.ResponseHttpCodeValidator.*;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetClientTests extends TestsBase {
    private int clientToDelete = -1;

    @AfterMethod()
    public void cleanData() {
        //clean up data after each test (prevent data to stay created if test fails
        if (clientToDelete != -1) {
            deleteClient(clientToDelete);
            clientToDelete = -1;
        }
    }

    @Test(description = "validate number of clients")
    public void numberOfClientsTest() {
        //get current number of clients
        Response<List<Client>> response = getClients();
        assertOk(response);
        List<Client> body = response.body();
        int initialSize = body.size();

        //create a dummy client
        Response<Integer> createResponse = createClient(TestsBase.baseClient);
        assertCreated(createResponse);
        clientToDelete = createResponse.body(); //check client to delete

        //get current number of clients
        response = getClients();
        assertOk(response);
        body = response.body();

        assertThat("quantity of clients is not the expected", body.size(), is(initialSize + 1));
    }

    @Test(description = "return client values correctly")
    public void clientValuesTest() {
        Integer clientIdToCheck = TestsBase.clientsWithoutVehicles.get(0);
        Response<Client> response = getClient(clientIdToCheck);
        assertOk(response);
        Client client = response.body();

        Client expectedClient = TestsBase.baseClient.clone();
        expectedClient.setId(clientIdToCheck);

        assertClient(client, expectedClient);
    }

    @Test(description = "check error if client does not exist")
    public void clientNotFoundTest() {
        Integer clientIdToCheck = TestsBase.deletedClients.get(0);
        Response<Client> response = getClient(clientIdToCheck);
        assertNotFound(response);

        ErrorResponse error = getErrorsResponse(response);
        assertErrorResponse(error, "Client not found", String.format("/client/%d", clientIdToCheck), HTTP_NOT_FOUND, "Not Found");
    }
}
