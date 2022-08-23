package descontos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountsTest {
    // DESCONTOS POR IDADE

    Integer getYearBaseOnAge(Integer age){
        Integer currentYear = LocalDate.now().getYear();
        return currentYear - age;
    }

    @ParameterizedTest
    @CsvSource({"-5,0", "-4,0", "-3,0", "-2,0", "-1,0", "5,2", "4,0", "5,2", "7,6", "9,8"})
    void discountByAge(int age, int expectedDiscount) {
        int discount = Discounts.discountByAge(getYearBaseOnAge(age));
        assertEquals(expectedDiscount, discount, String.format("Expected discount for a vehicle aged as %d to be %d but was %d", age, expectedDiscount, discount));
    }

// DESCONTOS POR MARCA
    @ParameterizedTest
    @CsvSource({"VW,3", "Tesla,5", "BMW,7", "Toyota,0"})
    void discountByBrand(String brand, int expectedDiscount) {
        int discount = Discounts.discountByBrand(brand);
        assertEquals(expectedDiscount, discount, String.format("Expected discount for brand %s to be %d but was %d", brand, expectedDiscount, discount));
    }

    @ParameterizedTest
    @CsvSource({
            "5,VW, 2022-06-06, 5",
            "0,Toyota, 2022-06-06, 0",
            "100,VW, 2022-06-06, 25"
    })
    void calculateTotalDiscountLimit(int age, String brand, String dateString, int expectedDiscount) {
        int vehicleYear = getYearBaseOnAge(age);
        LocalDate date = LocalDate.parse(dateString);
        
        DayOfWeek weekDay = date.getDayOfWeek();
        int discount = Discounts.calculateTotalDiscount(vehicleYear, brand, date);
        assertEquals(expectedDiscount, discount, String.format("Expected discount for brand %s aged as %d at day %s to be %d but was %d", brand, age, weekDay.toString(), expectedDiscount, discount));
    }
    
    @ParameterizedTest
    @CsvSource({

            "5,Peugeot, 2022-06-06, 2, false", //monday
            "5,Peugeot, 2022-06-07, 2, false", //tuesday 
            "5,Peugeot, 2022-06-08, 2, false", //wednesday
            "5,Peugeot, 2022-06-09, 2, false", //thuesday
            "5,Peugeot, 2022-06-10, 7, true", //friday
            "5,Peugeot, 2022-06-11, 2, false", //saturday
            "5,Peugeot, 2022-06-12, 2, false"  //sunday
    })
    void calculateTotalDiscountPromotionalDay(int age, String brand, String dateString, int expectedDiscount, boolean promotionalDay) {
        int vehicleYear = getYearBaseOnAge(age);
        LocalDate date = LocalDate.parse(dateString);

        DayOfWeek weekDay = date.getDayOfWeek();
        int discount = Discounts.calculateTotalDiscount(vehicleYear, brand, date);
        assertEquals(expectedDiscount, discount, String.format("Expected discount for brand %s aged as %d at %s day %s to be %d but was %d", brand, age, String.format("%spromotional", promotionalDay ? "" : "not "), weekDay.toString(), expectedDiscount, discount));
    }

   }