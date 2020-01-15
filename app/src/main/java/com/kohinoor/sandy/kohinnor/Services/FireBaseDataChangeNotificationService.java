package com.kohinoor.sandy.kohinnor.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.CustomClasses.Constants;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyNotificationManager;
import com.kohinoor.sandy.kohinnor.Model.ArticleData;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.PushNotification;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FireBaseDataChangeNotificationService extends Service {

    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;
    List<String> Subjects;
    List<String> Courses;
    public FireBaseDataChangeNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("PushNotification/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PushNotification mPushNotification = dataSnapshot.getValue(PushNotification.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(mPushNotification.getHeadingnoty(),mPushNotification.getBodynoty(),mPushNotification.getNotesTypenoty(),mPushNotification.getMaterialTypenoty(),mPushNotification.getSubItemnoty(),mPushNotification.getKey() );
                mDatabaseReference.removeValue();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Notification For All Important Course wise Articles
      /*  mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/अभ्यासक्रमानुसार/पोलीस उपनिरीक्षक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/अभ्यासक्रमानुसार/विक्रीकर निरीक्षक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/अभ्यासक्रमानुसार/सहाय्यक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/अभ्यासक्रमानुसार/राज्य सेवा/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/अभ्यासक्रमानुसार/महाराष्ट्र कृषी सेवा/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/अभ्यासक्रमानुसार/पोलीस भरती/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Push Notification for all Subject wise Important articles
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/मराठी/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/English/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/इतिहास/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/भुगोल/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/राज्यशास्त्र/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/सामान्य विज्ञान/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/गणित/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/बुधिमता/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial/महत्वाचे लेख/विषयानुसार/संगनक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ArticleData mArticleData = dataSnapshot.getValue(ArticleData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.ImportantArticles), mArticleData.getmArticleTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //Push Notification for all Subject wise PDF Notes
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/मराठी/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/English/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/इतिहास/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/भुगोल/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/राज्यशास्त्र/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/सामान्य विज्ञान/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/गणित/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/बुधिमता/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/विषयानुसार/संगनक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Push Notification for all Course wise PDF Notes
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/अभ्यासक्रमानुसार/पोलीस उपनिरीक्षक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/अभ्यासक्रमानुसार/विक्रीकर निरीक्षक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/अभ्यासक्रमानुसार/सहाय्यक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/अभ्यासक्रमानुसार/राज्य सेवा/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/अभ्यासक्रमानुसार/महाराष्ट्र कृषी सेवा/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/PDF Notes/अभ्यासक्रमानुसार/पोलिस भरती/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF नोट्स", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Push Notification for all Course wise PDF Quetion Set
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/अभ्यासक्रमानुसार/पोलीस उपनिरीक्षक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/अभ्यासक्रमानुसार/विक्रीकर निरीक्षक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/अभ्यासक्रमानुसार/राज्य सेवा/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/अभ्यासक्रमानुसार/महाराष्ट्र कृषी सेवा/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/अभ्यासक्रमानुसार/पोलिस भरती/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/अभ्यासक्रमानुसार/सहाय्यक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Push Notification for all Subject wise PDF Quetion Set
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/मराठी/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/English/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/इतिहास/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/भुगोल/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/राज्यशास्त्र/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/सामान्य विज्ञान/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/गणित/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/बुधिमता/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Downloads/प्रश्नसंच/विषयानुसार/संगनक/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StudyMData mStudyMData = dataSnapshot.getValue(StudyMData.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification("नवीन PDF प्रश्नसंच", mStudyMData.getmTitle());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("NewsData/");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // Toast.makeText(getApplicationContext(), "ChildAdded", Toast.LENGTH_SHORT).show();
                DataModel post = dataSnapshot.getValue(DataModel.class);
                MyNotificationManager.getInstance(getApplicationContext()).displayNotification(getString(R.string.CurrentAffairs), post.getmHeading());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
*/
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
            }
}
