package aurora.tech.gplanettask.RetrofitPackage;

import aurora.tech.gplanettask.Models.ReadPages;
import aurora.tech.gplanettask.Models.Users;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterfaces {
    @GET("GPlanet/userList2.php")
    Call<Users> getUsers();

    @GET("GPlanet/userReadPages.php")
    Call<ReadPages> getUserReadPages(@Query("userID") int userId);

}
