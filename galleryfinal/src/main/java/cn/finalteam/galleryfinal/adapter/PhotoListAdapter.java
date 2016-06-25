/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PhotoSelectActivity;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;
import java.util.List;
import java.util.Map;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:59
 */
public class PhotoListAdapter extends ViewHolderAdapter<PhotoListAdapter.PhotoViewHolder, PhotoInfo> {

    private List<PhotoInfo> mSelectList;
    private int mScreenWidth;
    private int mRowWidth;

    private Activity mActivity;

    public PhotoListAdapter(Activity activity, List<PhotoInfo> list, List<PhotoInfo> selectList, int screenWidth) {
        super(activity, list);
        this.mSelectList = selectList;
        this.mScreenWidth = screenWidth;
        this.mRowWidth = mScreenWidth/3;
        this.mActivity = activity;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_photo_list_item, parent);
        setHeight(view);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        PhotoInfo photoInfo = getDatas().get(position);
        String path = "";
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }
        if (GalleryFinal.getFunctionConfig().isCamera()) {
            if (position == 0) {
                Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_add_photo);
                GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvThumb, defaultDrawable, mRowWidth, mRowWidth);
                holder.mIvThumb.setImageResource(R.drawable.ic_add_photo);
//                holder.mIvCheck.setVisibility(View.GONE);
                holder.mTvNumber.setVisibility(View.GONE);
            } else {
                setSelectUI(holder, path, photoInfo);
            }
        } else {
            setSelectUI(holder, path, photoInfo);
        }
    }

    private void setSelectUI(PhotoViewHolder holder, String path, PhotoInfo photoInfo) {
        holder.mIvThumb.setImageResource(R.drawable.ic_gf_default_photo);
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
        GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvThumb, defaultDrawable, mRowWidth, mRowWidth);
        holder.mView.setAnimation(null);
        holder.mView.setAnimation(AnimationUtils.loadAnimation(mActivity, GalleryFinal.getCoreConfig().getAnimation()));
//        holder.mIvCheck.setImageResource(GalleryFinal.getGalleryTheme().getIconCheck());
        if ( GalleryFinal.getFunctionConfig().isMutiSelect() ) {
//            holder.mIvCheck.setVisibility(View.VISIBLE);
            holder.mTvNumber.setText(photoInfo.getIndex() + "");
            if (mSelectList.contains(photoInfo)) {
//                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
                holder.mTvNumber.setVisibility(View.VISIBLE);
            } else {
//                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
                holder.mTvNumber.setVisibility(View.GONE);
            }
        } else {
//            holder.mIvCheck.setVisibility(View.GONE);
            holder.mTvNumber.setVisibility(View.GONE);
        }
    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    @Override
    public void notifyDataSetChanged() {
        ((PhotoSelectActivity)mActivity).clearAllNumberView();
        super.notifyDataSetChanged();
    }

    private void startAnim() {
        if (GalleryFinal.getCoreConfig().getAnimation() > 0) {
            for (int i = 0; i < ((PhotoSelectActivity)mActivity).getPhotoGridView().getChildCount(); i++) {
                ((PhotoSelectActivity)mActivity).getPhotoGridView().getChildAt(i)
                        .setAnimation(AnimationUtils.loadAnimation(mActivity, GalleryFinal.getCoreConfig().getAnimation()));
            }
        }
    }

    public static class PhotoViewHolder extends ViewHolderAdapter.ViewHolder {

        public GFImageView mIvThumb;
        public ImageView mIvCheck;
        public TextView mTvNumber;
        View mView;
        public PhotoViewHolder(View view) {
            super(view);
            mView = view;
            mIvThumb = (GFImageView) view.findViewById(R.id.iv_thumb);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
            mTvNumber = (TextView) view.findViewById(R.id.tv_number);
        }
    }
}
