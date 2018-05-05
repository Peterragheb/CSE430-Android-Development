package com.games.peter.project_live_football_tactics.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.games.peter.project_live_football_tactics.Activity.MatchActivity;
import com.games.peter.project_live_football_tactics.Class.Lineup;
import com.games.peter.project_live_football_tactics.Class.Player;
import com.games.peter.project_live_football_tactics.R;

import java.util.ArrayList;

/**
 * Created by Peter on 19/4/2018.
 */

public class tab_away_lineup_fragment extends Fragment implements tab_Lineup_fragment.LineupListener {
    ViewStub viewStub;
    ArrayList<ViewStub> llout__col_2,llout__col_3,llout__col_4,llout__col_5,llout__col_6,llout__col_7,llout__col_8,llout__col_9,llout__col_1;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_away_lineup_fragment,container,false);
        Bundle bundle = getArguments();
        initUIComponents(bundle,view);
        return view;
    }
    //======================================================
    private void initUIComponents(Bundle bundle,View view){
        if(bundle!=null && !bundle.isEmpty()){
            ArrayList<Lineup> lineups =bundle.getParcelableArrayList("lineups");
            if(lineups!=null &&!lineups.isEmpty()&& lineups.get(0).getPlayersSize()!=0)
                setup_formation(lineups,view);

        }
    }
    //======================================================
    private void setup_formation(ArrayList<Lineup> lineups,View view){
        viewStub = view.findViewById(R.id.stub_formation_team);
        viewStub.setLayoutResource(R.layout.formation);
        View inflated = viewStub.inflate();
        if (getActivity().getIntent().hasExtra("away_team_name")){
            ((TextView)inflated.findViewById(R.id.tv_formation_team_name)).setText(getActivity().getIntent().getStringExtra("away_team_name"));
        }
        ArrayList<Player> players = lineups.get(0).getPlayers();
        int player1_row =players.get(0).getRow();
        if (player1_row==1){
            inflated.findViewById(R.id.llout_col_0_row_0).findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
            ((TextView)(inflated.findViewById(R.id.llout_col_0_row_0).findViewById(R.id.tv_player_number))).setText(players.get(0).getShirtNumber()+"");
            ((TextView)inflated.findViewById(R.id.llout_col_0_row_0).findViewById(R.id.tv_player_name)).setText(players.get(0).getName());
        }
        llout__col_2 = new ArrayList<>();
        llout__col_3 = new ArrayList<>();
        llout__col_4 = new ArrayList<>();
        llout__col_5 = new ArrayList<>();
        llout__col_6 = new ArrayList<>();
        llout__col_7 = new ArrayList<>();
        llout__col_8 = new ArrayList<>();
        llout__col_9 = new ArrayList<>();
        llout__col_1 = new ArrayList<>();
        for (int i=1;i<players.size();i++){
            int col = players.get(i).getCol();
            int row = players.get(i).getRow();
            if (col==2){
                String commonId ="llout_col_2_row_"+row ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_2.add(viewStub);
            }
            else if (col==3){
                String commonId ="llout_col_3_row_"+row ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_3.add(viewStub);
            }
            else if (col==4){
                String commonId ="llout_col_4_row_"+row ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_4.add(viewStub);
            }
            else if (col==5){
                String commonId ="llout_col_5_row_"+row;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_5.add(viewStub);
            }
            else if (col==6){
                String commonId ="llout_col_6_row_"+row;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_6.add(viewStub);
            }
            else if (col==7){
                String commonId ="llout_col_7_row_"+players.get(i).getRow() ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_7.add(viewStub);
            }
            else if (col==8){
                String commonId ="llout_col_8_row_"+row ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_8.add(viewStub);
            }
            else if (col==9){
                String commonId ="llout_col_9_row_"+row ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_9.add(viewStub);
            }
            else if (col==1){
                String commonId ="llout_col_1_row_"+row ;
                int id= getResources().getIdentifier(commonId, "id", getActivity().getPackageName());
                ViewStub viewStub = inflated.findViewById(id);
                viewStub.setLayoutResource(R.layout.player_on_pitch_layout);
                View inflated1 = viewStub.inflate();
                inflated1.findViewById(R.id.iv_player_image).setBackgroundResource(R.drawable.circle2);
                ((TextView)inflated1.findViewById(R.id.tv_player_number)).setText(players.get(i).getShirtNumber()+"");
                ((TextView)inflated1.findViewById(R.id.tv_player_name)).setText(players.get(i).getName());
                llout__col_1.add(viewStub);
            }

        }
        if (llout__col_2.isEmpty()){
            Log.v("REMOVE","2");
            inflated.findViewById(R.id.llout_col_2).setVisibility(View.GONE);
        }
        if (llout__col_3.isEmpty()){
            Log.v("REMOVE","3");
            inflated.findViewById(R.id.llout_col_3).setVisibility(View.GONE);
        }
        if (llout__col_4.isEmpty()){
            Log.v("REMOVE","4");
            inflated.findViewById(R.id.llout_col_4).setVisibility(View.GONE);
        }
        if (llout__col_5.isEmpty()){
            Log.v("REMOVE","5");
            inflated.findViewById(R.id.llout_col_5).setVisibility(View.GONE);
        }
        if (llout__col_6.isEmpty()){
            Log.v("REMOVE","6");
            inflated.findViewById(R.id.llout_col_6).setVisibility(View.GONE);
        }
        if (llout__col_7.isEmpty()){
            Log.v("REMOVE","7");
            inflated.findViewById(R.id.llout_col_7).setVisibility(View.GONE);
        }
        if (llout__col_8.isEmpty()){
            Log.v("REMOVE","8");
            inflated.findViewById(R.id.llout_col_8).setVisibility(View.GONE);
        }
        if (llout__col_9.isEmpty()){
            Log.v("REMOVE","9");
            inflated.findViewById(R.id.llout_col_9).setVisibility(View.GONE);
        }
        if (llout__col_1.isEmpty()){
            Log.v("REMOVE","1");
            inflated.findViewById(R.id.llout_col_1).setVisibility(View.GONE);
        }

    }
    //======================================================
    @Override
    public void onChange(ArrayList<Lineup> lineups) {
        setup_formation(lineups,tab_away_lineup_fragment.this.getView());
    }
    //======================================================
    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MatchActivity)getActivity()).mtab_Lineup_fragment.removeListener(this);
    }
    //======================================================
}