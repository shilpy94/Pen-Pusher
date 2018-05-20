package com.shilpy.feelingo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shilpy.feelingo.R;
import com.shilpy.feelingo.other.NotelyDBHelper;
import com.shilpy.feelingo.other.NotelyDataModel;
import com.shilpy.feelingo.other.NotelyUtility;

import java.util.List;

/**
 * This activity is used for both adding and viewing the note
 */

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private MenuItem action_edit,action_undo,action_save;
    private LinearLayout heading,select_type_layout;
    private Switch switch_button;
    private EditText notes_heading_edittext, content_edittext;
    private RelativeLayout add_notes;
    private TextView notes_heading_textview,content_textview,created_at,poemtype_textview,storytype_textview;
    private int row_id=0;
    private boolean isdataUpdated=false;
    private NotelyDataModel notelyDataModel;
    private String content_type=NotelyDataModel.TYPE_POEM;

    /**
     * this activity is used for both add and view a note
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnotesactivity);

        Intent callingIntent = getIntent();
        row_id=callingIntent.getIntExtra(NotelyUtility.ROW_ID, 0);
        initViews();
        setToolbar();
        if(row_id>0) {
            setLayoutForView();
            getNotesById();
        }
    }

    /**
     * initialise views
     */
    private void initViews(){
        toolbar= (Toolbar) findViewById(R.id.tabanim_toolbar);
        heading= (LinearLayout) findViewById(R.id.heading);

        select_type_layout= (LinearLayout) findViewById(R.id.select_type_layout);
        switch_button= (Switch) findViewById(R.id.switch_button);
        notes_heading_edittext = (EditText) findViewById(R.id.notes_heading_edittext);
        notes_heading_textview= (TextView) findViewById(R.id.notes_heading_textview);
        content_edittext = (EditText) findViewById(R.id.content_edittext);
        content_textview= (TextView) findViewById(R.id.content_textview);
        created_at= (TextView) findViewById(R.id.created_at);
        add_notes= (RelativeLayout) findViewById(R.id.add_notes);
        storytype_textview= (TextView) findViewById(R.id.storytype_textview);
        poemtype_textview= (TextView) findViewById(R.id.poemtype_textview);

        add_notes.setOnClickListener(this);

            switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        content_type = NotelyDataModel.TYPE_POEM;
                        poemtype_textview.setTextColor(getResources().getColor(R.color.red_heart));
                        storytype_textview.setTextColor(getResources().getColor(R.color.light_grey));
                        content_edittext.setHint("Enter your poem");
                    } else {
                        content_type = NotelyDataModel.TYPE_STORY;
                        storytype_textview.setTextColor(getResources().getColor(R.color.red_heart));
                        poemtype_textview.setTextColor(getResources().getColor(R.color.light_grey));
                        content_edittext.setHint("Enter your story");
                    }
                }
            });
    }

    /**
     * set toolbar of the activity
     */
    private void setToolbar(){
        try{
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(false);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_menu_item, menu);
            action_edit=menu.findItem(R.id.action_edit);
            action_save=menu.findItem(R.id.action_save);
            action_undo=menu.findItem(R.id.action_undo);
            setLayoutForView();
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home) {
            onBackPressed();
        }else if(item.getItemId()==R.id.action_edit) {
               setEditLayout();
        }else if(item.getItemId()==R.id.action_save){
            updateDatabase();
            setLayoutForView();
            hideKeyboard(content_edittext);
        } else if(item.getItemId()==R.id.action_undo){
            setLayoutForView();
            hideKeyboard(content_edittext);
        }else {
            super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * set the layout in edit mode so that all the editable text is visible
     *  text layout to view is hidden
     */
    private void setEditLayout(){
        select_type_layout.setVisibility(View.VISIBLE);
        notes_heading_edittext.setVisibility(View.VISIBLE);
        notes_heading_textview.setVisibility(View.GONE);
        content_textview.setVisibility(View.GONE);
        content_edittext.setVisibility(View.VISIBLE);
        action_edit.setVisible(false);
        action_save.setVisible(true);
        action_undo.setVisible(true);
        notes_heading_edittext.setText(notelyDataModel.getTitle());
        content_edittext.setText(notelyDataModel.getContent());
    }

    /**
     * set the layout in view mode so that all the editable text is not visible
     * if row_id is 0 then user is adding a new note
     * if row_id is greater than 0 then user is either viewing or editing an existing note
     */
    private void setLayoutForView(){
        if(row_id>0) {
            select_type_layout.setVisibility(View.GONE);
            add_notes.setVisibility(View.GONE);
            notes_heading_edittext.setVisibility(View.GONE);
            content_edittext.setVisibility(View.GONE);
            content_textview.setVisibility(View.VISIBLE);
            notes_heading_textview.setVisibility(View.VISIBLE);
            if(action_edit!=null) {
                action_edit.setVisible(true);
            }
            if(action_save!=null) {
                action_save.setVisible(false);
            }
            if(action_undo!=null) {
                action_undo.setVisible(false);
            }
        }
    }

    /**
     * get notes row from database on the basis of row_id
     */
    private void getNotesById(){
        notelyDataModel= NotelyDBHelper.getInstance(AddNotesActivity.this).getNotesById(row_id);
        gotNotelyData(notelyDataModel);
    }

    /**
     * set the data in view got from database
     */
    private void gotNotelyData(NotelyDataModel notelyDataModel){
        if(notelyDataModel!=null){
            if(notelyDataModel.getType().equalsIgnoreCase(NotelyDataModel.TYPE_STORY)){
                switch_button.setChecked(false);
            }
            notes_heading_textview.setText(notelyDataModel.getTitle());
            NotelyUtility notelyUtility=new NotelyUtility();
            created_at.setText("Last updated: " +notelyUtility.getDate(notelyDataModel.getCreatedAt()));
            content_textview.setText(notelyDataModel.getContent());
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_notes:
                if(notes_heading_edittext.getText()!=null &&
                        (!notes_heading_edittext.getText().toString().isEmpty())
                        &&((content_edittext.getText()!=null)
                        && (!content_edittext.getText().toString().isEmpty()))) {
                addNotesToSqlite();
            } else {
                    Toast.makeText(AddNotesActivity.this,"Title Or Content Can Not Be Empty",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isdataUpdated){
            Intent intent=getIntent();
            intent.putExtra(NotelyUtility.IS_DATA_UPDATED,isdataUpdated);
            setResult(NotelyUtility.ACTIVITY_REQUEST_CODE,intent);
            this.finish();
        }
        super.onBackPressed();
    }

//    =================================================================================
    /**
     * this method is used to add a new note in databse
     */
    private void addNotesToSqlite(){
        NotelyDataModel notelyDataModel=new NotelyDataModel();
        notelyDataModel.setHearted(false);
        notelyDataModel.setFavourite(false);
        notelyDataModel.setTitle(notes_heading_edittext.getText().toString());
        notelyDataModel.setType(content_type);
        notelyDataModel.setContent(content_edittext.getText().toString());
        notelyDataModel.setCreatedAt(""+System.currentTimeMillis());
        int rowId= NotelyDBHelper.getInstance(AddNotesActivity.this).addNotes(notelyDataModel);
        isdataUpdated=true;
        onBackPressed();
    }

    /**
     * this method is used to update the note in database when user edits and save
     */
    private void updateDatabase(){
        isdataUpdated=true;
        notelyDataModel.setId(notelyDataModel.getId());
        notelyDataModel.setHearted(notelyDataModel.isHearted());
        notelyDataModel.setFavourite(notelyDataModel.isFavourite());
        notelyDataModel.setTitle(notes_heading_edittext.getText().toString());
        notelyDataModel.setType(content_type);
        notelyDataModel.setContent(content_edittext.getText().toString());
        notelyDataModel.setCreatedAt(""+System.currentTimeMillis());
        NotelyDBHelper.getInstance(AddNotesActivity.this).updateNotes(notelyDataModel);
        getNotesById();
    }

    private void getAllNotes() {
        List<NotelyDataModel> notelyDataModels = NotelyDBHelper.getInstance(AddNotesActivity.this).getNotes();

        for (NotelyDataModel cn : notelyDataModels) {
            String log = "Id: " + cn.getId() + " ,Title: " + cn.getTitle() + " ,Content: " + cn.getContent();
            // Writing Data to log
            Log.e("Name: ", log);
        }
    }

    /**
     * hide keyboard from view
     * @param v view in which keyboard is visible
     */
    public void hideKeyboard(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
