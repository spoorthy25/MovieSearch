package com.example.spoorthy.moviesearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by spoorthy on 4/14/2017.
 */

public class ResultMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

protected TextView name,dateOfRelease,language;
protected ImageView imageView;

private OnClickListener mOnClickListener;

public interface OnClickListener {
    void OnPersonResultsItemClick(View view, int position);
}

    public ResultMovieViewHolder(View itemView, OnClickListener onClickListener) {
        super(itemView);
        this.mOnClickListener = onClickListener;
        name = (TextView) itemView.findViewById(R.id.search_mve_name);
        dateOfRelease = (TextView) itemView.findViewById(R.id.search_mve_dor);
        language = (TextView) itemView.findViewById(R.id.search_mve_lang);
        imageView = (ImageView) itemView.findViewById(R.id.image_search_movie);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mOnClickListener != null) {
            mOnClickListener.OnPersonResultsItemClick(view, getAdapterPosition());
        }
    }
}
