package api.mappings.client;

import api.mappings.vehicle.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Client {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String country;
    private Integer phoneNumber;
    private Integer nif;
    private String birthDate;
    private String clientDate;
    private List<Vehicle> vehicles;

    public Client clone(){
        return Client.builder()
                .address(this.address)
                .birthDate(this.birthDate)
                .city(this.city)
                .postalCode(this.postalCode)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .id(this.id)
                .country(this.country)
                .phoneNumber(this.phoneNumber)
                .nif(this.nif)
                .clientDate(this.clientDate)
                .vehicles(this.vehicles)
                .build();
    }
}
