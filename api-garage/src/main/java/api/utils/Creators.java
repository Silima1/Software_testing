package api.utils;

import api.mappings.client.Client;
import api.mappings.vehicle.Vehicle;
import retrofit2.Response;

import static api.retrofit.client.ClientRequests.createClient;
import static api.retrofit.vehicle.VehicleRequests.createVehicle;
import static api.validators.ResponseHttpCodeValidator.assertCreated;
import static api.validators.ResponseHttpCodeValidator.assertOk;

public class Creators {
    public interface CreateEntityRequest {
        Response<Integer> run(Object entity);
    }
    public static Integer createEntity(CreateEntityRequest createRequest, Object entity){
        Response<Integer> response = createRequest.run(entity);
        assertCreated(response);

        Integer entityId = response.body();
        return entityId;
    }
    public  static CreateEntityRequest createClientRequest = (client) -> createClient((Client)client);
    public static CreateEntityRequest createVehicleRequest = (vehicle) -> createVehicle((Vehicle)vehicle);
}
