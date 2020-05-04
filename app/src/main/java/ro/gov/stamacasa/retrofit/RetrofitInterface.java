package ro.gov.stamacasa.retrofit;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ro.gov.stamacasa.retrofit.model.request.UserData;
import ro.gov.stamacasa.retrofit.model.response.form.WsResponseForm;
import ro.gov.stamacasa.retrofit.model.response.versioning.WsResponse;

public interface RetrofitInterface {

    @GET("/AUTOEVAL/boot.php")
    Observable<WsResponse> getVersion();

    @POST("/AUTOEVAL/evaluate.php")
    Single<WsResponseForm> postForm(@Body UserData userData);

    @POST("/AUTOEVAL/evaluate.php")
    Call<WsResponseForm> postFormSimple(@Body UserData userData);
}
