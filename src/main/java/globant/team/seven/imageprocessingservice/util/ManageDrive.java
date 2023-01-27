package globant.team.seven.imageprocessingservice.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ManageDrive {

    private static final String APPLICATION_NAME = "StorageImages";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TYPE_READ = "read";
    private static final String TYPE_WRITE = "write";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES_READ = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final List<String> SCOPES_WRITE = Collections.singletonList(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    /**
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     */
    private  Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, String type)
            throws IOException {
        // client secrets.
        InputStream in = ManageDrive.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        List<String> SCOPE = type.equals(TYPE_READ) ? SCOPES_READ : SCOPES_WRITE;
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPE)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        return credential;
    }

    private void uploadToFolder(String fileName, java.io.File javaFile, String driveFolderId, String description) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, TYPE_WRITE))
                .setApplicationName(APPLICATION_NAME)
                .build();
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setDescription(description);
        fileMetadata.setParents(Collections.singletonList(driveFolderId));
        FileContent mediaContent = new FileContent("image/jpeg", javaFile);
        try {
            service.files().create(fileMetadata, mediaContent)
                    .setFields("id, parents")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    private String createFolder(String name, String parent) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, TYPE_WRITE))
                .setApplicationName(APPLICATION_NAME)
                .build();
        List<String> parents = new ArrayList<>();
        if(parent != null){
            parents.add(parent);
        }
        File fileMetadata = new File();
        fileMetadata.setName(name);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        if(parents != null && !parents.isEmpty()) {
            fileMetadata.setParents(parents);
        }
        try {
            File file = service.files().create(fileMetadata)
                    .setFields("id")
                    .execute();
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            throw e;
        }
    }
    private String searchIdFolder(String folderName) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, TYPE_WRITE))
                .setApplicationName(APPLICATION_NAME)
                .build();
        List<File> files = new ArrayList<File>();
        String pageToken = null;
        do {
            FileList result = service.files().list()
                    .setQ("mimeType = 'application/vnd.google-apps.folder' and name='" + folderName + "'")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name, parents)")
                    .setPageToken(pageToken)
                    .execute();
            for (File file : result.getFiles()) {
                System.out.printf("Found file: %s (%s)\n",
                        file.getName(), file.getId());
            }
            files.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        if (files.isEmpty()){
            return null;
        } else {
            return files.get(0).getId();
        }
    }
    public void processImage(String fileName, java.io.File javaFile, String folder, String description) throws IOException, GeneralSecurityException{
        String id = searchIdFolder(folder);
        if(id == null){
            id = createFolder(folder, null);
            System.out.println("Folder creado: " + id);
        } else {
            System.out.println("Folder encontrado: " + id);
        }
        uploadToFolder(fileName, javaFile, id, description);
    }

}
