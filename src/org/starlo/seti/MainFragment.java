package org.starlo.seti;

import android.app.ListFragment;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainFragment extends ListFragment {

    public MainFragment(){}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        new AsyncBuilder().execute();
        super.onViewCreated(view, savedInstanceState);
    }

    private class AsyncBuilder extends AsyncTask<Void, Void, String[]>
    {

         protected String[] doInBackground(Void... nothing)
         {
             return new String[] { "Blake W. Ford" };
         }

         protected void onPostExecute(String[] result)
         {
             setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result));
         }

    }

};
