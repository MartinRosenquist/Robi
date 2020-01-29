package com.example.robo_robi.ui.support;

        import android.app.Activity;
        import android.icu.text.IDNA;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.Nullable;
        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentTransaction;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProviders;

        import com.example.robo_robi.MainActivity;
        import com.example.robo_robi.R;
        import com.example.robo_robi.ui.info.InfoFragment;


public class SupportFragment extends Fragment {

    private SupportViewModel supportViewModel;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        supportViewModel =
                ViewModelProviders.of(this).get(SupportViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_support, container, false);
        onResume("Support");
        final TextView ticketTextView = root.findViewById(R.id.text_Ticket);
        final TextView businessTextView = root.findViewById(R.id.text_Business);
        final TextView infoTextView = root.findViewById(R.id.text_info);

        supportViewModel.getTicket().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String ticket) {
                ticketTextView.setText(ticket);
            }
        });

        supportViewModel.getBusiness().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String business) {
                businessTextView.setText(business);
            }
        });

        supportViewModel.getInfo().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String info) {
                infoTextView.setText(info);
            }
        });

        infoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ShowInfoFragment();



            }
        });

        return root;
    }

    private void ShowInfoFragment(){
        Fragment fr = new InfoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fr);
        transaction.addToBackStack(null);
        transaction.commit();
        onResume("Info");
    }

    private void onResume(String title){
        super.onResume();

        // Set title bar
        ((MainActivity) getActivity())
                .setActionBarTitle(title);

    }
}