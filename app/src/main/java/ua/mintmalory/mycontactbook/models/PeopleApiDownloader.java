package ua.mintmalory.mycontactbook.models;

import android.os.AsyncTask;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ua.mintmalory.mycontactbook.App;
import ua.mintmalory.mycontactbook.R;


public class PeopleApiDownloader {
    public interface OnPeopleDownloadedListener {
        void onDownloaded(List<Contact> contacts);
    }


    private final String CONNECTIONS_LIST = "people/me";
    private final String REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob";
    private final String CONNECTIONS_INFO_FILTER = "person.names,person.emailAddresses,person.phoneNumbers,person.photos";
    private String code = "";
    private volatile People peopleService;
    private OnPeopleDownloadedListener listener;

    public PeopleApiDownloader(String code, OnPeopleDownloadedListener listener) {
        this.code = code;
        this.listener = listener;
        new SetUp().execute();
    }

    public void downloadContacts() {
        if (peopleService != null) {
            new Downloader().execute();
        }
    }

    private class SetUp extends AsyncTask<Void, Void, People> {

        @Override
        protected People doInBackground(Void... voids) {
            try {
                HttpTransport httpTransport = new NetHttpTransport();
                JacksonFactory jsonFactory = new JacksonFactory();

                String clientId = App.getInstance().getApplicationContext().getString(R.string.client_id);
                String clientSecret = App.getInstance().getApplicationContext().getString(R.string.client_secret);

                GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        httpTransport, jsonFactory, clientId, clientSecret, code, REDIRECT_URL).execute();

                GoogleCredential credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(clientId, clientSecret)
                        .build()
                        .setFromTokenResponse(tokenResponse);

                return new People.Builder(httpTransport, jsonFactory, credential)
                        .build();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(People people) {
            super.onPostExecute(people);
            peopleService = people;
        }
    }

    private class Downloader extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> result = new ArrayList<>();

            try {
                ListConnectionsResponse response = peopleService.people().connections()
                        .list(CONNECTIONS_LIST)
                        .setRequestMaskIncludeField(CONNECTIONS_INFO_FILTER)
                        .execute();

                result = convertConnectionsToContacts(response.getConnections());

            } catch (IOException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            listener.onDownloaded(contacts);

        }

        private List<Contact> convertConnectionsToContacts(List<Person> connections) {
            List<Contact> result = new ArrayList<>();

            if (connections == null) {
                return result;
            }

            Contact c;
            for (Person person : connections) {
                c = new Contact();

                if (person.getNames() != null && person.getNames().size() > 0) {
                    c.setName(person.getNames().get(0).getDisplayName());
                } else if (person.getEmailAddresses() != null && person.getEmailAddresses().size() > 0) {
                    c.setName(person.getEmailAddresses().get(0).getValue());
                } else if (person.getPhoneNumbers() != null && person.getPhoneNumbers().size() > 0) {
                    c.setName(person.getPhoneNumbers().get(0).getValue());
                } else {
                    c.setName("");
                }

                c.setAvatarURI(person.getPhotos().get(0).getUrl());

                if (person.getPhoneNumbers() != null) {
                    for (PhoneNumber p : person.getPhoneNumbers()) {
                        c.addPhoneNumber(p.getValue());
                    }
                }

                if (person.getEmailAddresses() != null) {
                    for (EmailAddress e : person.getEmailAddresses()) {
                        c.addEmail(e.getValue());
                    }
                }

                result.add(c);
            }

            return result;
        }
    }


    public void setUp() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();

        String clientId = App.getInstance().getApplicationContext().getString(R.string.client_id);
        String clientSecret = App.getInstance().getApplicationContext().getString(R.string.client_secret);

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                httpTransport, jsonFactory, clientId, clientSecret, code, REDIRECT_URL).execute();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);

        peopleService = new People.Builder(httpTransport, jsonFactory, credential)
                .build();
    }
}