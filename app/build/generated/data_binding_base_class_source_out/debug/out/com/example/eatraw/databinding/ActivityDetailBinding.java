// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityDetailBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView detailRecylerView;

  @NonNull
  public final LinearLayout menubar1;

  @NonNull
  public final TextView nalLo;

  @NonNull
  public final LinearLayout topLayout;

  private ActivityDetailBinding(@NonNull LinearLayout rootView,
      @NonNull RecyclerView detailRecylerView, @NonNull LinearLayout menubar1,
      @NonNull TextView nalLo, @NonNull LinearLayout topLayout) {
    this.rootView = rootView;
    this.detailRecylerView = detailRecylerView;
    this.menubar1 = menubar1;
    this.nalLo = nalLo;
    this.topLayout = topLayout;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_detail, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityDetailBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.detailRecylerView;
      RecyclerView detailRecylerView = ViewBindings.findChildViewById(rootView, id);
      if (detailRecylerView == null) {
        break missingId;
      }

      id = R.id.menubar1;
      LinearLayout menubar1 = ViewBindings.findChildViewById(rootView, id);
      if (menubar1 == null) {
        break missingId;
      }

      id = R.id.nalLo;
      TextView nalLo = ViewBindings.findChildViewById(rootView, id);
      if (nalLo == null) {
        break missingId;
      }

      id = R.id.topLayout;
      LinearLayout topLayout = ViewBindings.findChildViewById(rootView, id);
      if (topLayout == null) {
        break missingId;
      }

      return new ActivityDetailBinding((LinearLayout) rootView, detailRecylerView, menubar1, nalLo,
          topLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
