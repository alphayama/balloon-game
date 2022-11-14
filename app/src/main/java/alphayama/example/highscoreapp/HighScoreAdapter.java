package alphayama.example.highscoreapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// handles the Recycler View
public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.HighScoreHolder> {

    private Context context;
    private ArrayList<HighScore> highScores;

    public HighScoreAdapter(Context context, ArrayList<HighScore> highScores) {
        this.context = context;
        this.highScores = highScores;
    }


    // adds data to recycler view list item
    @NonNull
    @Override
    public HighScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.score_list_item,parent,false);
        return new HighScoreHolder(view);
    }

    // iterates over high score list and sends them to be added to theist view
    @Override
    public void onBindViewHolder(@NonNull HighScoreAdapter.HighScoreHolder holder, int position) {
        HighScore highScore = highScores.get(position);
        holder.SetDetails(position,highScore);
    }

    // fetches size of the high score array list
    @Override
    public int getItemCount() {
        return highScores.size();
    }

    // updates the text views in list item
    class HighScoreHolder extends RecyclerView.ViewHolder{

        private TextView txtPlayerName, txtScore, txtDate;

        public HighScoreHolder(@NonNull View itemView) {
            super(itemView);
            txtPlayerName=itemView.findViewById(R.id.txtPlayerName);
            txtScore=itemView.findViewById(R.id.txtScore);
            txtDate=itemView.findViewById(R.id.txtDate);
        }

        void SetDetails(int position ,HighScore highScore){
            txtPlayerName.setText((position+1)+". "+highScore.getPlayerName());
            txtScore.setText(""+highScore.getScore());
            txtDate.setText(highScore.getDate());

        }
    }
}
