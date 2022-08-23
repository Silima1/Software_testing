package api;

import okhttp3.ResponseBody;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static api.retrofit.vehicle.VehicleRequests.associateVehicleToClient;
import static api.utils.Creators.*;
import static api.utils.Deleters.*;
import static api.validators.ResponseHttpCodeValidator.assertNoContent;

public class TestsBase extends BaseData {
    // this is used to know all clients that exist in database during test enviroment
    public static List<Integer> clients = new ArrayList<>();
    // this is used to know all clients that do not exist in database during test enviroment
    public static List<Integer> deletedClients = new ArrayList<>();
    // this is used to know all vehicles that exist in database during test enviroment
    public static List<Integer> vehicles = new ArrayList<>();
    // this is used to know all vehicles that do not exist in database during test enviroment
    public static List<Integer> deletedVehicles = new ArrayList<>();
    // this is used to know all clients that do not have vehicles (used to add owner)
    public static List<Integer> clientsWithoutVehicles = new ArrayList<>();
    // this is used to know all vehicles that do not have clients (used to add owner)
    public static List<Integer> vehiclesWithoutClients = new ArrayList<>();
    // this is used to know all vehicles that have clients (use to change owner)
    public static List<Integer> vehiclesWithClients = new ArrayList<>();

    @BeforeSuite
    public void createTestingEntities() {
        Integer entityId;

        //entities that will exist in database to perform tasks
        entityId = createEntity(createClientRequest, baseClient);
        TestsBase.clients.add(entityId);
        TestsBase.clientsWithoutVehicles.add(entityId);
        entityId = createEntity(createClientRequest, baseClient);
        TestsBase.clients.add(entityId);
        TestsBase.clientsWithoutVehicles.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        TestsBase.vehicles.add(entityId);
        TestsBase.vehiclesWithoutClients.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        TestsBase.vehicles.add(entityId);
        TestsBase.vehiclesWithoutClients.add(entityId);

        //entities that wont exists (for tasks like not found error)
        entityId = createEntity(createClientRequest, baseClient);
        deleteEntity(deleteClientRequest, entityId);
        TestsBase.deletedClients.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        deleteEntity(deleteVehicleRequest, entityId);
        TestsBase.deletedVehicles.add(entityId);

        //clients with vehicles
        Integer clientId = TestsBase.clients.get(0);
        Integer vehicleId = TestsBase.vehicles.get(0);
        Response<ResponseBody> response = associateVehicleToClient(vehicleId, clientId);
        assertNoContent(response);
        TestsBase.clientsWithoutVehicles.remove(clientId);
        TestsBase.vehiclesWithoutClients.remove(vehicleId);
        TestsBase.vehiclesWithClients.add(vehicleId);
    }

    @AfterSuite
    public void deleteTestingEntities() {
        for (Integer clientId : TestsBase.clients) {
            deleteEntity(deleteClientRequest, clientId);
        }
        for (Integer vehicleId : TestsBase.vehicles) {
            deleteEntity(deleteVehicleRequest, vehicleId);
        }
    }
}
