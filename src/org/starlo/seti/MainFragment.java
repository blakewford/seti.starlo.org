package org.starlo.seti;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.accounts.*;
import android.content.DialogInterface;

import com.google.android.gms.auth.*;
import com.google.android.gms.common.Scopes;

import java.io.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;

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

         private String httpGetResponse(String url, String token) throws Exception
         {

             StringBuilder sb = new StringBuilder();
             HttpGet httpGet = new HttpGet(url);
             httpGet.setHeader("Authorization", "OAuth " + token);
             httpGet.setHeader("Gdata-version", "3.0");
             HttpEntity entity = new DefaultHttpClient().execute(httpGet).getEntity();
             if (entity != null)
             {
                 InputStream instream = entity.getContent();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                 String line = null;
                 while ((line = reader.readLine()) != null) {
                     sb.append(line);
                 }
                 instream.close();
             }
             return sb.toString();
        }

        protected String[] doInBackground(String... email)
        {
            final String emailAddress = email[0];
            try{
                String token = GoogleAuthUtil.getToken(getActivity(), email[0], "oauth2:"+"https://www.google.com/m8/feeds");
                String response = httpGetResponse("https://www.google.com/m8/feeds/contacts/"+emailAddress+"/full?alt=json", token);
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
            return new String[]{"Blake Ford, blake.wford@gmail.com"};
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
