package com.jimg.scoutingapp.asynctasks;

import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.jimg.scoutingapp.MainActivity;
import com.jimg.scoutingapp.R;
import com.jimg.scoutingapp.helpers.ErrorHelpers;
import com.jimg.scoutingapp.utilityclasses.Pair;

import java.io.IOException;

/**
 * Created by Jim on 3/8/14.
 */
public class GetAuthTokenAsyncTask extends AsyncTask<Pair<MainActivity, GoogleApiClient>, Void, String> {
    private MainActivity mMainActivity;

    @Override
    protected String doInBackground(Pair<MainActivity, GoogleApiClient>... pairs) {
        Pair<MainActivity, GoogleApiClient> inputParameter = pairs[0];
        mMainActivity = inputParameter.first;

        return getAuthToken(inputParameter);
    }

    private String getAuthToken(Pair<MainActivity, GoogleApiClient> param) {
        String accessToken = null;
        try {
            accessToken = GoogleAuthUtil.getToken(param.first,
                    Plus.AccountApi.getAccountName(param.second),
                    "oauth2:" + Scopes.PLUS_LOGIN);
        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            ErrorHelpers.handleError(mMainActivity.getString(R.string.failed_to_obtain_auth_token), transientEx.getMessage(), ErrorHelpers.getStackTraceAsString(transientEx), mMainActivity);
        } catch (UserRecoverableAuthException e) {
            ErrorHelpers.handleError(mMainActivity.getString(R.string.failed_to_obtain_auth_token), e.getMessage(), ErrorHelpers.getStackTraceAsString(e), mMainActivity);
            // Recover
            accessToken = null;
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            ErrorHelpers.handleError(mMainActivity.getString(R.string.failed_to_obtain_auth_token), authEx.getMessage(), ErrorHelpers.getStackTraceAsString(authEx), mMainActivity);
        } catch (Exception e) {
            ErrorHelpers.handleError(mMainActivity.getString(R.string.failed_to_obtain_auth_token), e.getMessage(), ErrorHelpers.getStackTraceAsString(e), mMainActivity);
            throw new RuntimeException(e);
        }

        return accessToken;
    }

    @Override
    protected void onPostExecute(String authToken) {
        mMainActivity.updateUiForSignIn(authToken);
        super.onPostExecute(authToken);
    }
}
