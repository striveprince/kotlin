package com.lifecycle.rx.databinding;
import com.lifecycle.rx.R;
import com.lifecycle.rx.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LayoutSwipeRecyclerViewBindingImpl extends LayoutSwipeRecyclerViewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView4;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener smartRefreshLayoutandroidStateAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of vm.loadingState.getValue()
            //         is vm.loadingState.setValue((java.lang.Integer) callbackArg_0)
            int callbackArg_0 = com.lifecycle.binding.adapter.databinding.smartrefresh.SmartRefreshLayoutBindingAdapter.getState(smartRefreshLayout);
            // localize variables for thread safety
            // vm.loadingState
            androidx.lifecycle.MutableLiveData<java.lang.Integer> vmLoadingState = null;
            // vm != null
            boolean vmJavaLangObjectNull = false;
            // vm
            com.lifecycle.rx.viewmodel.list.ListViewModel vm = mVm;
            // vm.loadingState != null
            boolean vmLoadingStateJavaLangObjectNull = false;
            // vm.loadingState.getValue()
            java.lang.Integer vmLoadingStateGetValue = null;



            vmJavaLangObjectNull = (vm) != (null);
            if (vmJavaLangObjectNull) {


                vmLoadingState = vm.getLoadingState();

                vmLoadingStateJavaLangObjectNull = (vmLoadingState) != (null);
                if (vmLoadingStateJavaLangObjectNull) {




                    vmLoadingState.setValue(((java.lang.Integer) (callbackArg_0)));
                }
            }
        }
    };

    public LayoutSwipeRecyclerViewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private LayoutSwipeRecyclerViewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (android.widget.FrameLayout) bindings[3]
            , (androidx.recyclerview.widget.RecyclerView) bindings[2]
            , (com.scwang.smart.refresh.layout.SmartRefreshLayout) bindings[1]
            );
        this.frameLayout.setTag(null);
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.recyclerView.setTag(null);
        this.smartRefreshLayout.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.vm == variableId) {
            setVm((com.lifecycle.rx.viewmodel.list.ListViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVm(@Nullable com.lifecycle.rx.viewmodel.list.ListViewModel Vm) {
        this.mVm = Vm;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.vm);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmLoadingState((androidx.lifecycle.MutableLiveData<java.lang.Integer>) object, fieldId);
            case 1 :
                return onChangeVmError((androidx.lifecycle.MutableLiveData<java.lang.Throwable>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmLoadingState(androidx.lifecycle.MutableLiveData<java.lang.Integer> VmLoadingState, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmError(androidx.lifecycle.MutableLiveData<java.lang.Throwable> VmError, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        boolean vmErrorJavaLangObjectNull = false;
        java.lang.String vmErrorMessage = null;
        int vmErrorJavaLangObjectNullViewGONEViewVISIBLE = 0;
        boolean vmLoadingStateInt0 = false;
        java.lang.Throwable vmErrorGetValue = null;
        androidx.lifecycle.MutableLiveData<java.lang.Integer> vmLoadingState = null;
        com.lifecycle.rx.viewmodel.list.ListViewModel<? extends com.lifecycle.binding.inter.inflate.Inflate> vm = mVm;
        androidx.lifecycle.MutableLiveData<java.lang.Throwable> vmError = null;
        java.lang.Integer vmLoadingStateGetValue = null;
        int androidxDatabindingViewDataBindingSafeUnboxVmLoadingStateGetValue = 0;
        com.lifecycle.rx.IListAdapter<? extends com.lifecycle.binding.inter.inflate.Inflate> vmAdapter = null;

        if ((dirtyFlags & 0xfL) != 0) {


            if ((dirtyFlags & 0xdL) != 0) {

                    if (vm != null) {
                        // read vm.loadingState
                        vmLoadingState = vm.getLoadingState();
                    }
                    updateLiveDataRegistration(0, vmLoadingState);


                    if (vmLoadingState != null) {
                        // read vm.loadingState.getValue()
                        vmLoadingStateGetValue = vmLoadingState.getValue();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vm.loadingState.getValue())
                    androidxDatabindingViewDataBindingSafeUnboxVmLoadingStateGetValue = androidx.databinding.ViewDataBinding.safeUnbox(vmLoadingStateGetValue);


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vm.loadingState.getValue()) == 0
                    vmLoadingStateInt0 = (androidxDatabindingViewDataBindingSafeUnboxVmLoadingStateGetValue) == (0);
            }
            if ((dirtyFlags & 0xeL) != 0) {

                    if (vm != null) {
                        // read vm.error
                        vmError = vm.getError();
                    }
                    updateLiveDataRegistration(1, vmError);


                    if (vmError != null) {
                        // read vm.error.getValue()
                        vmErrorGetValue = vmError.getValue();
                    }


                    // read vm.error.getValue() == null
                    vmErrorJavaLangObjectNull = (vmErrorGetValue) == (null);
                if((dirtyFlags & 0xeL) != 0) {
                    if(vmErrorJavaLangObjectNull) {
                            dirtyFlags |= 0x20L;
                    }
                    else {
                            dirtyFlags |= 0x10L;
                    }
                }
                    if (vmErrorGetValue != null) {
                        // read vm.error.getValue().message
                        vmErrorMessage = vmErrorGetValue.getMessage();
                    }


                    // read vm.error.getValue() == null ? View.GONE : View.VISIBLE
                    vmErrorJavaLangObjectNullViewGONEViewVISIBLE = ((vmErrorJavaLangObjectNull) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
            }
            if ((dirtyFlags & 0xcL) != 0) {

                    if (vm != null) {
                        // read vm.adapter
                        vmAdapter = vm.getAdapter();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0xeL) != 0) {
            // api target 1

            this.frameLayout.setVisibility(vmErrorJavaLangObjectNullViewGONEViewVISIBLE);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView4, vmErrorMessage);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            com.lifecycle.binding.adapter.databinding.ViewBindingAdapter.setAdapter(this.recyclerView, vmAdapter);
        }
        if ((dirtyFlags & 0xdL) != 0) {
            // api target 1

            this.smartRefreshLayout.setEnabled(vmLoadingStateInt0);
            com.lifecycle.binding.adapter.databinding.smartrefresh.SmartRefreshLayoutBindingAdapter.setState(this.smartRefreshLayout, androidxDatabindingViewDataBindingSafeUnboxVmLoadingStateGetValue);
        }
        if ((dirtyFlags & 0x8L) != 0) {
            // api target 1

            com.lifecycle.binding.adapter.databinding.smartrefresh.SmartRefreshLayoutBindingAdapter.setRefreshingListener(this.smartRefreshLayout, (com.scwang.smart.refresh.layout.listener.OnLoadMoreListener)null, (com.scwang.smart.refresh.layout.listener.OnRefreshListener)null, smartRefreshLayoutandroidStateAttrChanged);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vm.loadingState
        flag 1 (0x2L): vm.error
        flag 2 (0x3L): vm
        flag 3 (0x4L): null
        flag 4 (0x5L): vm.error.getValue() == null ? View.GONE : View.VISIBLE
        flag 5 (0x6L): vm.error.getValue() == null ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}