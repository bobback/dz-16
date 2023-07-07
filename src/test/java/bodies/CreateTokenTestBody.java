package bodies;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTokenTestBody {
    private String username;
    private String password;
}
