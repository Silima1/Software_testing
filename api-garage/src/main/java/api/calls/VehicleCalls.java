package api.calls;

import api.mappings.vehicle.Vehicle;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface VehicleCalls {
    String ID = "id";
    String CLIENT_ID = "clientId";
    String PATH = "vehicle";


    @GET(PATH)
    Call<List<Vehicle>> getVehicles();

    @GET(PATH + "/{id}")
    Call<Vehicle> getVehicle(@Path(ID) Integer vehicleId);
    
    @DELETE(PATH + "/{id}")
    Call<ResponseBody> deleteVehicle(@Path(ID) Integer vehicleId);
    
    @POST(PATH)
    Call<Integer> createVehicle(@Body Vehicle newVehicle);
    
    @PUT(PATH+ "/{id}")
    Call<Integer> updateVehicle(@Path(ID) Integer vehicleId, @Body Vehicle newVehicleData);

    @PUT(PATH+ "/{id}/client/{clientId}")
    Call<ResponseBody> associateVehicleToClient(@Path(ID) Integer vehicleId, @Path(CLIENT_ID) Integer clientId);

    @DELETE(PATH+ "/{id}/client/")
    Call<ResponseBody> removeVehicleFromClient(@Path(ID) Integer vehicleId);
}
