package com.p2jj.wesportif.Adapters;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.p2jj.wesportif.Fragments.CommentFragment;
import com.p2jj.wesportif.Fragments.InvitationDetailFragment;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.R;

import java.util.List;

public class AdaptorInvitCoach extends RecyclerView.Adapter<AdaptorInvitCoach.myViewHolder> {

    Fragment InvitationDetailFragment;
    Context mContext;
    List<Event> mData;
    List<User> mDataUser;
    LinearLayout InvitCoach_mainFrame;

    public AdaptorInvitCoach(Context mContext,List<User>mDataUser, List<Event> mData) {
        this.mContext = mContext;
        this.mDataUser = mDataUser;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflator= LayoutInflater.from(mContext);
        View v = inflator.inflate(R.layout.card_invitationcoach,parent,false);
        InvitationDetailFragment = new InvitationDetailFragment();

        return new myViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {

        //holder.User_img.setImageResource(mDataUser.get(position).getImg_user());
        holder.User_name.setText(mDataUser.get(position).getNom());
        holder.titleEvent.setText(mData.get(position).getTitre());
        holder.date.setText(mData.get(position).getDate_debut());
        holder.VoirMoin.setVisibility(View.GONE);

        InvitationDetailFragment = new InvitationDetailFragment();
        final FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();



        holder.VoirPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               InvitationDetailFragment = new InvitationDetailFragment();
//                final FrameLayout frameLayout = new FrameLayout(mContext);
//                frameLayout.setId(position+1);
//                holder.InvitCoach_mainFrame.addView(frameLayout);
//                setFragment(InvitationDetailFragment,frameLayout.getId());
//
//                setFragment(InvitationDetailFragment,frameLayout.getId());


                if(!InvitationDetailFragment.isAdded()){

                int newContainerId = getUniqueId();
                holder.InvitCoach_mainFrame.setId(newContainerId);
                fragmentManager.beginTransaction().add(newContainerId,InvitationDetailFragment).commit();
                    fragmentManager.beginTransaction().show(InvitationDetailFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                    holder.VoirMoin.setVisibility(View.VISIBLE);
                    holder.VoirPlus.setVisibility(View.GONE);


                }else{
                    int newContainerId = getUniqueId();
                    holder.InvitCoach_mainFrame.setId(newContainerId);
                    if(newContainerId==newContainerId ){
                        fragmentManager.beginTransaction().show(InvitationDetailFragment).commit();
                        holder.VoirMoin.setVisibility(View.VISIBLE);
                        holder.VoirPlus.setVisibility(View.GONE);

                    }


                }

            }

            // Method that could us an unique id
            public int getUniqueId(){
                return (int) SystemClock.currentThreadTimeMillis();
            }
        });


        holder.VoirMoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager.beginTransaction().hide(InvitationDetailFragment).commit();
                holder.VoirMoin.setVisibility(View.GONE);
                holder.VoirPlus.setVisibility(View.VISIBLE);

            }
        });













    }


















    public final View.OnClickListener VoirPlus=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InvitationDetailFragment = new InvitationDetailFragment();
            if(!InvitationDetailFragment.isAdded()){

                showFragment(InvitationDetailFragment);
            }else{

                showFragment(InvitationDetailFragment);            }
        }
    };



    @Override
    public int getItemCount() {
        return mData.size();
     }

    public class myViewHolder extends  RecyclerView.ViewHolder{
        ImageView User_img;
        TextView titleEvent,User_name, date , CategorieSport;
        TextView VoirPlus, VoirMoin;
        LinearLayout InvitCoach_mainFrame;

        public myViewHolder( View itemView) {
            super(itemView);
            User_img = itemView.findViewById(R.id.InvitCoach_UserInviter_img);
            titleEvent= itemView.findViewById(R.id.InvitCoach_EventName);

            User_name=itemView.findViewById(R.id.InvitCoach_UserNameInviter);
            date =itemView.findViewById(R.id.InvitCoach_DateCreation);
            VoirPlus=itemView.findViewById(R.id.InvitCoach_VoirPlusButton);
            VoirMoin=itemView.findViewById(R.id.InvitCoach_VoirPMoinButton);
            InvitCoach_mainFrame=itemView.findViewById(R.id.InvitCoach_mainFrame);
            InvitationDetailFragment = new InvitationDetailFragment();
        }
    }


    public void setFragment(Fragment fragment,int v){


        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.InvitCoach_mainFrame,fragment);
        fragmentTransaction.commit();

    }

    public void showFragment(Fragment fragment){

        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

    }

    public void RemoveFragment(Fragment fragment){
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();

    }
}
