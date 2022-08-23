package api.retrofit.vehicle;

import api.calls.VehicleCalls;
import api.mappings.vehicle.Vehicle;
import api.retrofit.RetrofitBuilder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.util.*;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class VehicleRequests {
    private static final VehicleCalls vehicleCalls = new RetrofitBuilder().getRetrofit().create(VehicleCalls.class);

    @SneakyThrows()
    public static Response<List<Vehicle>> getVehicles() {
        return vehicleCalls.getVehicles().execute();
    }

    @SneakyThrows()
    public static Response<Vehicle> getVehicle(Integer vehicleId) {
        return vehicleCalls.getVehicle(vehicleId).execute();
    }
    
    @SneakyThrows()
    public static Response<ResponseBody> deleteVehicle(Integer vehicleId) {
        return vehicleCalls.deleteVehicle(vehicleId).execute();
    }
    
    @SneakyThrows()
    public static Response<Integer> createVehicle(Vehicle newVehicle){
        return vehicleCalls.createVehicle(newVehicle).execute();
    }

    @SneakyThrows()
    public static Response<Integer> updateVehicle(Integer vehicleId, Vehicle newVehicleData){
        return vehicleCalls.updateVehicle(vehicleId, newVehicleData).execute();
    }

    @SneakyThrows()
    public static Response<ResponseBody> associateVehicleToClient(Integer vehicleId, Integer clientId){
        return vehicleCalls.associateVehicleToClient(vehicleId, clientId).execute();
    }

    @SneakyThrows()
    public static Response<ResponseBody> removeVehicleFromClient(Integer vehicleId){
        return vehicleCalls.removeVehicleFromClient(vehicleId).execute();
    }
}
