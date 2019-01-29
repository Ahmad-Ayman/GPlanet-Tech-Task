package aurora.tech.gplanettask.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import aurora.tech.gplanettask.Adapters.UserListAdapter;
import aurora.tech.gplanettask.HelperClasses.ReadPagesHelperClass;
import aurora.tech.gplanettask.Models.ReadPages;
import aurora.tech.gplanettask.Models.Users;
import aurora.tech.gplanettask.R;
import aurora.tech.gplanettask.RetrofitPackage.ApiClient;
import aurora.tech.gplanettask.RetrofitPackage.ApiInterfaces;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDataActivity extends AppCompatActivity {

    private int userId;
    private String userName;
    private String userIDandNameFromIntent;
    @BindView(R.id.percentageValueTV)
    TextView percentageValueTV;
    @BindView(R.id.userOrderValueTV)
    public TextView userOrderValueTV;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.rv_usersList)
    RecyclerView rv_usersList;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.error_icon)
    ImageView error_icon;
    @BindView(R.id.rootView)
    RelativeLayout rootView;
    @BindView(R.id.viewGroupforPercentage)
    LinearLayout viewGroupforPercentage;
    @BindView(R.id.viewGroupforUserOrder)
    LinearLayout viewGroupforUserOrder;
    @BindView(R.id.userListTitleTV)
    TextView userListTitleTV;

    private final static int SHOW_ERROR_FOR_NO_INTERNET = 0;
    private final static int SHOW_ERROR_FOR_WRONG_DATA = 1;
    private final static int SHOW_ERROR_FOR_NO_DATA = 2;
    ReadPagesHelperClass readPagesHelperClass;
    int min, max;
    List<ReadPages.ReadPagesData> readPagesData, readPagesDataSorted, listwithaddedone;
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        ButterKnife.bind(this);
        getDataFromIntent();
        setTitle("User : " + userName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UpdateUI();
        if (isNetworkAvailable(this)) {
            showItems();
            getPercentageOfReadPages();
            getAllUsersListFromRetrofit();
        } else {
            //  percentageValueTV.setText("No Connection");
            showError(SHOW_ERROR_FOR_NO_INTERNET);
            viewGroupforPercentage.setVisibility(View.INVISIBLE);
            viewGroupforUserOrder.setVisibility(View.INVISIBLE);
            userListTitleTV.setVisibility(View.INVISIBLE);
            Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_SHORT);

        }
        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isNetworkAvailable(ShowDataActivity.this)) {
                            getAllUsersListFromRetrofit();
                            rootView.setVisibility(View.VISIBLE);
                        } else {
                            showError(SHOW_ERROR_FOR_NO_INTERNET);
                            Toast.makeText(ShowDataActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        swiperefresh.setRefreshing(false);
                    }
                }
        );


    }

    private void showLoading() {
        rv_usersList.setVisibility(View.INVISIBLE);
        error_icon.setVisibility(View.INVISIBLE);
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void showList() {
        progress_bar.setVisibility(View.INVISIBLE);
        error_icon.setVisibility(View.INVISIBLE);
        rv_usersList.setVisibility(View.VISIBLE);

    }

    private void showItems() {
        viewGroupforPercentage.setVisibility(View.VISIBLE);
        viewGroupforUserOrder.setVisibility(View.VISIBLE);
        userListTitleTV.setVisibility(View.VISIBLE);
    }

    private void showError(int condition) {
        if (condition == SHOW_ERROR_FOR_NO_DATA) {
            rootView.setBackgroundColor(getResources().getColor(R.color.background_error));

            Picasso.get().load(R.drawable.no_results_found).fit().centerInside().into(error_icon);
        } else if (condition == SHOW_ERROR_FOR_NO_INTERNET) {
            rootView.setBackgroundColor(getResources().getColor(R.color.grey5));

            Picasso.get().load(R.drawable.no_intent_connection).fit().centerInside().into(error_icon);

        } else if (condition == SHOW_ERROR_FOR_WRONG_DATA) {
            rootView.setBackgroundColor(getResources().getColor(android.R.color.white));

            Picasso.get().load(R.drawable.wrong_data).fit().centerInside().into(error_icon);
        }
        progress_bar.setVisibility(View.INVISIBLE);
        rv_usersList.setVisibility(View.INVISIBLE);
        error_icon.setVisibility(View.VISIBLE);
    }

    private void UpdateUI() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_usersList.setLayoutManager(manager);
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        userIDandNameFromIntent = intent.getStringExtra(getResources().getString(R.string.selectedItemKey));
        StringTokenizer tokens = new StringTokenizer(userIDandNameFromIntent, "-");
        String userIDasString = tokens.nextToken();// this will contain "ID"
        String userNameasString = tokens.nextToken();// this will contain "Name"
        userId = Integer.parseInt(userIDasString.trim());
        userName = userNameasString.trim();
    }

    private void getPercentageOfReadPages() {

        ApiInterfaces apiInterface = ApiClient.getApiClient().create(ApiInterfaces.class);
        Call<ReadPages> call = apiInterface.getUserReadPages(userId);

        call.enqueue(new Callback<ReadPages>() {
            @Override
            public void onResponse(Call<ReadPages> call, Response<ReadPages> response) {
                if (response.isSuccessful()) {
                    readPagesData = response.body().getReadPagesData();
                    if (readPagesData != null && !readPagesData.isEmpty()) {
//                        Comparator<ReadPages.ReadPagesData> c = (s1, s2) -> s1.getReadfrom().compareTo(s2.getReadfrom());
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            readPagesDataSorted.sort(c);
//                        } else {
//                            Collections.sort(readPagesData, new Comparator<ReadPages.ReadPagesData>() {
//                                public int compare(ReadPages.ReadPagesData s1, ReadPages.ReadPagesData s2) {
//                                    readPagesDataSorted=
//                                    return s1.getReadfrom().compareToIgnoreCase(s2.getReadfrom());
//                                }
//                            });
//                        }
//                        readPagesDataSorted = readPagesData.stream()
//                                .sorted(Comparator.comparing(ReadPages.ReadPagesData::getReadfrom))
//                                .collect(Collectors.toList());
//                        createNewList(readPagesDataSorted);
                        readPagesHelperClass = new ReadPagesHelperClass(readPagesData);

                        percentageValueTV.setText(readPagesHelperClass.getPercentageOfUniqueReadPagesList(readPagesHelperClass.getReadPagesCount()));

                        Log.d("TAG", "value is : " + (double) (readPagesHelperClass.getReadPagesCount() / 70));

                    } else {
                        Log.d("TAG", "Error readPagesData is null");

                    }
                } else {

                    Log.d("TAG", "Error response failed");
                }
            }


            @Override
            public void onFailure(Call<ReadPages> call, Throwable t) {
                Log.d("onFailure response", t.toString());
                //showErrorForNoData();
            }
        });
    }


    private void getAllUsersListFromRetrofit() {
        showLoading();
        ApiInterfaces apiInterface = ApiClient.getApiClient().create(ApiInterfaces.class);
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
                        Collections.sort(userDataList, new Comparator<Users.UserData>() {
                            @Override
                            public int compare(Users.UserData lhs, Users.UserData rhs) {
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                if (lhs.getmNoOfReadPages() > rhs.getmNoOfReadPages()) {
                                    return -1;
                                } else {
                                    return 1;
                                }
                            }
                        });
                        userListAdapter = new UserListAdapter(ShowDataActivity.this, userDataList, userId, ShowDataActivity.this);
                        rv_usersList.setAdapter(userListAdapter);
                        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                        itemAnimator.setAddDuration(1000);
                        itemAnimator.setRemoveDuration(1000);
                        rv_usersList.setItemAnimator(itemAnimator);
                        showList();
                    } else {
                        Log.d("TAG", "list is empty");
                        showError(SHOW_ERROR_FOR_NO_DATA);

                    }
                } else {
                    Log.d("TAG", "response is not successful");
                    showError(SHOW_ERROR_FOR_WRONG_DATA);

                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.d("onFailure response", t.toString());
                showError(SHOW_ERROR_FOR_WRONG_DATA);

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
}
