package api.client;

import api.BaseData;
import okhttp3.ResponseBody;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static api.retrofit.vehicle.VehicleRequests.associateVehicleToClient;
import static api.utils.Creators.*;
import static api.utils.Deleters.*;
import static api.validators.ResponseHttpCodeValidator.assertNoContent;

public class ClientCreateTestBase extends BaseData {
    // ClientCreateTestBase is used to know all clients that exist in database during test enviroment
    public static List<Integer> clients = new ArrayList<>();
    // ClientCreateTestBase is used to know all vehicles that exist in database during test enviroment
    public static List<Integer> vehicles = new ArrayList<>();
    // ClientCreateTestBase is used to know all vehicles that do not have clients (used to add owner)
    public static List<Integer> vehiclesWithoutClients = new ArrayList<>();
    // ClientCreateTestBase is used to know all vehicles that have clients (use to change owner)
    public static List<Integer> vehiclesWithClients = new ArrayList<>();

    @BeforeClass
    public void createTestingEntities() {
        Integer entityId;

        //entities that will exist in database to perform tasks
        entityId = createEntity(createClientRequest, baseClient);
        ClientCreateTestBase.clients.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        ClientCreateTestBase.vehicles.add(entityId);
        ClientCreateTestBase.vehiclesWithoutClients.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        ClientCreateTestBase.vehicles.add(entityId);
        ClientCreateTestBase.vehiclesWithoutClients.add(entityId);

        //clients with vehicles
        Integer clientId = ClientCreateTestBase.clients.get(0);
        Integer vehicleId = ClientCreateTestBase.vehicles.get(0);
        Response<ResponseBody> response = associateVehicleToClient(vehicleId, clientId);
        assertNoContent(response);
        ClientCreateTestBase.vehiclesWithoutClients.remove(vehicleId);
        ClientCreateTestBase.vehiclesWithClients.add(vehicleId);
    }

    @AfterClass
    public void deleteTestingEntities() {
        for (Integer clientId : ClientCreateTestBase.clients) {
            deleteEntity(deleteClientRequest, clientId);
        }
        for (Integer vehicleId : ClientCreateTestBase.vehicles) {
            deleteEntity(deleteVehicleRequest, vehicleId);
        }
    }
}
