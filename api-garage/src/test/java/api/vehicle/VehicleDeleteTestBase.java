package api.vehicle;

import api.BaseData;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.ArrayList;
import java.util.List;

import static api.utils.Creators.createEntity;
import static api.utils.Creators.createVehicleRequest;
import static api.utils.Deleters.deleteEntity;
import static api.utils.Deleters.deleteVehicleRequest;

public class VehicleDeleteTestBase extends BaseData {
    // VehicleDeleteTestBase is used to know all vehicles that exist in database during test enviroment
    public static List<Integer> vehicles = new ArrayList<>();
    // VehicleDeleteTestBase is used to know all vehicles that do not exist in database during test enviroment
    public static List<Integer> deletedVehicles = new ArrayList<>();

    @BeforeClass
    public void createTestingEntities() {
        Integer entityId;
        
        entityId = createEntity(createVehicleRequest, baseVehicle);
        VehicleDeleteTestBase.vehicles.add(entityId);
        entityId = createEntity(createVehicleRequest, baseVehicle);
        VehicleDeleteTestBase.vehicles.add(entityId);

        //entities that wont exists (for tasks like not found error)
        entityId = createEntity(createVehicleRequest, baseVehicle);
        deleteEntity(deleteVehicleRequest, entityId);
        VehicleDeleteTestBase.deletedVehicles.add(entityId);
    }

    @AfterClass
    public void deleteTestingEntities() {
        for (Integer vehicleId : VehicleDeleteTestBase.vehicles) {
            deleteEntity(deleteVehicleRequest, vehicleId);
        }
    }
}
