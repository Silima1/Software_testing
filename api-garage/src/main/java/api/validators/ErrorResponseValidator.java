package api.validators;

import api.mappings.generic.ErrorResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import retrofit2.Response;


import static api.retrofit.generic.Errors.getErrorsResponse;
import static api.validators.ResponseHttpCodeValidator.assertBadRequest;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseValidator {
    public static void assertErrorResponse(ErrorResponse errorResponse, String message, String path, Integer status, String error, Object usedValue) {
        String extraText = "";
        if(usedValue != null){
            extraText = String.format("when using %s", usedValue.toString());
        }
        assertThat(String.format("Message should be %s %s", message, extraText), errorResponse.getMessage(), is(message));
        assertThat(String.format("Path should be %s %s", path, extraText), errorResponse.getPath(), is(path));
        assertThat(String.format("Status should be %s %s", status, extraText), errorResponse.getStatus(), is(status));
        assertThat(String.format("Error should be %s %s", error, extraText), errorResponse.getError(), is(error));
    }

    public static void assertErrorResponse(ErrorResponse errorResponse, String message, String path, Integer status, String error) {
        assertErrorResponse( errorResponse,  message,  path,  status,  error, "");
    }
}
