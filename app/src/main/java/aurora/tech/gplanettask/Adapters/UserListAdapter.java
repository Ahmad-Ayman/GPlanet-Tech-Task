package aurora.tech.gplanettask.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aurora.tech.gplanettask.Activities.ShowDataActivity;
import aurora.tech.gplanettask.Models.Users;
import aurora.tech.gplanettask.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListAdapterViewHolder> {
    private Context mContext;
    private List<Users.UserData> userData;
    private int userID;
    ShowDataActivity mParentActivity;

    public UserListAdapter(Context mContext, List<Users.UserData> userData, int userID, ShowDataActivity mParentActivity) {
        this.mContext = mContext;
        this.userData = userData;
        this.userID = userID;
        this.mParentActivity = mParentActivity;
    }

    @NonNull
    @Override
    public UserListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_item, null);
        UserListAdapter.UserListAdapterViewHolder rcv = new UserListAdapter.UserListAdapterViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapterViewHolder userListAdapterViewHolder, int i) {
        Users.UserData user = userData.get(i);
        userListAdapterViewHolder.BindViews(user);
        if (user.getId() == userID) {
            // saveUserOrderToSharedPref(i+1);
            mParentActivity.userOrderValueTV.setText(String.valueOf(i + 1));
        }
    }

    private void saveUserOrderToSharedPref(int userOrder) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("USERIDPREFERENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("USERORDER_KEY", userOrder).apply();
    }

    @Override
    public int getItemCount() {
        if (null == userData)
            return 0;
        return userData.size();
    }

    public class UserListAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTV;
        private TextView noofpagesTV;

        public UserListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.userNameValueTV);
            noofpagesTV = itemView.findViewById(R.id.noofpagesTV);
        }

        private void BindViews(Users.UserData userDataItem) {
            userNameTV.setText(userDataItem.getName());
            noofpagesTV.setText(String.valueOf((int) userDataItem.getmNoOfReadPages()));
        }

    }
}
