// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemBestReviewBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final ImageView reviewImage;

  @NonNull
  public final TextView reviewRating;

  private ItemBestReviewBinding(@NonNull FrameLayout rootView, @NonNull ImageView reviewImage,
      @NonNull TextView reviewRating) {
    this.rootView = rootView;
    this.reviewImage = reviewImage;
    this.reviewRating = reviewRating;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemBestReviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemBestReviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_best_review, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemBestReviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.reviewImage;
      ImageView reviewImage = ViewBindings.findChildViewById(rootView, id);
      if (reviewImage == null) {
        break missingId;
      }

      id = R.id.reviewRating;
      TextView reviewRating = ViewBindings.findChildViewById(rootView, id);
      if (reviewRating == null) {
        break missingId;
      }

      return new ItemBestReviewBinding((FrameLayout) rootView, reviewImage, reviewRating);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
