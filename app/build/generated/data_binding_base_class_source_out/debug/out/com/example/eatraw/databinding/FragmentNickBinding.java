// Generated by view binder compiler. Do not edit!
package com.example.eatraw.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.eatraw.R;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentNickBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageButton backButton;

  @NonNull
  public final Button btnRegister;

  @NonNull
  public final Button nickcheck;

  @NonNull
  public final TextInputEditText nickname;

  @NonNull
  public final ImageView thumbnail;

  private FragmentNickBinding(@NonNull LinearLayout rootView, @NonNull ImageButton backButton,
      @NonNull Button btnRegister, @NonNull Button nickcheck, @NonNull TextInputEditText nickname,
      @NonNull ImageView thumbnail) {
    this.rootView = rootView;
    this.backButton = backButton;
    this.btnRegister = btnRegister;
    this.nickcheck = nickcheck;
    this.nickname = nickname;
    this.thumbnail = thumbnail;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentNickBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentNickBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_nick, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentNickBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.backButton;
      ImageButton backButton = ViewBindings.findChildViewById(rootView, id);
      if (backButton == null) {
        break missingId;
      }

      id = R.id.btn_register;
      Button btnRegister = ViewBindings.findChildViewById(rootView, id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.nickcheck;
      Button nickcheck = ViewBindings.findChildViewById(rootView, id);
      if (nickcheck == null) {
        break missingId;
      }

      id = R.id.nickname;
      TextInputEditText nickname = ViewBindings.findChildViewById(rootView, id);
      if (nickname == null) {
        break missingId;
      }

      id = R.id.thumbnail;
      ImageView thumbnail = ViewBindings.findChildViewById(rootView, id);
      if (thumbnail == null) {
        break missingId;
      }

      return new FragmentNickBinding((LinearLayout) rootView, backButton, btnRegister, nickcheck,
          nickname, thumbnail);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
