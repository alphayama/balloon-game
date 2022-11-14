package <package_name>.highscoreapp;

import android.text.InputFilter;
import android.text.Spanned;

// this class defines the range of scores that can be entered
public class ScoreRangeFilter implements InputFilter {
    private int minVal , maxVal ;
    public ScoreRangeFilter ( int minVal , int maxVal) {
        this.minVal = minVal;
        this.maxVal = maxVal;
    }
    public ScoreRangeFilter (String minVal , String maxVal) {
        this.minVal = Integer.parseInt(minVal);
        this.maxVal = Integer.parseInt(maxVal);
    }

    // checks if the high score edittext field lies in valid range or not
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try{
            int score = Integer.parseInt(dest.toString() + source.toString());
            if (score >= minVal && score <= maxVal)
                return null;

        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return "";
    }
}
