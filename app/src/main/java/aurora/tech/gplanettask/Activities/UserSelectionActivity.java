package aurora.tech.gplanettask.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aurora.tech.gplanettask.Models.Users;
import aurora.tech.gplanettask.R;
import aurora.tech.gplanettask.RetrofitPackage.ApiClient;
import aurora.tech.gplanettask.RetrofitPackage.ApiInterfaces;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSelectionActivity extends AppCompatActivity {
    @BindView(R.id.spinnerForService)
    MaterialSpinner spinnerForService;
    private ApiInterfaces apiInterface;
    private boolean isValiedItemSelectedFromSpinner;
    @BindView(R.id.showDataBtn)
    TextView showDataBtnl;
    private int userID;
    private String userName;
    ArrayList<Integer> usersIds;
    ArrayList<String> usersNames;
    private String selectedItem;
    @BindView(R.id.rootView)
    ConstraintLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        ButterKnife.bind(this);
        if (isNetworkAvailable(UserSelectionActivity.this)) {
            getAllUsersListFromRetrofit();
        } else {
            Snackbar snackbar = Snackbar.make(rootView, "No Internet Connection!", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            spinnerForService.setHint("No Internet Connection");
        }

    }

    private void createSpinner(List<Users.UserData> userDataList) {
        Log.d("TAG", "contains = " + userDataList.size());
        ArrayList<String> spinnerList = new ArrayList<>();
        usersIds = new ArrayList<>();
        usersNames = new ArrayList<>();
        for (int i = 0; i < userDataList.size(); i++) {
            String userdataItem = userDataList.get(i).getId() + " - " + userDataList.get(i).getName();
            usersIds.add(userDataList.get(i).getId());
            usersNames.add(userDataList.get(i).getName());
            spinnerList.add(userdataItem);
            Log.d("TAG", "list number = " + i + " is item name = " + userDataList.get(i).getName());
        }
        for (int i = 0; i < spinnerList.size(); i++) {
            Log.d("TAG", "list number = " + i + " is item name = " + spinnerList.get(i));
        }
        spinnerForService.setItems(spinnerList);
        spinnerForService.setGravity(Gravity.CENTER);
        spinnerForService.setTextSize(13);
        spinnerForService.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (materialSpinner, pos, id, item) -> {
            if (TextUtils.isEmpty(item)) {
                isValiedItemSelectedFromSpinner = false;
            }
            if (!TextUtils.equals(item, getResources().getString(R.string.spinner_hint))) {
                isValiedItemSelectedFromSpinner = true;
                selectedItem = item;

            }
        });
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnected());
        } else {
            return false;
        }
    }

    private void getAllUsersListFromRetrofit() {
        apiInterface = ApiClient.getApiClient().create(ApiInterfaces.class);
        Call<Users> call = apiInterface.getUsers();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "response is successful");
                    List<Users.UserData> userDataList = response.body().getUsersData();
                    if (userDataList != null && !userDataList.isEmpty()) {
                        Log.d("TAG", "list is not empty");
                        for (int i = 0; i < userDataList.size(); i++) {
                            Log.d("TAG", "Name is " + userDataList.get(i).getName() + " and the number of pages is : " + userDataList.get(i).getmNoOfReadPages());
                        }

                        createSpinner(userDataList);
                    } else {
                        Log.d("TAG", "list is empty");
                        spinnerForService.setHint("No Users");
                        isValiedItemSelectedFromSpinner = false;
                    }
                } else {
                    Log.d("TAG", "response is not successful");
                    spinnerForService.setHint("failed to get data");
                    isValiedItemSelectedFromSpinner = false;
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d("onFailure response", t.toString());
                if (isNetworkAvailable(UserSelectionActivity.this)) {
                    spinnerForService.setHint("no Internet Connection");
                } else {
                    spinnerForService.setHint("Server is down");
                }
                isValiedItemSelectedFromSpinner = false;

            }
        });
    }

    @OnClick(R.id.showDataBtn)
    protected void onShowDataBtnClicked() {
        if (isValiedItemSelectedFromSpinner) {
            if (isNetworkAvailable(UserSelectionActivity.this)) {
                Intent i = new Intent(UserSelectionActivity.this, ShowDataActivity.class);
                i.putExtra(getResources().getString(R.string.useridKey), usersIds);
                i.putExtra(getResources().getString(R.string.usernameKey), usersNames);
                i.putExtra(getResources().getString(R.string.selectedItemKey), selectedItem);
                startActivity(i);
            } else {
                Snackbar snackbar = Snackbar.make(rootView, "No Internet connection, Please press retry", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();
            }
        } else {
            Toast.makeText(this, "You must select a user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_retry:
                recreate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
