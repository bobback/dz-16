package bodies;

import lombok.*;

import java.time.LocalDate;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingDates {
    private LocalDate checkin;
    private LocalDate checkout;
}
