package api.mappings.generic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private Timestamp timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
