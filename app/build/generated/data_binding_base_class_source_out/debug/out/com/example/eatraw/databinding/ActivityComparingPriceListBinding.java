// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityComparingPriceListBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView imgBackarrow;

  @NonNull
  public final SearchView mainSearchbar;

  @NonNull
  public final RecyclerView recyclerViewComparingPrice;

  private ActivityComparingPriceListBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView imgBackarrow, @NonNull SearchView mainSearchbar,
      @NonNull RecyclerView recyclerViewComparingPrice) {
    this.rootView = rootView;
    this.imgBackarrow = imgBackarrow;
    this.mainSearchbar = mainSearchbar;
    this.recyclerViewComparingPrice = recyclerViewComparingPrice;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityComparingPriceListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityComparingPriceListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_comparing_price_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityComparingPriceListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.img_backarrow;
      ImageView imgBackarrow = ViewBindings.findChildViewById(rootView, id);
      if (imgBackarrow == null) {
        break missingId;
      }

      id = R.id.mainSearchbar;
      SearchView mainSearchbar = ViewBindings.findChildViewById(rootView, id);
      if (mainSearchbar == null) {
        break missingId;
      }

      id = R.id.recyclerViewComparingPrice;
      RecyclerView recyclerViewComparingPrice = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewComparingPrice == null) {
        break missingId;
      }

      return new ActivityComparingPriceListBinding((LinearLayout) rootView, imgBackarrow,
          mainSearchbar, recyclerViewComparingPrice);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
