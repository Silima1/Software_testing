package api;

import api.mappings.client.Client;
import api.mappings.vehicle.Vehicle;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;

public class BaseData {
    @DataProvider(name = "testDates")
    public Object[][] testDates(){
        return new Object[][] {
                {"2022-13-06"},
                {"2022-06-32"},
                {"2022/06/06"},
                {"2022 06 06"},
                {"2022"},
                {"06-06-2022"},
                {"2022-06-06T12:02:56Z"}};
    }

    @DataProvider(name = "testValidPlates")
    public Object[][] testValidPlates(){
        return new Object[][] {
                {"22-22-22"},
                {"AA-22-22"},
                {"AA-AA-22"},
                {"AA-AA-AA"}};
    }

    @DataProvider(name = "testInvalidPlates")
    public Object[][] testInvalidPlates(){
        return new Object[][] {
                {"A2-AA-22"},
                {"22 AA 22"},
                {"22.AA.22"},
                {"22_AA_22"},
                {"22AA22"}};
    }

    @DataProvider(name = "testNifs")
    public Object[][] testNifs(){
        return new Object[][] {
                {1234567890},
                {12345678}};
    }

    @DataProvider(name = "testPhoneNumbers")
    public Object[][] testPhoneNumbers(){
        return new Object[][] {
                {1234567890},
                {12345678}};
    }

    @DataProvider(name = "testPostalCodes")
    public Object[][] testPostalCodes(){
        return new Object[][] {
                {"3500300"},
                {"3500 300"},
                {"300-3500"},
                {"3500-abc"}};
    }

    @DataProvider(name = "testYears")
    public Object[][] testYears(){
        return new Object[][] {
                {-1}};
    }
    
    public static final Client baseClient = Client.builder()
            .address("testing street")
            .clientDate("2022-06-07")
            .birthDate("2022-06-06")
            .city("Barcelona")
            .country("Spain")
            .firstName("Leonel")
            .lastName("Silima")
            .nif(123456789)
            .phoneNumber(969191900)
            .postalCode("3500-300")
            .vehicles(new ArrayList<>())
            .build();

    public static final Vehicle baseVehicle = Vehicle.builder()
            .active(true)
            .model("207")
            .brand("peugeot")
            .type("car")
            .plate("AA-22-22")
            .year(2007)
            .build();
}
