package aurora.tech.gplanettask.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReadPages {
    @SerializedName("readPagesList")
    private List<ReadPages.ReadPagesData> readPagesData;

    public ReadPages() {
        this.readPagesData = new ArrayList<>();
    }

    public List<ReadPages.ReadPagesData> getReadPagesData() {
        return readPagesData;
    }

    public class ReadPagesData {
        @SerializedName("id")
        private int id;
        @SerializedName("readfrom")
        private String readfrom;
        @SerializedName("readto")
        private String readto;

        public ReadPagesData(int id, String readfrom, String readto) {
            this.id = id;
            this.readfrom = readfrom;
            this.readto = readto;
        }

        public int getId() {
            return id;
        }

        public String getReadfrom() {
            return readfrom;
        }

        public String getReadto() {
            return readto;
        }

        public void setReadTo(int value) {
            this.readto = String.valueOf(value);
        }
    }
}
