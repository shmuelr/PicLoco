package com.orbitdesign.panoramiopics.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.animation.GlideAnimationFactory;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.orbitdesign.panoramiopics.R;
import com.orbitdesign.panoramiopics.activities.PhotoActivity;
import com.orbitdesign.panoramiopics.models.Photo;
import com.orbitdesign.panoramiopics.utils.Tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shmuel Rosansky on 3/18/2015.
 */
public class MainListAdapter extends  RecyclerView.Adapter<MainListAdapter.PhotoHolder> {

    private static final String TAG = "MainListAdapter";
    private List<Photo> photoList = new ArrayList<>();



    public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView imageView;


        public PhotoHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView =  (TextView) v.findViewById(R.id.textView);
            imageView =  (ImageView) v.findViewById(R.id.imageView);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PhotoActivity.class);
            intent.putExtra("photo", photoList.get(getLayoutPosition()));

            PhotoActivity.IMAGE_BITMAP_SMALL = Tools.drawableToBitmap(imageView.getDrawable());

            view.getContext().startActivity(intent);
        }
    }


    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.photo_item, viewGroup, false);

        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder photoHolder, int i) {

        final Photo photo = photoList.get(i);

        photoHolder.textView.setText(photo.getPhotoTitle());

        Glide.with(photoHolder.imageView.getContext())
                .load(photo.getPhotoFileUrl())
                .asBitmap()
                .animate(R.anim.abc_fade_in) // this smoothly animates the images in
                .into(new BitmapImageViewTarget(photoHolder.imageView) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                if (palette.getDarkVibrantSwatch() != null) {
                                    photoHolder.textView.setTextColor(palette.getDarkVibrantSwatch().getTitleTextColor());
                                    photoHolder.textView.setBackgroundColor(palette.getDarkVibrantSwatch().getRgb());

                                } else if (palette.getMutedSwatch() != null) {
                                    photoHolder.textView.setTextColor(palette.getMutedSwatch().getTitleTextColor());
                                    photoHolder.textView.setBackgroundColor(palette.getMutedSwatch().getRgb());
                                }

                                if (photoHolder.textView != null && photoHolder.textView.getBackground() != null)
                                    photoHolder.textView.getBackground().setAlpha(200);
                            }
                        });
                    }
                });


    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void swapData(List<Photo> data){
        photoList = data;
        this.notifyDataSetChanged();
    }

    public void addData(List<Photo> photos) {
        Set<Photo> photoSet = new HashSet<>(photoList);

        for(Photo photo: photos){
            if(!photoSet.contains(photo)){
                photoList.add(photoList.size(),photo);
            }
        }

        //photoList.addAll(photoList.size(), photos);
        this.notifyDataSetChanged();
    }
}
