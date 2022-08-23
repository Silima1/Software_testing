package api.calls;

import api.mappings.client.Client;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ClientCalls {
    String ID = "id";
    String PATH = "client";
    
    
    @GET(PATH)
    Call<List<Client>> getClients();

    @GET(PATH + "/{id}")
    Call<Client> getClient(@Path(ID) Integer clientId);
    
    @DELETE(PATH + "/{id}")
    Call<ResponseBody> deleteClient(@Path(ID) Integer clientId);
    
    @POST(PATH)
    Call<Integer> createClient(@Body Client newClient);
    
    @PUT(PATH+ "/{id}")
    Call<Integer> updateClient(@Path(ID) Integer clientId, @Body Client newClientData);
}
