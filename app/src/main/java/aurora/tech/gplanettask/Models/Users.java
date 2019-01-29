package aurora.tech.gplanettask.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import aurora.tech.gplanettask.HelperClasses.ReadPagesHelperClass;

public class Users {
    @SerializedName("data")
    private List<Users.UserData> userDataItem;

    public Users() {
        this.userDataItem = new ArrayList<>();
    }

    public List<Users.UserData> getUsersData() {
        return userDataItem;
    }

    public class UserData {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("subQuery")
        private List<ReadPages.ReadPagesData> readPagesDataList;
        private double mNoOfReadPages;
        ReadPagesHelperClass readPagesHelperClass;

        public UserData(int id, String name, List<ReadPages.ReadPagesData> readPagesDataList) {
            this.id = id;
            this.name = name;
            this.readPagesDataList = readPagesDataList;

        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<ReadPages.ReadPagesData> getReadPagesDataList() {
            return readPagesDataList;
        }

        public double getmNoOfReadPages() {
            readPagesHelperClass = new ReadPagesHelperClass(getReadPagesDataList());
            mNoOfReadPages = readPagesHelperClass.getReadPagesCount();
            return mNoOfReadPages;
        }
    }
}
