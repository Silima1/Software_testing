package api.utils;

import api.mappings.client.Client;
import api.mappings.vehicle.Vehicle;
import api.retrofit.client.ClientRequests;
import api.retrofit.vehicle.VehicleRequests;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static api.retrofit.client.ClientRequests.createClient;
import static api.retrofit.client.ClientRequests.deleteClient;
import static api.retrofit.vehicle.VehicleRequests.createVehicle;
import static api.retrofit.vehicle.VehicleRequests.deleteVehicle;
import static api.validators.ResponseHttpCodeValidator.assertNoContent;
import static api.validators.ResponseHttpCodeValidator.assertOk;

public class Deleters {
    public interface DeleteEntityRequest {
        Response<ResponseBody> run(Integer entityId);
    }
    public static void deleteEntity(DeleteEntityRequest deleteRequest, Integer entityId){
        Response<ResponseBody> response = deleteRequest.run(entityId);
        assertNoContent(response);
    }
    public  static DeleteEntityRequest deleteClientRequest = ClientRequests::deleteClient;
    public static DeleteEntityRequest deleteVehicleRequest = VehicleRequests::deleteVehicle;
}
