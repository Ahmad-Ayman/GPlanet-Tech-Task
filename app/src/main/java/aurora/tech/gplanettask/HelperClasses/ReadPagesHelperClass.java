package aurora.tech.gplanettask.HelperClasses;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aurora.tech.gplanettask.Models.ReadPages;

public class ReadPagesHelperClass {
    private List<ReadPages.ReadPagesData> mReadPagesList;
    private int fromInteger, toInteger;
    private List<Integer> readPages;

    public ReadPagesHelperClass(List<ReadPages.ReadPagesData> readPagesList) {
        this.mReadPagesList = readPagesList;
        readPages = new ArrayList<>();
    }

    public double getReadPagesCount() {
        for (int i = 0; i < mReadPagesList.size(); i++) {
            fromInteger = Integer.parseInt(mReadPagesList.get(i).getReadfrom());
            toInteger = Integer.parseInt(mReadPagesList.get(i).getReadto());
            for (int j = fromInteger; j <= toInteger; j++) {
                if (!readPages.contains(j)) {
                    readPages.add(j);
                }
            }
        }
        if (readPages != null) {
            return readPages.size();
        } else {
            return 0;
        }

    }

    public String getPercentageOfUniqueReadPagesList(double x) {
        DecimalFormat df = new DecimalFormat("#.000");
        df.setMinimumFractionDigits(1);
        df.setMinimumIntegerDigits(1);
        return df.format(x / 70);

    }

}
