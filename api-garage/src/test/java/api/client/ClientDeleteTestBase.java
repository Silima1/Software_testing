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

public class ClientDeleteTestBase extends BaseData {
    // this is used to know all clients that exist in database during test enviroment
    public static List<Integer> clients = new ArrayList<>();
    // this is used to know all clients that do not exist in database during test enviroment
    public static List<Integer> deletedClients = new ArrayList<>();
    // this is used to know all vehicles that exist in database during test enviroment
    public static List<Integer> vehicles = new ArrayList<>();
    // this is used to know all clients that do not have vehicles
    public static List<Integer> clientsWithoutVehicles = new ArrayList<>();
    // this is used to know all clients that have vehicles (used for delete with vehicles)
    public static List<Integer> clientsWithVehicles = new ArrayList<>();

    @BeforeClass
    public void createTestingEntities() {
        Integer entityId;

        //entities that will exist in database to perform tasks
        entityId = createEntity(createClientRequest, baseClient);
        this.clients.add(entityId);
        this.clientsWithoutVehicles.add(entityId);
        entityId = createEntity(createClientRequest, baseClient);
        this.clients.add(entityId);
        this.clientsWithoutVehicles.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        this.vehicles.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        this.vehicles.add(entityId);

        //entities that wont exists (for tasks like not found error)
        entityId = createEntity(createClientRequest, baseClient);
        deleteEntity(deleteClientRequest, entityId);
        this.deletedClients.add(entityId);

        //clients with vehicles
        Integer clientId = this.clients.get(0);
        Integer vehicleId = this.vehicles.get(0);
        Response<ResponseBody> response = associateVehicleToClient(vehicleId, clientId);
        assertNoContent(response);
        this.clientsWithoutVehicles.remove(clientId);
        this.clientsWithVehicles.add(clientId);
    }

    @AfterClass
    public void deleteTestingEntities() {
        for (Integer clientId : this.clients) {
            deleteEntity(deleteClientRequest, clientId);
        }
        for (Integer vehicleId : this.vehicles) {
            deleteEntity(deleteVehicleRequest, vehicleId);
        }
    }
}
