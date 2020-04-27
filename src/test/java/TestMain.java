import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.AbstractHttpService;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.ServiceRequestContext;
import java.util.concurrent.CompletableFuture;

public class TestMain {

    public static void main(String[] args) {
        ServerBuilder sb = Server.builder();
        sb.http(8080);

        sb.service("/greet/{name}", new AbstractHttpService() {
            @Override
            protected HttpResponse doGet(ServiceRequestContext ctx, HttpRequest req) throws Exception {
                String name = ctx.pathParam("name");
                return HttpResponse.of("Hello, %s!", name);
            }
        });

        Server server = sb.build();
        CompletableFuture<Void> future = server.start();
        future.join();
        
        // address in use and shut down.
    }

}