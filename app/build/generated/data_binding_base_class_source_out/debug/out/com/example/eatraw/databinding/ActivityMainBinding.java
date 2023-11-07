// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final BottomNavigationView bnvMain;

  @NonNull
  public final TextView fishName1;

  @NonNull
  public final TextView fishName2;

  @NonNull
  public final TextView fishPrice1;

  @NonNull
  public final TextView fishPrice2;

  @NonNull
  public final ImageView imageView2;

  @NonNull
  public final ImageView imageView3;

  @NonNull
  public final SearchView mainSearchbar;

  @NonNull
  public final ActionMenuView menubar1;

  @NonNull
  public final LinearLayout menubar3;

  @NonNull
  public final LinearLayout menubar4;

  @NonNull
  public final TextView nalLo;

  @NonNull
  public final RecyclerView recyclerViewBanner;

  @NonNull
  public final RecyclerView recyclerViewBestReview;

  @NonNull
  public final RecyclerView recyclerViewComparingPrice;

  @NonNull
  public final TextView seeingMore;

  @NonNull
  public final LinearLayout topLayout;

  private ActivityMainBinding(@NonNull RelativeLayout rootView,
      @NonNull BottomNavigationView bnvMain, @NonNull TextView fishName1,
      @NonNull TextView fishName2, @NonNull TextView fishPrice1, @NonNull TextView fishPrice2,
      @NonNull ImageView imageView2, @NonNull ImageView imageView3,
      @NonNull SearchView mainSearchbar, @NonNull ActionMenuView menubar1,
      @NonNull LinearLayout menubar3, @NonNull LinearLayout menubar4, @NonNull TextView nalLo,
      @NonNull RecyclerView recyclerViewBanner, @NonNull RecyclerView recyclerViewBestReview,
      @NonNull RecyclerView recyclerViewComparingPrice, @NonNull TextView seeingMore,
      @NonNull LinearLayout topLayout) {
    this.rootView = rootView;
    this.bnvMain = bnvMain;
    this.fishName1 = fishName1;
    this.fishName2 = fishName2;
    this.fishPrice1 = fishPrice1;
    this.fishPrice2 = fishPrice2;
    this.imageView2 = imageView2;
    this.imageView3 = imageView3;
    this.mainSearchbar = mainSearchbar;
    this.menubar1 = menubar1;
    this.menubar3 = menubar3;
    this.menubar4 = menubar4;
    this.nalLo = nalLo;
    this.recyclerViewBanner = recyclerViewBanner;
    this.recyclerViewBestReview = recyclerViewBestReview;
    this.recyclerViewComparingPrice = recyclerViewComparingPrice;
    this.seeingMore = seeingMore;
    this.topLayout = topLayout;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bnv_main;
      BottomNavigationView bnvMain = ViewBindings.findChildViewById(rootView, id);
      if (bnvMain == null) {
        break missingId;
      }

      id = R.id.fish_name1;
      TextView fishName1 = ViewBindings.findChildViewById(rootView, id);
      if (fishName1 == null) {
        break missingId;
      }

      id = R.id.fish_name2;
      TextView fishName2 = ViewBindings.findChildViewById(rootView, id);
      if (fishName2 == null) {
        break missingId;
      }

      id = R.id.fish_price1;
      TextView fishPrice1 = ViewBindings.findChildViewById(rootView, id);
      if (fishPrice1 == null) {
        break missingId;
      }

      id = R.id.fish_price2;
      TextView fishPrice2 = ViewBindings.findChildViewById(rootView, id);
      if (fishPrice2 == null) {
        break missingId;
      }

      id = R.id.imageView2;
      ImageView imageView2 = ViewBindings.findChildViewById(rootView, id);
      if (imageView2 == null) {
        break missingId;
      }

      id = R.id.imageView3;
      ImageView imageView3 = ViewBindings.findChildViewById(rootView, id);
      if (imageView3 == null) {
        break missingId;
      }

      id = R.id.mainSearchbar;
      SearchView mainSearchbar = ViewBindings.findChildViewById(rootView, id);
      if (mainSearchbar == null) {
        break missingId;
      }

      id = R.id.menubar1;
      ActionMenuView menubar1 = ViewBindings.findChildViewById(rootView, id);
      if (menubar1 == null) {
        break missingId;
      }

      id = R.id.menubar3;
      LinearLayout menubar3 = ViewBindings.findChildViewById(rootView, id);
      if (menubar3 == null) {
        break missingId;
      }

      id = R.id.menubar4;
      LinearLayout menubar4 = ViewBindings.findChildViewById(rootView, id);
      if (menubar4 == null) {
        break missingId;
      }

      id = R.id.nalLo;
      TextView nalLo = ViewBindings.findChildViewById(rootView, id);
      if (nalLo == null) {
        break missingId;
      }

      id = R.id.recyclerViewBanner;
      RecyclerView recyclerViewBanner = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewBanner == null) {
        break missingId;
      }

      id = R.id.recyclerViewBestReview;
      RecyclerView recyclerViewBestReview = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewBestReview == null) {
        break missingId;
      }

      id = R.id.recyclerViewComparingPrice;
      RecyclerView recyclerViewComparingPrice = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewComparingPrice == null) {
        break missingId;
      }

      id = R.id.seeingMore;
      TextView seeingMore = ViewBindings.findChildViewById(rootView, id);
      if (seeingMore == null) {
        break missingId;
      }

      id = R.id.topLayout;
      LinearLayout topLayout = ViewBindings.findChildViewById(rootView, id);
      if (topLayout == null) {
        break missingId;
      }

      return new ActivityMainBinding((RelativeLayout) rootView, bnvMain, fishName1, fishName2,
          fishPrice1, fishPrice2, imageView2, imageView3, mainSearchbar, menubar1, menubar3,
          menubar4, nalLo, recyclerViewBanner, recyclerViewBestReview, recyclerViewComparingPrice,
          seeingMore, topLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
