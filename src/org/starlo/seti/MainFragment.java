package org.starlo.seti;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;

public class MainFragment extends Fragment {

    public MainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_list, null);
    }

};
