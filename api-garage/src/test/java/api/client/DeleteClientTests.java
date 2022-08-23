package api.client;

import api.mappings.client.Client;
import api.mappings.generic.ErrorResponse;
import okhttp3.ResponseBody;
import org.testng.annotations.Test;
import retrofit2.Response;

import static api.retrofit.client.ClientRequests.deleteClient;
import static api.retrofit.client.ClientRequests.getClient;
import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpCodeValidator.assertNoContent;
import static api.validators.ResponseHttpCodeValidator.assertNotFound;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

public class DeleteClientTests extends ClientDeleteTestBase {

    @Test(description = "check if client is correctly deleted")
    public void deleteClientTest() {
        Integer clientIdToCheck = ClientDeleteTestBase.clients.get(0);
        
        Response<ResponseBody> response = deleteClient(clientIdToCheck);
        assertNoContent(response);

        //remove client from list
        ClientDeleteTestBase.clients.remove(clientIdToCheck);
        
        //check if can get client after delete (should not)
        Response<Client> getResponse = getClient(clientIdToCheck);
        assertNotFound(getResponse);
    }
    
    @Test(description = "check if client delete error is correct")
    public void deleteClientErrorTest() {
        Integer clientIdToCheck = ClientDeleteTestBase.deletedClients.get(0);

        Response<ResponseBody> response = deleteClient(clientIdToCheck);
        assertNotFound(response);

        ErrorResponse error = getErrorsResponse(response);
        assertErrorResponse(error, "Client not found", String.format("/client/%d", clientIdToCheck), HTTP_NOT_FOUND, "Not Found");
    }    
}
