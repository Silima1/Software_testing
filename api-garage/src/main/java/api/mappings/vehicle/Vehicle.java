package api.mappings.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Vehicle {
    private Integer id;
    private Integer client;
    private String brand;
    private String model;
    private Integer year;
    private String type;
    private String plate;
    private Boolean active;
    
    public Vehicle clone(){
      return Vehicle.builder()
                .active(this.active)
                .model(this.model)
                .brand(this.brand)
                .type(this.type)
                .plate(this.plate)
                .year(this.year)
                .client(this.client)
              .id(this.id)
                .build();
    }
}
