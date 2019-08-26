package com.zaaibo.tolet.repositories.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zaaibo.tolet.models.Feedback;
import com.zaaibo.tolet.models.Filter;
import com.zaaibo.tolet.utils.FilterResult;
import com.zaaibo.tolet.models.PostAd;
import com.zaaibo.tolet.models.User;
import com.zaaibo.tolet.utils.ConstantKey;

import java.util.ArrayList;

public class FirebaseRepository implements IFirebaseRepository {

    private static final String TAG = "FirebaseRepository";
    private DatabaseReference mDatabaseRef;

    /**********************************************************************************
     * Get/Fetch
     ***********************************************************************************/

    @Override
    public User getUserById(String mAuthId) {
        return null;
    }

    @Override
    public ArrayList<PostAd> getPostAdList() {
        return null;
    }

    @Override
    public ArrayList<PostAd> getAllPostAdByFilter(Filter mFilter) {
        return null;
    }

    @Override
    public PostAd getPostAdById(String mAuthId) {
        return null;
    }

    @Override
    public ArrayList<User> getAllNotificationById(String mAuthId) {
        return null;
    }

    @Override
    public ArrayList<Feedback> getAllFeedback(String mAuthId) {
        return null;
    }

    /**********************************************************************************
     * Save
     ***********************************************************************************/

    @Override
    public String saveUser(User mUser) {
        return null;
    }

    @Override
    public String savePostAd(PostAd mPostAd) {
        return null;
    }

    @Override
    public String saveNotification(String mOwnerAuthId, User mUser) {
        return null;
    }

    @Override
    public String saveFeedback(Feedback mFeedback) {
        return null;
    }

    /**********************************************************************************
     * Save Image
     ***********************************************************************************/

    @Override
    public String saveImageByPath(Uri uri, String mDirectory, String mAuthId) {
        return null;
    }

    /**********************************************************************************
     * Delete
     ***********************************************************************************/

    @Override
    public String deleteFeedbackById(String mAuthId) {
        return null;
    }

    @Override
    public String deletePostAdById(String mOwnerAuthId) {
        return null;
    }

    @Override
    public String deleteImageByUrl(String mImageUrl) {
        return null;
    }

    //===============================================| Get
    public void getUserData(final UserCallback mCallback, String mUserAuthId) {
        Query query = FirebaseDatabase.getInstance().getReference(ConstantKey.USER_NODE).child(mUserAuthId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    User model = dataSnapshot.getValue(User.class);
                    mCallback.onCallback(model);
                } else {
                    mCallback.onCallback(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "" + databaseError.getMessage());
                mCallback.onCallback(null);
            }
        });
    }

    public interface UserCallback {
        void onCallback(User model);
    }

    public void getAllPostAd(final PostsAdCallback mCallback) {
        ArrayList<PostAd> arrayList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.USER_POST_NODE);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        PostAd model = snapshot.getValue(PostAd.class);
                        arrayList.add(model);
                        mCallback.onCallback(arrayList);
                    }
                } else {
                    mCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "" + databaseError.getMessage());
                mCallback.onCallback(null);
            }
        });
    }
    public interface PostsAdCallback {
        void onCallback(ArrayList<PostAd> arrayList);
    }

    public void getAllPostAdByLocation(final PostsAdCallback mCallback, Filter filter) {
        ArrayList<PostAd> arrayList = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child(ConstantKey.USER_POST_NODE).orderByChild(PostAd.getLocationFieldName()).equalTo(filter.getLocation());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        PostAd model = FilterResult.filters(snapshot.getValue(PostAd.class), filter);
                        if (model != null) {
                            arrayList.add(model);
                            mCallback.onCallback(arrayList);
                        }
                    }
                } else {
                    mCallback.onCallback(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "" + databaseError.getMessage());
                mCallback.onCallback(null);
            }
        });
    }

    public void getPostAdById(final PostAdCallback mCallback, String mAuthId) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.USER_POST_NODE).child(mAuthId);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    String key = dataSnapshot.getKey();
                    PostAd model = dataSnapshot.getValue(PostAd.class);
                    mCallback.onCallback(model);
                } else {
                    mCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "" + databaseError.getMessage());
                mCallback.onCallback(null);
            }
        });
    }
    public interface PostAdCallback {
        void onCallback(PostAd model);
    }

    public void getNotifications(final NotificationCallback mCallback, String mAuthId) {
        ArrayList<User> arrayList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.NOTIFICATION_POST_NODE).child(mAuthId);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        User model = snapshot.getValue(User.class);
                        arrayList.add(model);
                        mCallback.onCallback(arrayList);
                    }
                } else {
                    mCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "" + databaseError.getMessage());
                mCallback.onCallback(null);
            }
        });
    }
    public interface NotificationCallback {
        void onCallback(ArrayList<User> users);
    }

    public void getAllFeedback(final GetFeedbackCallback mCallback, String mAuthId) {
        ArrayList<Feedback> arrayList = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.FEEDBACK_POST_NODE).child(mAuthId);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        Feedback model = snapshot.getValue(Feedback.class);
                        arrayList.add(model);
                        mCallback.onCallback(arrayList);
                    }
                } else {
                    mCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "" + databaseError.getMessage());
                mCallback.onCallback(null);
            }
        });
    }
    public interface GetFeedbackCallback {
        void onCallback(ArrayList<Feedback> arrayList);
    }

    //===============================================| Store
    public void storeUserData(final UserStoreCallback mCallback, User mUser) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.USER_NODE);
        //String mDbId = mDatabaseRef.push().getKey() : Get database UUID after inserting data
        mDatabaseRef.child(mUser.getUserAuthId()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mCallback.onCallback("success");
                    Log.d(TAG, "Data inserted successfully");
                } else {
                    mCallback.onCallback("failure");
                    Log.e(TAG, "" + task.getException().getMessage());
                }
            }
        });
    }
    public interface UserStoreCallback {
        void onCallback(String result);
    }

    public void storePostAd(final StorePostAdCallback mCallback, PostAd postAd) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.USER_POST_NODE);
        //String mDbId = mDatabaseRef.push().getKey() : Get database UUID after inserting data
        mDatabaseRef.child(postAd.getOwnerAuthId()).setValue(postAd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mCallback.onCallback("success");
                    Log.d(TAG, "Data inserted successfully");
                } else {
                    mCallback.onCallback("failure");
                    Log.e(TAG, "" + task.getException().getMessage());
                }
            }
        });
    }
    public interface StorePostAdCallback {
        void onCallback(String result);
    }

    public void storeNotification(final StoreNotificationCallback mCallback, String mOwnerAuthId, User mUser) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.NOTIFICATION_POST_NODE).child(mOwnerAuthId);
        //String mDbId = mDatabaseRef.push().getKey() : Get database UUID after inserting data
        mDatabaseRef.child(mUser.getUserAuthId()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mCallback.onCallback("success");
                    Log.d(TAG, "Data inserted successfully");
                } else {
                    mCallback.onCallback("failure");
                    Log.e(TAG, "" + task.getException().getMessage());
                }
            }
        });
    }
    public interface StoreNotificationCallback {
        void onCallback(String result);
    }

    public void storeFeedback(final StoreFeedbackCallback mCallback, Feedback model) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.FEEDBACK_POST_NODE).child(model.getOwnerAuthId());
        //String mDbId = mDatabaseRef.push().getKey() : Get database UUID after inserting data
        mDatabaseRef.child(model.getUserAuthId()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mCallback.onCallback("success");
                    Log.d(TAG, "Data inserted successfully");
                } else {
                    mCallback.onCallback("failure");
                    Log.e(TAG, "" + task.getException().getMessage());
                }
            }
        });
    }
    public interface StoreFeedbackCallback {
        void onCallback(String result);
    }

    //===============================================| Save image to firebase storage
    public void uploadImageToStorage(final ImageStorageCallback mCallback, Uri uri, String mDirectory, String mAuthId) {
        final StorageReference filePath = FirebaseStorage.getInstance().getReference(mDirectory + "/" + mAuthId + ".jpg");
        if (uri != null) {
            filePath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Log.d(TAG, "Image stored successfully");
                    onSuccessImageStorage(new FirebaseRepository.ImageStorageCallback() {
                        @Override
                        public void onCallback(String result) {
                            if (result != null) {
                                mCallback.onCallback(result);
                            }
                        }
                    }, uri, filePath);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    onSuccessImageStorage(new FirebaseRepository.ImageStorageCallback() {
                        @Override
                        public void onCallback(String result) {
                            if (result != null) {
                                mCallback.onCallback(result);
                            }
                        }
                    }, uri, filePath);
                    //Log.d(TAG, "Exception: " + e.getMessage());
                }
            });
        }
    }
    private void onSuccessImageStorage(final ImageStorageCallback mCallback, Uri uri, final StorageReference filePath) {
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mCallback.onCallback(uri.toString());
                    }
                });
            }
        });
    }
    public interface ImageStorageCallback {
        void onCallback(String filePath);
    }

    //===============================================| Remove
    public void removeFeedback(final String mAuthId) {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.FEEDBACK_POST_NODE).child(mAuthId);
        mDatabaseRef.removeValue();
    }

    public void removePostAd(final RemovePostAdCallback mCallback, PostAd model) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(ConstantKey.USER_POST_NODE).child(model.getOwnerAuthId());
        //mDatabaseRef.removeValue();
        mDatabaseRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    mCallback.onCallback("success");
                } else {
                    mCallback.onCallback("failure");
                }
            }
        });
    }
    public void removePostAdImages(RemovePostAdCallback mCallback, String mImageUrl) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(mImageUrl);
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mCallback.onCallback("success");
                Log.d(TAG, "Remove successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                mCallback.onCallback("failure");
                Log.e(TAG, "" + exception.getMessage());
            }
        });
    }
    public interface RemovePostAdCallback {
        void onCallback(String result);
    }


    /*
    public void removeAllData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("firebase-test").orderByChild("title").equalTo("Apple");
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
    */
}
