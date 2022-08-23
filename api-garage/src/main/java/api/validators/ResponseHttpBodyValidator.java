package api.validators;

import api.mappings.client.Client;
import api.mappings.vehicle.Vehicle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseHttpBodyValidator {
    public static void assertVehicle(Vehicle vehicleBody, Vehicle expectedVehicleBody){
        assertThat("Id is not the expected", vehicleBody.getId(), is(expectedVehicleBody.getId()));
        assertThat("Client is not the expected", vehicleBody.getClient(), is(expectedVehicleBody.getClient()));
        assertThat("Brand is not the expected", vehicleBody.getBrand(), is(expectedVehicleBody.getBrand()));
        assertThat("Model is not the expected", vehicleBody.getModel(), is(expectedVehicleBody.getModel()));
        assertThat("Type is not the expected", vehicleBody.getType(), is(expectedVehicleBody.getType()));
        assertThat("Plate is not the expected", vehicleBody.getPlate(), is(expectedVehicleBody.getPlate()));
        assertThat("Year is not the expected", vehicleBody.getYear(), is(expectedVehicleBody.getYear()));
        assertThat("Active state is not the expected", vehicleBody.getActive(), is(expectedVehicleBody.getActive()));
    }

    public static void assertClient(Client clientBody, Client expectedClientBody){
        assertThat("Id is not the expected", clientBody.getId(), is(expectedClientBody.getId()));
        assertThat("First Name is not the expected", clientBody.getFirstName(), is(expectedClientBody.getFirstName()));
        assertThat("Last Name is not the expected", clientBody.getLastName(), is(expectedClientBody.getLastName()));
        assertThat("Client Date is not the expected", clientBody.getClientDate(), is(expectedClientBody.getClientDate()));
        assertThat("Birth Date is not the expected", clientBody.getBirthDate(), is(expectedClientBody.getBirthDate()));
        assertThat("NIF is not the expected", clientBody.getNif(), is(expectedClientBody.getNif()));
        assertThat("Phone Number is not the expected", clientBody.getPhoneNumber(), is(expectedClientBody.getPhoneNumber()));
        assertThat("Country is not the expected", clientBody.getCountry(), is(expectedClientBody.getCountry()));
        assertThat("City is not the expected", clientBody.getCity(), is(expectedClientBody.getCity()));
        assertThat("Address is not the expected", clientBody.getAddress(), is(expectedClientBody.getAddress()));
        assertThat("Postal Code is not the expected", clientBody.getPostalCode(), is(expectedClientBody.getPostalCode()));
        assertThat("Vehicles is not the expected", clientBody.getVehicles(), is(expectedClientBody.getVehicles()));
    }
}
