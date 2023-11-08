// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDetailBoxBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RatingBar DratingBar;

  @NonNull
  public final CardView ImageView;

  @NonNull
  public final TextView IntAvg;

  @NonNull
  public final TextView IntMax;

  @NonNull
  public final TextView IntMin;

  @NonNull
  public final TextView MenuFishName;

  @NonNull
  public final ImageView Reviewimg;

  @NonNull
  public final TextView StorePrice;

  @NonNull
  public final TextView StorePriceInt;

  @NonNull
  public final TextView Textcomparison;

  @NonNull
  public final TextView contentView;

  @NonNull
  public final TextView likeInt;

  @NonNull
  public final CircleImageView mImg;

  @NonNull
  public final TextView mName;

  @NonNull
  public final LinearLayout mReiview;

  @NonNull
  public final TextView mStarsocore;

  @NonNull
  public final TextView texMax;

  @NonNull
  public final TextView textAvg;

  @NonNull
  public final TextView textMin;

  @NonNull
  public final ImageView up;

  private ActivityDetailBoxBinding(@NonNull LinearLayout rootView, @NonNull RatingBar DratingBar,
      @NonNull CardView ImageView, @NonNull TextView IntAvg, @NonNull TextView IntMax,
      @NonNull TextView IntMin, @NonNull TextView MenuFishName, @NonNull ImageView Reviewimg,
      @NonNull TextView StorePrice, @NonNull TextView StorePriceInt,
      @NonNull TextView Textcomparison, @NonNull TextView contentView, @NonNull TextView likeInt,
      @NonNull CircleImageView mImg, @NonNull TextView mName, @NonNull LinearLayout mReiview,
      @NonNull TextView mStarsocore, @NonNull TextView texMax, @NonNull TextView textAvg,
      @NonNull TextView textMin, @NonNull ImageView up) {
    this.rootView = rootView;
    this.DratingBar = DratingBar;
    this.ImageView = ImageView;
    this.IntAvg = IntAvg;
    this.IntMax = IntMax;
    this.IntMin = IntMin;
    this.MenuFishName = MenuFishName;
    this.Reviewimg = Reviewimg;
    this.StorePrice = StorePrice;
    this.StorePriceInt = StorePriceInt;
    this.Textcomparison = Textcomparison;
    this.contentView = contentView;
    this.likeInt = likeInt;
    this.mImg = mImg;
    this.mName = mName;
    this.mReiview = mReiview;
    this.mStarsocore = mStarsocore;
    this.texMax = texMax;
    this.textAvg = textAvg;
    this.textMin = textMin;
    this.up = up;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDetailBoxBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDetailBoxBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_detail_box, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDetailBoxBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.DratingBar;
      RatingBar DratingBar = ViewBindings.findChildViewById(rootView, id);
      if (DratingBar == null) {
        break missingId;
      }

      id = R.id.ImageView;
      CardView ImageView = ViewBindings.findChildViewById(rootView, id);
      if (ImageView == null) {
        break missingId;
      }

      id = R.id.IntAvg;
      TextView IntAvg = ViewBindings.findChildViewById(rootView, id);
      if (IntAvg == null) {
        break missingId;
      }

      id = R.id.IntMax;
      TextView IntMax = ViewBindings.findChildViewById(rootView, id);
      if (IntMax == null) {
        break missingId;
      }

      id = R.id.IntMin;
      TextView IntMin = ViewBindings.findChildViewById(rootView, id);
      if (IntMin == null) {
        break missingId;
      }

      id = R.id.MenuFishName;
      TextView MenuFishName = ViewBindings.findChildViewById(rootView, id);
      if (MenuFishName == null) {
        break missingId;
      }

      id = R.id.Reviewimg;
      ImageView Reviewimg = ViewBindings.findChildViewById(rootView, id);
      if (Reviewimg == null) {
        break missingId;
      }

      id = R.id.StorePrice;
      TextView StorePrice = ViewBindings.findChildViewById(rootView, id);
      if (StorePrice == null) {
        break missingId;
      }

      id = R.id.StorePriceInt;
      TextView StorePriceInt = ViewBindings.findChildViewById(rootView, id);
      if (StorePriceInt == null) {
        break missingId;
      }

      id = R.id.Textcomparison;
      TextView Textcomparison = ViewBindings.findChildViewById(rootView, id);
      if (Textcomparison == null) {
        break missingId;
      }

      id = R.id.contentView;
      TextView contentView = ViewBindings.findChildViewById(rootView, id);
      if (contentView == null) {
        break missingId;
      }

      id = R.id.likeInt;
      TextView likeInt = ViewBindings.findChildViewById(rootView, id);
      if (likeInt == null) {
        break missingId;
      }

      id = R.id.mImg;
      CircleImageView mImg = ViewBindings.findChildViewById(rootView, id);
      if (mImg == null) {
        break missingId;
      }

      id = R.id.mName;
      TextView mName = ViewBindings.findChildViewById(rootView, id);
      if (mName == null) {
        break missingId;
      }

      LinearLayout mReiview = (LinearLayout) rootView;

      id = R.id.mStarsocore;
      TextView mStarsocore = ViewBindings.findChildViewById(rootView, id);
      if (mStarsocore == null) {
        break missingId;
      }

      id = R.id.texMax;
      TextView texMax = ViewBindings.findChildViewById(rootView, id);
      if (texMax == null) {
        break missingId;
      }

      id = R.id.textAvg;
      TextView textAvg = ViewBindings.findChildViewById(rootView, id);
      if (textAvg == null) {
        break missingId;
      }

      id = R.id.textMin;
      TextView textMin = ViewBindings.findChildViewById(rootView, id);
      if (textMin == null) {
        break missingId;
      }

      id = R.id.up;
      ImageView up = ViewBindings.findChildViewById(rootView, id);
      if (up == null) {
        break missingId;
      }

      return new ActivityDetailBoxBinding((LinearLayout) rootView, DratingBar, ImageView, IntAvg,
          IntMax, IntMin, MenuFishName, Reviewimg, StorePrice, StorePriceInt, Textcomparison,
          contentView, likeInt, mImg, mName, mReiview, mStarsocore, texMax, textAvg, textMin, up);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
