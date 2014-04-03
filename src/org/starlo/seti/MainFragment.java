package org.starlo.seti;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.accounts.*;
import com.google.android.gms.auth.*;
import com.google.android.gms.common.Scopes;
import android.content.DialogInterface;

public class MainFragment extends ListFragment {

    private AccountManager mAccountManager = null;

    public MainFragment(){}

    public void onAttach(Activity activity)
    {
        mAccountManager = AccountManager.get(activity);
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        new AccountsDialogFragment().show(getActivity().getFragmentManager(), null);
        super.onViewCreated(view, savedInstanceState);
    }

    private class AsyncBuilder extends AsyncTask<String, Void, String[]>
    {

        protected String[] doInBackground(String... email)
        {
            try{
                GoogleAuthUtil.getToken(getActivity(), email[0], Scopes.PLUS_LOGIN);
            }catch(UserRecoverableAuthException e){
                startActivityForResult(e.getIntent(), 0);
            }catch(Exception e){
                final Exception copy = e;
                getActivity().runOnUiThread(
                    new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(getActivity(), copy.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                );
            }
            return new String[]{};
        }

        protected void onPostExecute(String[] result)
        {
            setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, result));
        }

    }

    private class AccountsDialogFragment extends DialogFragment
    {

        private String[] getAccountNames()
        {
            Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            String[] names = new String[accounts.length];
            for (int i = 0; i < names.length; i++)
            {
                names[i] = accounts[i].name;
            }
            return names;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            setRetainInstance(true);
            return new AlertDialog.Builder(getActivity())
            .setCancelable(false)
            .setItems(getAccountNames(),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int item)
                    {
                        new AsyncBuilder().execute(getAccountNames()[item]);
                    }
                }
            ).create();
        }

    }

};
