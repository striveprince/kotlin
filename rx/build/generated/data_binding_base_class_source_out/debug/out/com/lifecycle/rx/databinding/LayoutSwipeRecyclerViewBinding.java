// Generated by data binding compiler. Do not edit!
package com.lifecycle.rx.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.lifecycle.rx.R;
import com.lifecycle.rx.viewmodel.list.ListViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class LayoutSwipeRecyclerViewBinding extends ViewDataBinding {
  @NonNull
  public final FrameLayout frameLayout;

  @NonNull
  public final RecyclerView recyclerView;

  @NonNull
  public final SmartRefreshLayout smartRefreshLayout;

  @Bindable
  protected ListViewModel mVm;

  protected LayoutSwipeRecyclerViewBinding(Object _bindingComponent, View _root,
      int _localFieldCount, FrameLayout frameLayout, RecyclerView recyclerView,
      SmartRefreshLayout smartRefreshLayout) {
    super(_bindingComponent, _root, _localFieldCount);
    this.frameLayout = frameLayout;
    this.recyclerView = recyclerView;
    this.smartRefreshLayout = smartRefreshLayout;
  }

  public abstract void setVm(@Nullable ListViewModel vm);

  @Nullable
  public ListViewModel getVm() {
    return mVm;
  }

  @NonNull
  public static LayoutSwipeRecyclerViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.layout_swipe_recycler_view, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static LayoutSwipeRecyclerViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<LayoutSwipeRecyclerViewBinding>inflateInternal(inflater, R.layout.layout_swipe_recycler_view, root, attachToRoot, component);
  }

  @NonNull
  public static LayoutSwipeRecyclerViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.layout_swipe_recycler_view, null, false, component)
   */
  @NonNull
  @Deprecated
  public static LayoutSwipeRecyclerViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<LayoutSwipeRecyclerViewBinding>inflateInternal(inflater, R.layout.layout_swipe_recycler_view, null, false, component);
  }

  public static LayoutSwipeRecyclerViewBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static LayoutSwipeRecyclerViewBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (LayoutSwipeRecyclerViewBinding)bind(component, view, R.layout.layout_swipe_recycler_view);
  }
}