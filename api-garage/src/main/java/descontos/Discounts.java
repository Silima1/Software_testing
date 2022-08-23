package descontos;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Discounts {

    private Discounts() {
    }

    public static int discountByAge(Integer carYear) {
        int age = LocalDate.now().getYear() - carYear;
        int discount = 0;

        if (age >= 5) {
            discount = 2;
        }
        if (age >= 6 && age < 10) {
            discount = discount + age - 3;
        } else if (age > 10) {
            discount = (discount + age) / 2;
        }
        return discount;
    }

    public static int discountByBrand(String brand) {
        return switch (brand) {
            case "VW" -> 3;
            case "Tesla" -> 5;
            case "BMW" -> 7;
            default -> 0;
        };
    }

    public static int calculateTotalDiscount(Integer carYear, String brand, LocalDate today) {
        int MAX_TOTAL_DISCOUNT = 25;
        int PROMOTION_DAY_DISCOUNT = 5;
        boolean isPromotionDay = today.getDayOfWeek().equals(DayOfWeek.FRIDAY);

        int totalDiscount = discountByAge(carYear) + discountByBrand(brand);

        if (isPromotionDay) {
            totalDiscount = totalDiscount + PROMOTION_DAY_DISCOUNT;
        }

        totalDiscount = Math.min(totalDiscount, MAX_TOTAL_DISCOUNT);

        return totalDiscount;
    }
}
