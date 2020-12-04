package com.p2jj.wesportif.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.p2jj.wesportif.Model.Comment;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptorComments extends RecyclerView.Adapter<AdaptorComments.myViewHolder> {

    Context mContext;
    List<Comment> mData;

    public AdaptorComments(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflator= LayoutInflater.from(mContext);
        View v = inflator.inflate(R.layout.comment_card,parent,false);




        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {


        String url="http://wessporitf.eu-4.evennode.com/uploads/users/"+mData.get(position).getUserImg_com();
        Glide.with(mContext).load(url).into(holder.commentUser_img);




        //  holder.commentUser_img.setImageResource(mData.get(position).getImg_user_commenter());
        String username=mData.get(position).getPrenomUser()+" "+mData.get(position).getNomUser();
        holder.userName_Commenter.setText(username);
        holder.date.setText(mData.get(position).getDateComment());
        holder.Comment.setText(mData.get(position).getComment());


    }

    @Override
    public int getItemCount() {
        return mData.size();
     }

    public class myViewHolder extends  RecyclerView.ViewHolder{
        ImageView commentUser_img;
        TextView userName_Commenter, date , Comment;

        public myViewHolder( View itemView) {
            super(itemView);
            commentUser_img = itemView.findViewById(R.id.CommentUser_img);

            userName_Commenter= itemView.findViewById(R.id.CommentUser_name);
            date =itemView.findViewById(R.id.CommentDate_comment);
            Comment=itemView.findViewById(R.id.Comment_commentText);




        }
    }


}
