package api.client;

import api.mappings.client.Client;
import api.mappings.generic.ErrorResponse;
import api.mappings.vehicle.Vehicle;
import lombok.SneakyThrows;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import retrofit2.Response;

import java.util.ArrayList;

import static api.retrofit.client.ClientRequests.*;
import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.retrofit.vehicle.VehicleRequests.getVehicle;
import static api.validators.ErrorResponseValidator.assertErrorResponse;
import static api.validators.ResponseHttpBodyValidator.assertClient;
import static api.validators.ResponseHttpCodeValidator.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;

public class CreateClientTests extends ClientCreateTestBase {
    private int clientToDelete = -1;

    private enum BadRequests {
        invalidBirthDate,
        invalidClientDate,
        invalidNif,
        invalidPhoneNumber,
        invalidPostalCode,
        idNotNull,
        vehiclesNotNull
    }

    @AfterMethod()
    public void cleanData() {
        //clean up data after each test (prevent data to stay created if test fails
        if (clientToDelete != -1) {
            deleteClient(clientToDelete);
            clientToDelete = -1;
        }
    }

    @Test(description = "check if client is correctly created")
    public void createClientTest() {
        Response<Integer> createResponse = createClient(ClientCreateTestBase.baseClient);
        assertCreated(createResponse);
        clientToDelete = createResponse.body();

        Response<Client> response = getClient(clientToDelete);
        assertOk(response);
        Client client = response.body();

        Client expectedClient = ClientCreateTestBase.baseClient.clone();
        expectedClient.setId(clientToDelete);
        
        assertClient(client, expectedClient);
    }

    @SneakyThrows
    public void validateCreateClientBadRequest(Response<Integer> createResponse, BadRequests expectedMessage, Object usedValue) {
        if (createResponse.code() == HTTP_CREATED) {
            //make sure that if client is created for some reason, its deleted after test
            clientToDelete = createResponse.body();
        }

        assertBadRequest(createResponse);
        ErrorResponse error = getErrorsResponse(createResponse);

        String message = "";
        switch (expectedMessage) {
            case invalidBirthDate:
                message = "Invalid Birth Date";
                break;
            case invalidClientDate:
                message = "Invalid Client Date";
                break;
            case invalidNif:
                message = "Invalid NIF";
                break;
            case invalidPhoneNumber:
                message = "Invalid Phone Number";
                break;
            case invalidPostalCode:
                message = "Invalid Postal Code";
                break;
            case idNotNull:
                message = "Id should be null on creation";
                break;
            case vehiclesNotNull:
                message = "Vehicles should be null on creation";
                break;

            default:
                throw new Exception("BadRequest type unsuported");
        }

        assertErrorResponse(error, message, "/client", HTTP_BAD_REQUEST, "Bad Request", usedValue);
    }
    
    @Test(description = "check if client birth Date is correctly validated", dataProvider = "testDates")
    public void createClientInvalidBirthDateTest(String birthDate) {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Response<Integer> createResponse;

        clientData.setBirthDate(birthDate);
        createResponse = createClient(clientData);
        validateCreateClientBadRequest(createResponse, BadRequests.invalidBirthDate, birthDate);
    }

    @Test(description = "check if client Client Date is correctly validated", dataProvider = "testDates")
    public void createClientInvalidClientDateTest(String clientDate) {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Response<Integer> createResponse;
        
            clientData.setClientDate(clientDate);
            createResponse = createClient(clientData);
            validateCreateClientBadRequest(createResponse, BadRequests.invalidClientDate, clientDate);
    }

    @Test(description = "check if client nif is correctly validated", dataProvider = "testNifs")
    public void createClientInvalidNIFTest(Integer nif) {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Response<Integer> createResponse;

            clientData.setNif(nif);
            createResponse = createClient(clientData);
            validateCreateClientBadRequest(createResponse, BadRequests.invalidNif, nif);
    }

    @Test(description = "check if client phone number is correctly validated", dataProvider = "testPhoneNumbers")
    public void createClientInvalidPhoneNumberTest(Integer phoneNumber) {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Response<Integer> createResponse;
        
            clientData.setPhoneNumber(phoneNumber);
            createResponse = createClient(clientData);
            validateCreateClientBadRequest(createResponse, BadRequests.invalidPhoneNumber, phoneNumber);
    }


    @Test(description = "check if client postal code is correctly validated", dataProvider = "testPostalCodes")
    public void createClientInvalidPostalCodeTest(String postalCode) {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Response<Integer> createResponse;
        
            clientData.setPostalCode(postalCode);
            createResponse = createClient(clientData);
            validateCreateClientBadRequest(createResponse, BadRequests.invalidPostalCode, postalCode);
    }


    @Test(description = "check if client id is correctly validated")
    public void createClientIdNotNullTest() {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        clientData.setId(1);
        Response<Integer> createResponse = createClient(clientData);
        validateCreateClientBadRequest(createResponse, BadRequests.idNotNull, "id is not null");
    }

    @Test(description = "check if client vehicles is correctly validated when vehicle belongs to someone else")
    public void createClientVehiclesFromClientsNotNullTest() {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Integer vehicleIdToCheck = ClientCreateTestBase.vehiclesWithClients.get(0);
        Response<Vehicle> response = getVehicle(vehicleIdToCheck);
        assertOk(response);
        Vehicle vehicle = response.body();

        clientData.setVehicles(new ArrayList<>(){{
           add(vehicle);
        }});
        Response<Integer> createResponse = createClient(clientData);
        validateCreateClientBadRequest(createResponse, BadRequests.vehiclesNotNull, "client at vehicles is not null");
    }

    @Test(description = "check if client vehicles is correctly validated when vehicles does not belong to anyone")
    public void createClientVehiclesFromNoOneNotNullTest() {
        Client clientData = ClientCreateTestBase.baseClient.clone();

        Integer vehicleIdToCheck = ClientCreateTestBase.vehiclesWithoutClients.get(0);
        Response<Vehicle> response = getVehicle(vehicleIdToCheck);
        assertOk(response);
        Vehicle vehicle = response.body();

        clientData.setVehicles(new ArrayList<>(){{
            add(vehicle);
        }});
        Response<Integer> createResponse = createClient(clientData);
        validateCreateClientBadRequest(createResponse, BadRequests.vehiclesNotNull, "client at vehicles is null");
    }
}
