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
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityComparingPriceDetailBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnBuy;

  @NonNull
  public final TextView fishBigPrice;

  @NonNull
  public final TextView fishMiddlePrice;

  @NonNull
  public final TextView fishMiniPrice;

  @NonNull
  public final TextView fishNameDetail;

  @NonNull
  public final TextView fishPriceAvg;

  @NonNull
  public final TextView fishPriceMax;

  @NonNull
  public final TextView fishPriceMin;

  @NonNull
  public final TextView fishSmallPrice;

  @NonNull
  public final ImageView imgBackarrow;

  private ActivityComparingPriceDetailBinding(@NonNull LinearLayout rootView,
      @NonNull Button btnBuy, @NonNull TextView fishBigPrice, @NonNull TextView fishMiddlePrice,
      @NonNull TextView fishMiniPrice, @NonNull TextView fishNameDetail,
      @NonNull TextView fishPriceAvg, @NonNull TextView fishPriceMax,
      @NonNull TextView fishPriceMin, @NonNull TextView fishSmallPrice,
      @NonNull ImageView imgBackarrow) {
    this.rootView = rootView;
    this.btnBuy = btnBuy;
    this.fishBigPrice = fishBigPrice;
    this.fishMiddlePrice = fishMiddlePrice;
    this.fishMiniPrice = fishMiniPrice;
    this.fishNameDetail = fishNameDetail;
    this.fishPriceAvg = fishPriceAvg;
    this.fishPriceMax = fishPriceMax;
    this.fishPriceMin = fishPriceMin;
    this.fishSmallPrice = fishSmallPrice;
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
      id = R.id.btn_buy;
      Button btnBuy = ViewBindings.findChildViewById(rootView, id);
      if (btnBuy == null) {
        break missingId;
      }

      id = R.id.fish_big_price;
      TextView fishBigPrice = ViewBindings.findChildViewById(rootView, id);
      if (fishBigPrice == null) {
        break missingId;
      }

      id = R.id.fish_middle_price;
      TextView fishMiddlePrice = ViewBindings.findChildViewById(rootView, id);
      if (fishMiddlePrice == null) {
        break missingId;
      }

      id = R.id.fish_mini_price;
      TextView fishMiniPrice = ViewBindings.findChildViewById(rootView, id);
      if (fishMiniPrice == null) {
        break missingId;
      }

      id = R.id.fish_name_detail;
      TextView fishNameDetail = ViewBindings.findChildViewById(rootView, id);
      if (fishNameDetail == null) {
        break missingId;
      }

      id = R.id.fish_price_avg;
      TextView fishPriceAvg = ViewBindings.findChildViewById(rootView, id);
      if (fishPriceAvg == null) {
        break missingId;
      }

      id = R.id.fish_price_max;
      TextView fishPriceMax = ViewBindings.findChildViewById(rootView, id);
      if (fishPriceMax == null) {
        break missingId;
      }

      id = R.id.fish_price_min;
      TextView fishPriceMin = ViewBindings.findChildViewById(rootView, id);
      if (fishPriceMin == null) {
        break missingId;
      }

      id = R.id.fish_small_price;
      TextView fishSmallPrice = ViewBindings.findChildViewById(rootView, id);
      if (fishSmallPrice == null) {
        break missingId;
      }

      id = R.id.img_backarrow;
      ImageView imgBackarrow = ViewBindings.findChildViewById(rootView, id);
      if (imgBackarrow == null) {
        break missingId;
      }

      return new ActivityComparingPriceDetailBinding((LinearLayout) rootView, btnBuy, fishBigPrice,
          fishMiddlePrice, fishMiniPrice, fishNameDetail, fishPriceAvg, fishPriceMax, fishPriceMin,
          fishSmallPrice, imgBackarrow);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
