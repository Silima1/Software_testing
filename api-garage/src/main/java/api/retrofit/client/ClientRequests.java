package api.retrofit.client;

import api.calls.ClientCalls;
import api.mappings.client.Client;
import api.retrofit.RetrofitBuilder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ClientRequests {
    private static final ClientCalls clientCalls = new RetrofitBuilder().getRetrofit().create(ClientCalls.class);

    @SneakyThrows()
    public static Response<List<Client>> getClients() {
        return clientCalls.getClients().execute();
    }

    @SneakyThrows()
    public static Response<Client> getClient(Integer clientId) {
        return clientCalls.getClient(clientId).execute();
    }
    
    @SneakyThrows()
    public static Response<ResponseBody> deleteClient(Integer clientId) {
        return clientCalls.deleteClient(clientId).execute();
    }
    
    @SneakyThrows()
    public static Response<Integer> createClient(Client newClient){
        return clientCalls.createClient(newClient).execute();
    }

    @SneakyThrows()
    public static Response<Integer> updateClient(Integer clientId, Client newClientData){
        return clientCalls.updateClient(clientId, newClientData).execute();
    }
}
