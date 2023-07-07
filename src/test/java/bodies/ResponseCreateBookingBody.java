package bodies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseCreateBookingBody {
    @JsonProperty("bookingid")
    private Number bookingid;
    @JsonProperty("booking")
    private CreateBookingTestBody booking;
}
