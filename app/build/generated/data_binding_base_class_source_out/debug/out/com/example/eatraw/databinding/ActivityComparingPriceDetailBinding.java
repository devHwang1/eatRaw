// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityComparingPriceDetailBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final BottomNavigationView bnvMain;

  @NonNull
  public final Button btnBuyReview;

  @NonNull
  public final ImageView fishImg;

  @NonNull
  public final TextView fishNameDetail;

  @NonNull
  public final TextView fishPriceAvg;

  @NonNull
  public final TextView fishPriceMax;

  @NonNull
  public final TextView fishPriceMin;

  @NonNull
  public final ImageView imgBackarrow;

  private ActivityComparingPriceDetailBinding(@NonNull LinearLayout rootView,
      @NonNull BottomNavigationView bnvMain, @NonNull Button btnBuyReview,
      @NonNull ImageView fishImg, @NonNull TextView fishNameDetail, @NonNull TextView fishPriceAvg,
      @NonNull TextView fishPriceMax, @NonNull TextView fishPriceMin,
      @NonNull ImageView imgBackarrow) {
    this.rootView = rootView;
    this.bnvMain = bnvMain;
    this.btnBuyReview = btnBuyReview;
    this.fishImg = fishImg;
    this.fishNameDetail = fishNameDetail;
    this.fishPriceAvg = fishPriceAvg;
    this.fishPriceMax = fishPriceMax;
    this.fishPriceMin = fishPriceMin;
    this.imgBackarrow = imgBackarrow;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityComparingPriceDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityComparingPriceDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_comparing_price_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityComparingPriceDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bnv_main;
      BottomNavigationView bnvMain = ViewBindings.findChildViewById(rootView, id);
      if (bnvMain == null) {
        break missingId;
      }

      id = R.id.btnBuyReview;
      Button btnBuyReview = ViewBindings.findChildViewById(rootView, id);
      if (btnBuyReview == null) {
        break missingId;
      }

      id = R.id.fishImg;
      ImageView fishImg = ViewBindings.findChildViewById(rootView, id);
      if (fishImg == null) {
        break missingId;
      }

      id = R.id.fishNameDetail;
      TextView fishNameDetail = ViewBindings.findChildViewById(rootView, id);
      if (fishNameDetail == null) {
        break missingId;
      }

      id = R.id.fishPriceAvg;
      TextView fishPriceAvg = ViewBindings.findChildViewById(rootView, id);
      if (fishPriceAvg == null) {
        break missingId;
      }

      id = R.id.fishPriceMax;
      TextView fishPriceMax = ViewBindings.findChildViewById(rootView, id);
      if (fishPriceMax == null) {
        break missingId;
      }

      id = R.id.fishPriceMin;
      TextView fishPriceMin = ViewBindings.findChildViewById(rootView, id);
      if (fishPriceMin == null) {
        break missingId;
      }

      id = R.id.img_backarrow;
      ImageView imgBackarrow = ViewBindings.findChildViewById(rootView, id);
      if (imgBackarrow == null) {
        break missingId;
      }

      return new ActivityComparingPriceDetailBinding((LinearLayout) rootView, bnvMain, btnBuyReview,
          fishImg, fishNameDetail, fishPriceAvg, fishPriceMax, fishPriceMin, imgBackarrow);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
