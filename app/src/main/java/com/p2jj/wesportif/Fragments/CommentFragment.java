package com.p2jj.wesportif.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.Adapters.AdaptorAllEvnets;
import com.p2jj.wesportif.Adapters.AdaptorComments;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.Comment;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentFragment extends Fragment {

    EditText CommentInput_Text;
    CircleImageView UserImg_Commenter;
    ImageView SendComment_User ;
    RecyclerView recyclerViewComments;
    int eventId;


    public CommentFragment(int eventId)
    {
        this.eventId=eventId;
    }
    public CommentFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_comment,container,false);
        UserImg_Commenter= rootView.findViewById(R.id.CreateComment_imgUser);
        CommentInput_Text=rootView.findViewById(R.id.CreateComment_textInput);
        SendComment_User=rootView.findViewById(R.id.CreateComment_SendComment_Button);
        recyclerViewComments=rootView.findViewById(R.id.recyclerViewComments);

        // Test : fill fragment with comment to test the layout display

        // Config the Comment EditText with th button send
        retrieveComments();


        String urlC="http://wessporitf.eu-4.evennode.com/uploads/users/"+ Session.getInstance().getUser().getImg_user();
        Glide.with(getContext()).load(urlC).into(UserImg_Commenter);



        SendComment_User.setVisibility(View.GONE);
        CommentInput_Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SendComment_User.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                SendComment_User.bringToFront();
                SendComment_User.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.equals(SendComment_User)) {
                            addComment(CommentInput_Text.getText().toString());


                            CommentInput_Text.getText().clear();
                            //Intent i = new Intent(getContext(), AllEvent.class);
                          //  startActivity(i);



                        }
                    }
                });
            }
        });




        return rootView;
    }

    public void addComment(String com)
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .PostCom(eventId,Session.getInstance().getUser().getCin(),com);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("reactionInformations");
                    if(msg.equals("Reactions added"))
                    {
                        Toast.makeText(getContext(), "Comment added", Toast.LENGTH_LONG).show();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getStackTrace());
            }
        });
    }

    public void retrieveComments()
    {

       final List<Comment> mList=new ArrayList<>();
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetComments(eventId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("reactionInformations");

                    if(msg.equals("Reactions retrieved")){
                        JSONArray evts=jsonResp.getJSONArray("reactions");

                        for (int i = 0 ; i < evts.length(); i++) {

                            JSONObject obj = evts.getJSONObject(i);
                            Comment c=new Comment();

                            c.setId_Event(obj.getInt("id_event"));
                            c.setComment(obj.getString("userCom"));
                            c.setId_User(obj.getString("id_user"));
                            c.setNomUser(obj.getString("nom"));
                            c.setPrenomUser(obj.getString("prenom"));
                            c.setUserImg_com(obj.getString("img"));
                            c.setDateComment("");


                            mList.add(c);

                        }


                        AdaptorComments adaptorEv = new AdaptorComments(getContext(),mList);

                        recyclerViewComments.setAdapter(adaptorEv);
                        recyclerViewComments.setLayoutManager(new LinearLayoutManager(getContext()));


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("in comment fragment ","getting comments");
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI even


}
