package com.example.android.whatsapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.android.whatsapp.freagments.CallFragment;
import com.example.android.whatsapp.freagments.ChatsFragment;
import com.example.android.whatsapp.freagments.StatusFragment;

public class fragmentAdapter extends FragmentStateAdapter {

    public fragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public fragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public fragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new ChatsFragment();
            case  1 :
                return new StatusFragment();

           // case 2 :
              //  return new CallFragment();
            default: new ChatsFragment();
        }
        return new ChatsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
