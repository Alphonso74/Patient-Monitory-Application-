package psu.ajm6684.patientmonitoringsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import psu.ajm6684.patientmonitoringsystem.ui.login.LoginActivity;
import android.os.Bundle;

public class neonatalPatientFeed extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patients = db.collection("patients3");
    //private CollectionReference bluePatients = db.collection("patients").whereEqualTo("triageTag","Blue");

    private PatientAdapter patientAdapter;

    DatabaseReference reference;

    ArrayList<String> arrayList;
    ArrayList<Object> patientList;
    DocumentReference patientItem;
    RecyclerView recyclerView;

    EditText e1;
    ImageButton l1;
    ArrayAdapter<String> adapter;
    String name;
    EditText ee;
    Button profileButton;
    Long heartRate;
    Long bodyTemp;
    String department;
    TextView feedTitle;
    String feedType = "regular";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neonatal_patient_feed);
        feedTitle = (TextView) findViewById(R.id.textView32);
        final Intent intent = getIntent();
        feedType = intent.getStringExtra("Type");
        e1 = (EditText) findViewById(R.id.editText);
        l1 = (ImageButton) findViewById(R.id.button_message);
        arrayList = new ArrayList<>();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        //         l1.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference().getRoot();
        //request_username();
        profileButton = (Button) findViewById(R.id.profileButton);
        feedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                TextView title = new TextView(neonatalPatientFeed.this);
                title.setText("Filter This Feed");
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.rgb(0, 153, 204));
                title.setTextSize(23);
                builder.setMessage("Choose the filter type");
                builder.setCustomTitle(title);


                builder.setPositiveButton("Tag", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder tagDialog = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        TextView title = new TextView(neonatalPatientFeed.this);
                        title.setText("Choose Tag Color");
                        title.setPadding(10, 10, 10, 10);
                        title.setGravity(Gravity.CENTER);
                        title.setTextColor(Color.rgb(0, 153, 204));
                        title.setTextSize(23);
                        tagDialog.setMessage("Make Selection:");
                        tagDialog.setCustomTitle(title);

                        tagDialog.setPositiveButton("Black", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(neonatalPatientFeed.this,neonatalPatientFeed.class);
                                intent.putExtra("Type","Black");
                                startActivity(intent);


                            }
                        });

                        tagDialog.setNeutralButton("Red", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(neonatalPatientFeed.this,neonatalPatientFeed.class);
                                intent.putExtra("Type","Red");
                                startActivity(intent);


                            }
                        });

                        tagDialog.setNegativeButton("Yellow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(neonatalPatientFeed.this,neonatalPatientFeed.class);
                                intent.putExtra("Type","Yellow");
                                startActivity(intent);


                            }
                        });


                        AlertDialog dialogTag =  tagDialog.show();


                        TextView messageView = (TextView) dialogTag.findViewById(android.R.id.message);
                        messageView.setGravity(Gravity.CENTER);

                    }
                });

                builder.setNeutralButton("Heart Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(neonatalPatientFeed.this,neonatalPatientFeed.class);
                        intent.putExtra("Type","Heart Rate");
                        startActivity(intent);


                    }
                });

                builder.setNegativeButton("Critical Status", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(neonatalPatientFeed.this,neonatalPatientFeed.class);
                        intent.putExtra("Type","Critical Status");
                        startActivity(intent);

                    }
                });

                AlertDialog dialog =  builder.show();


                TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
                messageView.setGravity(Gravity.CENTER);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(neonatalPatientFeed.this,UProfile.class);
                startActivity(intent);
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();


                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());

                }

                arrayList.clear();
                arrayList.addAll(set);
                adapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(neonatalPatientFeed.this, "No network connectivity", Toast.LENGTH_SHORT).show();
            }
        });


        //ImageButton imageButton = (ImageButton) findViewById(R.id.button_message);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v) {

                        Intent intent = new Intent(neonatalPatientFeed.this, Chatroom.class);
                        startActivity(intent);
                Toast.makeText(neonatalPatientFeed.this, "Welcome to the Hospital Chatroom! The Staff can communicate with and keep track of the patients and any emergencies.  ", Toast.LENGTH_LONG).show();

                    }
                });


        if(feedType == null) {
            setUpView();
        }
        else if(feedType.equals("Critical Status")){

            filterStatusView();

        }
        else if(feedType.equals("Heart Rate")){

            heartRateView();
        }
        else if(feedType.equals("Black")){

            blackTagView();
        } else if(feedType.equals("Red")){

            redTagView();
        } else if(feedType.equals("Yellow")){

            yellowTagView();
        }

//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        Query query = patients;
//        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();

//        patientAdapter = new PatientAdapter(options);

//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(patientAdapter);


        Button menuButton = (Button) findViewById(R.id.button_menu);




        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder menuDialog = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                menuDialog.setTitle("User Options");
                menuDialog.setMessage("What would you like to do?");
                menuDialog.setCancelable(true);

                menuDialog.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(neonatalPatientFeed.this, " Goodbye", Toast.LENGTH_SHORT).show();


                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();


                    }


                });

                menuDialog.setPositiveButton("More Options", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder menuDialog23 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        menuDialog23.setTitle("More Options");
                        menuDialog23.setMessage("What would you like to do?");
                        menuDialog23.setCancelable(true);

                        menuDialog23.setPositiveButton("Add Patient", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(neonatalPatientFeed.this, addPatient.class));

                            }
                        });

                        menuDialog23.setNegativeButton("Post-Op feed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(neonatalPatientFeed.this, postOpPatientFeed.class));

                            }
                        });

                        menuDialog23.setNeutralButton("General Care feed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(neonatalPatientFeed.this, patientFeed.class));

                            }
                        });





                        menuDialog23.show();
                    }

                });


                        menuDialog.setNeutralButton("Data Feed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(neonatalPatientFeed.this, dataSimFeed.class);
                                        startActivity(intent);
                                    }
                                });

//                        menuDialog1.setPositiveButton("Filter By Tag", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                //setUpViewByTag();
//
////                                Query patients1 = patients.whereEqualTo("triageTag","Blue");
////
////                                setUpView(patients1);
//                            }
//                        });



//                        menuDialog1.setNegativeButton("Filter By Heart Rate", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                //setUpViewByHeartRate();
//
//
//                            }
//                        });

//                        menuDialog1.setNeutralButton("Default List", new DialogInterface.OnClickListener() {
//                            @RequiresApi(api = Build.VERSION_CODES.N)
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                setUpView();
//                            }
//                        });






                menuDialog.show();

            }


        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpView() {
//            Query query = patients.whereEqualTo("triageTag","Blue");
        Query query = patients.whereEqualTo("department","Neonatal");


        //patientList.addAll(patients.whereEqualTo("department","Neonatal").get().toString());

        //       ///  [START fs_collection_group_query]
//        db.collection("patients3").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//        @Override
//        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//            // [START_EXCLUDE]
//            for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
//                patientList.add(snap.getData());
//
//
//
//            }
//            // [END_EXCLUDE]
//        }
//    });

        // Query nurses = patients.document().;


//        ImageView face = (ImageView) findViewById(R.id.facey) ;

        // face.setVisibility(View.VISIBLE);



        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        options.getSnapshots().sort(Note.By_Ascending);

        patientAdapter = new PatientAdapter(options);



        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(patientAdapter);





        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {

            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                alertDlg.setTitle("Patient Options");
                alertDlg.setMessage("What would you like to do?");
                alertDlg.setCancelable(true);

                alertDlg.setPositiveButton("Delete This Patient", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        alertDlg2.setTitle("Delete this patient");
                        alertDlg2.setMessage("Are you sure?");
                        alertDlg2.setCancelable(true);

                        alertDlg2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                patientAdapter.deletePatient(position);

                            }


                        });

                        alertDlg2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        alertDlg2.show();
                    }


                });

//                alertDlg.setNegativeButton("Add Nurse", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        final Dialog dialog1 = new Dialog(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
//                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        dialog1.setCancelable(false);
//                        dialog1.setContentView(R.layout.addnursedialog);

                       // final EditText et = dialog1.findViewById(R.id.et);

                        //String id = patientAdapter.getItemId(position);

                        //Button btnok = (Button) dialog1.findViewById(R.id.btnok);
//                        btnok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//
//                                String nurse =null;
//
//
//
//                                //Note patient = documentSnapshot.toObject(Note.class);
//
//
//                                final DocumentReference patientItem = db.collection("patients3").document(documentSnapshot.getId());
//
//                                //update
//
//                                if(nurse.isEmpty()){
//
//                                    Toast toast = Toast.makeText(getApplicationContext(),"Please Enter the name of the Nurse",Toast.LENGTH_SHORT);
//                                    toast.show();
//                                }
//                                else {
//
//                                    patientItem.update("activeNurse", nurse);
//
//                                    Toast.makeText(neonatalPatientFeed.this, " Nurse Added", Toast.LENGTH_SHORT).show();
//
//
//
//                                    dialog1.dismiss();
//                                }
//                            }
                        //});

                       // Button btncn = (Button) dialog1.findViewById(R.id.btncn);
//                        btncn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog1.dismiss();
//                            }
//                        });

//                        dialog1.show();
//
//                    }
//                });

                alertDlg.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });





                alertDlg.show();




            }


            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Note note = documentSnapshot.toObject(Note.class);
                assert note != null;
                String pname = note.getPatientName();
                String pdescription = note.getDescription();
                String pheight = note.getHeight();
                String pweight = String.valueOf(note.getWeight());
                String prhr = String.valueOf(note.getrHeartRate());
                String id = documentSnapshot.getId();
                Integer position1 = position;
                String bodyTemp = String.valueOf(note.getBodyTempature());
                String activeNurse = note.getActiveNurse();
                String medications = note.getMedications();
                String surgicalHistory = note.getSurgicaHistory();
                String standingO = note.getStandingOrder();
                String department = note.getDepartment();


                DocumentSnapshot DocSnap = documentSnapshot;


                DocumentReference ref = documentSnapshot.getReference();

                Intent intent = new Intent(neonatalPatientFeed.this,patientProfile.class);
                intent.putExtra("Patient Name",pname);
                intent.putExtra("Patient Description",pdescription);
                intent.putExtra("Patient Height",pheight);
                intent.putExtra("Patient Weight",pweight);
                intent.putExtra("Patient Resting Heart Rate",prhr);
                intent.putExtra("Patient ID",id);
                intent.putExtra("position",position1);
                intent.putExtra("bodyTemp",bodyTemp);
                intent.putExtra("nurse",activeNurse);
                intent.putExtra("medications",medications);
                intent.putExtra("surgicalH",surgicalHistory);
                intent.putExtra("standingO",standingO);
                //intent.putExtra("DocSnap", (Serializable) documentSnapshot);
                //intent.putExtra("Firebse Reference", (Serializable) ref);
                intent.putExtra("FeedType", "PatientFeed");

                startActivity(intent);






            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterStatusView() {
//            Query query = patients.whereEqualTo("triageTag","Blue");
        Query query = patients.whereLessThan("rHeartRate",50).whereEqualTo("department","Neonatal");







        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        options.getSnapshots().sort(Note.By_Ascending);

        patientAdapter = new PatientAdapter(options);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(patientAdapter);





        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {

            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                alertDlg.setTitle("Patient Options");
                alertDlg.setMessage("What would you like to do?");
                alertDlg.setCancelable(true);
                patientItem = db.collection("patients3").document(documentSnapshot.getId());

                alertDlg.setPositiveButton("Delete This Patient", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        alertDlg2.setTitle("Delete this patient");
                        alertDlg2.setMessage("Are you sure?");
                        alertDlg2.setCancelable(true);

                        alertDlg2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                patientAdapter.deletePatient(position);

                            }


                        });

                        alertDlg2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        alertDlg2.show();
                    }


                });

                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDlg.show();






            }


            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Note note = documentSnapshot.toObject(Note.class);
                assert note != null;
                String pname = note.getPatientName();
                String pdescription = note.getDescription();
                String pheight = note.getHeight();
                String pweight = String.valueOf(note.getWeight());
                String prhr = String.valueOf(note.getrHeartRate());
                String id = documentSnapshot.getId();
                Integer position1 = position;
                String bodyTemp = String.valueOf(note.getBodyTempature());
                String activeNurse = note.getActiveNurse();
                String medications = note.getMedications();
                String surgicalHistory = note.getSurgicaHistory();
                String standingO = note.getStandingOrder();


                DocumentSnapshot DocSnap = documentSnapshot;


                DocumentReference ref = documentSnapshot.getReference();

                Intent intent = new Intent(neonatalPatientFeed.this,patientProfile.class);
                intent.putExtra("Patient Name",pname);
                intent.putExtra("Patient Description",pdescription);
                intent.putExtra("Patient Height",pheight);
                intent.putExtra("Patient Weight",pweight);
                intent.putExtra("Patient Resting Heart Rate",prhr);
                intent.putExtra("Patient ID",id);
                intent.putExtra("position",position1);
                intent.putExtra("bodyTemp",bodyTemp);
                intent.putExtra("nurse",activeNurse);
                intent.putExtra("medications",medications);
                intent.putExtra("surgicalH",surgicalHistory);
                intent.putExtra("standingO",standingO);
                intent.putExtra("activeNurse",activeNurse);

                //intent.putExtra("DocSnap", (Serializable) documentSnapshot);
                //intent.putExtra("Firebse Reference", (Serializable) ref);

                startActivity(intent);






            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void heartRateView() {
//            Query query = patients.whereEqualTo("triageTag","Blue");
        Query query = patients.whereEqualTo("department","Neonatal").orderBy("rHeartRate", Query.Direction.ASCENDING);







        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        options.getSnapshots().sort(Note.By_Ascending);

        patientAdapter = new PatientAdapter(options);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(patientAdapter);





        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {

            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                alertDlg.setTitle("Patient Options");
                alertDlg.setMessage("What would you like to do?");
                alertDlg.setCancelable(true);
                patientItem = db.collection("patients3").document(documentSnapshot.getId());

                alertDlg.setPositiveButton("Delete This Patient", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        alertDlg2.setTitle("Delete this patient");
                        alertDlg2.setMessage("Are you sure?");
                        alertDlg2.setCancelable(true);

                        alertDlg2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                patientAdapter.deletePatient(position);

                            }


                        });

                        alertDlg2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        alertDlg2.show();
                    }


                });

                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDlg.show();






            }


            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Note note = documentSnapshot.toObject(Note.class);
                assert note != null;
                String pname = note.getPatientName();
                String pdescription = note.getDescription();
                String pheight = note.getHeight();
                String pweight = String.valueOf(note.getWeight());
                String prhr = String.valueOf(note.getrHeartRate());
                String id = documentSnapshot.getId();
                Integer position1 = position;
                String bodyTemp = String.valueOf(note.getBodyTempature());
                String activeNurse = note.getActiveNurse();
                String medications = note.getMedications();
                String surgicalHistory = note.getSurgicaHistory();
                String standingO = note.getStandingOrder();


                DocumentSnapshot DocSnap = documentSnapshot;


                DocumentReference ref = documentSnapshot.getReference();

                Intent intent = new Intent(neonatalPatientFeed.this,patientProfile.class);
                intent.putExtra("Patient Name",pname);
                intent.putExtra("Patient Description",pdescription);
                intent.putExtra("Patient Height",pheight);
                intent.putExtra("Patient Weight",pweight);
                intent.putExtra("Patient Resting Heart Rate",prhr);
                intent.putExtra("Patient ID",id);
                intent.putExtra("position",position1);
                intent.putExtra("bodyTemp",bodyTemp);
                intent.putExtra("nurse",activeNurse);
                intent.putExtra("medications",medications);
                intent.putExtra("surgicalH",surgicalHistory);
                intent.putExtra("standingO",standingO);
                intent.putExtra("activeNurse",activeNurse);

                //intent.putExtra("DocSnap", (Serializable) documentSnapshot);
                //intent.putExtra("Firebse Reference", (Serializable) ref);

                startActivity(intent);






            }
        });




    }

    @Override
    protected void onStart(){
        super.onStart();
        patientAdapter.startListening();


        patients.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();


                    String patientName1 = documentSnapshot.get("patientName").toString();
                    String id = documentSnapshot.getId();
                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();
                    heartRate = (Long) documentSnapshot.get("rHeartRate");
                    bodyTemp = (Long) documentSnapshot.get("bodyTempature");
                    department = (String) documentSnapshot.get("department");






                    switch (dc.getType()) {
                        case ADDED:
                            //Toast.makeText(patientFeed.this, patientName1, Toast.LENGTH_SHORT).show();


                            break;
                        case MODIFIED:

                            //Toast.makeText(dataSimFeed.this,"Patient: " + patientName1 + ", New Body Temperature - " + documentSnapshot.get("rHeartRate") + ", New Heart Rate - " + documentSnapshot.get("bodyTempature"), Toast.LENGTH_SHORT).show();
                            if(department.equals("Neonatal")) {

                                if (heartRate < 50 || bodyTemp < 90 || heartRate > 100 || bodyTemp > 110) {

                                    AlertDialog.Builder menuDialog = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                                    menuDialog.setTitle("PATIENT IN CRITICAL CONDITION");
                                    menuDialog.setMessage(patientName1 + " Is in critical condition!!!!");
                                    menuDialog.setCancelable(true);

                                    menuDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

//                                    menuDialog.setNegativeButton("Notify", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    });


                                    menuDialog.show();


                                }
                            }


                            break;
                        case REMOVED:
                            //Toast.makeText(dataSimFeed.this, "Removed - " + patientName1, Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void blackTagView() {
//            Query query = patients.whereEqualTo("triageTag","Blue");
        Query query = patients.whereEqualTo("department","Neonatal").whereEqualTo("triageTag","Black");







        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        options.getSnapshots().sort(Note.By_Ascending);

        patientAdapter = new PatientAdapter(options);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(patientAdapter);





        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {

            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                alertDlg.setTitle("Patient Options");
                alertDlg.setMessage("What would you like to do?");
                alertDlg.setCancelable(true);
                patientItem = db.collection("patients3").document(documentSnapshot.getId());

                alertDlg.setPositiveButton("Delete This Patient", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        alertDlg2.setTitle("Delete this patient");
                        alertDlg2.setMessage("Are you sure?");
                        alertDlg2.setCancelable(true);

                        alertDlg2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                patientAdapter.deletePatient(position);

                            }


                        });

                        alertDlg2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        alertDlg2.show();
                    }


                });

                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDlg.show();






            }


            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Note note = documentSnapshot.toObject(Note.class);
                assert note != null;
                String pname = note.getPatientName();
                String pdescription = note.getDescription();
                String pheight = note.getHeight();
                String pweight = String.valueOf(note.getWeight());
                String prhr = String.valueOf(note.getrHeartRate());
                String id = documentSnapshot.getId();
                Integer position1 = position;
                String bodyTemp = String.valueOf(note.getBodyTempature());
                String activeNurse = note.getActiveNurse();
                String medications = note.getMedications();
                String surgicalHistory = note.getSurgicaHistory();
                String standingO = note.getStandingOrder();


                DocumentSnapshot DocSnap = documentSnapshot;


                DocumentReference ref = documentSnapshot.getReference();

                Intent intent = new Intent(neonatalPatientFeed.this,patientProfile.class);
                intent.putExtra("Patient Name",pname);
                intent.putExtra("Patient Description",pdescription);
                intent.putExtra("Patient Height",pheight);
                intent.putExtra("Patient Weight",pweight);
                intent.putExtra("Patient Resting Heart Rate",prhr);
                intent.putExtra("Patient ID",id);
                intent.putExtra("position",position1);
                intent.putExtra("bodyTemp",bodyTemp);
                intent.putExtra("nurse",activeNurse);
                intent.putExtra("medications",medications);
                intent.putExtra("surgicalH",surgicalHistory);
                intent.putExtra("standingO",standingO);
                intent.putExtra("activeNurse",activeNurse);

                //intent.putExtra("DocSnap", (Serializable) documentSnapshot);
                //intent.putExtra("Firebse Reference", (Serializable) ref);

                startActivity(intent);






            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void redTagView() {
//            Query query = patients.whereEqualTo("triageTag","Blue");
        Query query = patients.whereEqualTo("department","Neonatal").whereEqualTo("triageTag","Red");







        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        options.getSnapshots().sort(Note.By_Ascending);

        patientAdapter = new PatientAdapter(options);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(patientAdapter);





        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {

            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                alertDlg.setTitle("Patient Options");
                alertDlg.setMessage("What would you like to do?");
                alertDlg.setCancelable(true);
                patientItem = db.collection("patients3").document(documentSnapshot.getId());

                alertDlg.setPositiveButton("Delete This Patient", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        alertDlg2.setTitle("Delete this patient");
                        alertDlg2.setMessage("Are you sure?");
                        alertDlg2.setCancelable(true);

                        alertDlg2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                patientAdapter.deletePatient(position);

                            }


                        });

                        alertDlg2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        alertDlg2.show();
                    }


                });

                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDlg.show();






            }


            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Note note = documentSnapshot.toObject(Note.class);
                assert note != null;
                String pname = note.getPatientName();
                String pdescription = note.getDescription();
                String pheight = note.getHeight();
                String pweight = String.valueOf(note.getWeight());
                String prhr = String.valueOf(note.getrHeartRate());
                String id = documentSnapshot.getId();
                Integer position1 = position;
                String bodyTemp = String.valueOf(note.getBodyTempature());
                String activeNurse = note.getActiveNurse();
                String medications = note.getMedications();
                String surgicalHistory = note.getSurgicaHistory();
                String standingO = note.getStandingOrder();


                DocumentSnapshot DocSnap = documentSnapshot;


                DocumentReference ref = documentSnapshot.getReference();

                Intent intent = new Intent(neonatalPatientFeed.this,patientProfile.class);
                intent.putExtra("Patient Name",pname);
                intent.putExtra("Patient Description",pdescription);
                intent.putExtra("Patient Height",pheight);
                intent.putExtra("Patient Weight",pweight);
                intent.putExtra("Patient Resting Heart Rate",prhr);
                intent.putExtra("Patient ID",id);
                intent.putExtra("position",position1);
                intent.putExtra("bodyTemp",bodyTemp);
                intent.putExtra("nurse",activeNurse);
                intent.putExtra("medications",medications);
                intent.putExtra("surgicalH",surgicalHistory);
                intent.putExtra("standingO",standingO);
                intent.putExtra("activeNurse",activeNurse);

                //intent.putExtra("DocSnap", (Serializable) documentSnapshot);
                //intent.putExtra("Firebse Reference", (Serializable) ref);

                startActivity(intent);






            }
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void yellowTagView() {
//            Query query = patients.whereEqualTo("triageTag","Blue");
        Query query = patients.whereEqualTo("department","Neonatal").whereEqualTo("triageTag","Yellow");







        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        options.getSnapshots().sort(Note.By_Ascending);

        patientAdapter = new PatientAdapter(options);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(patientAdapter);





        patientAdapter.setOnItemClickListener(new PatientAdapter.onItemClickListener() {

            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                alertDlg.setTitle("Patient Options");
                alertDlg.setMessage("What would you like to do?");
                alertDlg.setCancelable(true);
                patientItem = db.collection("patients3").document(documentSnapshot.getId());

                alertDlg.setPositiveButton("Delete This Patient", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        AlertDialog.Builder alertDlg2 = new AlertDialog.Builder(new ContextThemeWrapper(neonatalPatientFeed.this, android.R.style.Theme_Holo_Light));
                        alertDlg2.setTitle("Delete this patient");
                        alertDlg2.setMessage("Are you sure?");
                        alertDlg2.setCancelable(true);

                        alertDlg2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                patientAdapter.deletePatient(position);

                            }


                        });

                        alertDlg2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        alertDlg2.show();
                    }


                });

                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDlg.show();






            }


            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Note note = documentSnapshot.toObject(Note.class);
                assert note != null;
                String pname = note.getPatientName();
                String pdescription = note.getDescription();
                String pheight = note.getHeight();
                String pweight = String.valueOf(note.getWeight());
                String prhr = String.valueOf(note.getrHeartRate());
                String id = documentSnapshot.getId();
                Integer position1 = position;
                String bodyTemp = String.valueOf(note.getBodyTempature());
                String activeNurse = note.getActiveNurse();
                String medications = note.getMedications();
                String surgicalHistory = note.getSurgicaHistory();
                String standingO = note.getStandingOrder();


                DocumentSnapshot DocSnap = documentSnapshot;


                DocumentReference ref = documentSnapshot.getReference();

                Intent intent = new Intent(neonatalPatientFeed.this,patientProfile.class);
                intent.putExtra("Patient Name",pname);
                intent.putExtra("Patient Description",pdescription);
                intent.putExtra("Patient Height",pheight);
                intent.putExtra("Patient Weight",pweight);
                intent.putExtra("Patient Resting Heart Rate",prhr);
                intent.putExtra("Patient ID",id);
                intent.putExtra("position",position1);
                intent.putExtra("bodyTemp",bodyTemp);
                intent.putExtra("nurse",activeNurse);
                intent.putExtra("medications",medications);
                intent.putExtra("surgicalH",surgicalHistory);
                intent.putExtra("standingO",standingO);
                intent.putExtra("activeNurse",activeNurse);

                //intent.putExtra("DocSnap", (Serializable) documentSnapshot);
                //intent.putExtra("Firebse Reference", (Serializable) ref);

                startActivity(intent);






            }
        });




    }

    @Override
    protected void onStop(){
        super.onStop();
        patientAdapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_patient_menu, menu);
        return true;
    }



    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
