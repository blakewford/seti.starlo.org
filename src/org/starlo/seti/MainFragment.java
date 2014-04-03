package org.starlo.seti;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MainFragment extends ListFragment {

    private String[] mContacts = new String[] { "Blake W. Ford" };

    public MainFragment(){}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mContacts));
        super.onViewCreated(view, savedInstanceState);
    }

};
