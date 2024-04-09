package msu.edu.cse476.hoerdema.cse476projectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {
    private SwitchCompat weeklySwitch;
    private RecyclerView leaderBoardEntries;
    private RecyclerView leaderBoardWeeklyEntries;

    private UserAdapter adapter;
    private UserAdapter adapterWeekly;
    private ArrayList<User> userList;
    private ArrayList<User> userListWeekly;

    public class NetworkTask implements Runnable {
        @Override
        public void run() {
            // Perform your network operations here
            // Make sure to call the appropriate method for your operation, like GetAllRecords()
            JSONArray users = NetworkUtil.GetAllRecords();
            JSONArray usersWeekly = NetworkUtil.GetAllRecordsWeekly();


            try {
                for (int i = 0; i < users.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    String name = user.getString("User");
                    int time = user.getInt("Time");
                    userList.add(new User(name, time));
                }

                for (int i = 0; i < usersWeekly.length(); i++) {
                    JSONObject user = users.getJSONObject(i);
                    String name = user.getString("User");
                    int time = user.getInt("Time");
                    userListWeekly.add(new User(name, time));
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
            }

            Collections.sort(userList, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    // Compare based on age
                    return Integer.compare(user2.time, user1.time);
                }
            });

            Collections.sort(userListWeekly, new Comparator<User>() {
                @Override
                public int compare(User user1, User user2) {
                    // Compare based on age
                    return Integer.compare(user2.time, user1.time);
                }
            });

            runOnUiThread(() -> {
                adapter = new UserAdapter(userList);
                leaderBoardEntries.setAdapter(adapter);

                adapterWeekly = new UserAdapter(userListWeekly);
                leaderBoardWeeklyEntries.setAdapter(adapterWeekly);
            });


        }
    }


    public static class User {
        public String name;
        public int time;

        public User(String name, int time) {
            this.name = name;
            this.time = time;
        }



    }

    public static class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
        private ArrayList<User> userList;

        public UserAdapter(ArrayList<User> userList) {
            this.userList = userList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User user = userList.get(position);
            holder.posTextView.setText((position+1)+". ");
            holder.nameTextView.setText(user.name);

            long seconds = user.time;
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long remainingSeconds = seconds % 60;

            String time = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
            holder.timeTextView.setText(time);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTextView;
            public TextView posTextView;
            public TextView timeTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.userName);
                timeTextView = itemView.findViewById(R.id.userTIme);
                posTextView = itemView.findViewById(R.id.place);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);


        weeklySwitch = findViewById(R.id.leaderBoardWeeklyToggle);
        leaderBoardEntries = findViewById(R.id.leaderBoardEntries);
        leaderBoardWeeklyEntries = findViewById(R.id.leaderBoardWeeklyEntries);


        leaderBoardEntries.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<User>();

        leaderBoardWeeklyEntries.setLayoutManager(new LinearLayoutManager(this));
        userListWeekly = new ArrayList<User>();


        Thread thread = new Thread(new NetworkTask());
        thread.start();


        weeklySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (weeklySwitch.isChecked()){
                leaderBoardEntries.setVisibility(View.GONE);
                leaderBoardWeeklyEntries.setVisibility(View.VISIBLE);
            } else {
                leaderBoardEntries.setVisibility(View.VISIBLE);
                leaderBoardWeeklyEntries.setVisibility(View.GONE);
            }
        });

    }





}