package com.vodkasoft.canyousinkme.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.vodkasoft.canyousinkme.dataaccess.BackendServiceAccessor;
import com.vodkasoft.canyousinkme.dataaccess.model.User;
import com.vodkasoft.canyousinkme.gamelogic.IConstant;

public class FacebookAuthFragment extends Fragment implements IConstant {

    private static final String DEFAULT_AVATAR = "No image";
    private static final String DEFAULT_COUNTRY_CODE = "ZZ";
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state,
                         final Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private String mSessionToken;
    private UiLifecycleHelper uiHelper;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.Facebook_btn);
        authButton.setFragment(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    public void showToastWithMessage(CharSequence ToastText) {
        Toast toast = Toast.makeText(getActivity(), ToastText,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened() && mSessionToken != session.getAccessToken() && exception == null) {
            mSessionToken = session.getAccessToken();
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        String avatar = DEFAULT_AVATAR;
                        final String facebookId = user.getId();
                        String displayname = user.getName();
                        String countryCode = DEFAULT_COUNTRY_CODE;
                        Intent intent = new Intent(getActivity(), WelcomeFB.class);
                        startActivity(intent);
                        FBSession.setName(displayname);
                        FBSession.setFacebookID(facebookId);
                        FBSession.setCountryCode(countryCode);

                        final BackendServiceAccessor backendServiceAccessor = new BackendServiceAccessor(
                                BACKEND_HOST, getActivity().getApplicationContext());

                        backendServiceAccessor.getUser(facebookId, new BackendServiceAccessor.Listener<User>() {
                            @Override
                            public void onError(String message) {
                                backendServiceAccessor.createUser(facebookId, new BackendServiceAccessor.Listener<String>() {
                                    @Override
                                    public void onError(String message) {
                                        Toast toast = Toast.makeText(getActivity(), USER_ERROR,
                                                Toast.LENGTH_LONG);
                                        toast.show();
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Intent intent = new Intent(getActivity(), MenuFB.class);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onResponse(User response) {
                                Intent intent = new Intent(getActivity(), MenuFB.class);
                                startActivity(intent);
                            }
                        });


                    }

                }
            }).executeAsync();
        } else if (state.isClosed()) {
            mSessionToken = null;
        }
    }
}
