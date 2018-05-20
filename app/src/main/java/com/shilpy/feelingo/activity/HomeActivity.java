package com.shilpy.feelingo.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shilpy.feelingo.R;
import com.shilpy.feelingo.adapter.NotesRowAdapter;
import com.shilpy.feelingo.other.NotelyDBHelper;
import com.shilpy.feelingo.other.NotelyDataModel;
import com.shilpy.feelingo.other.NotelyFilter;
import com.shilpy.feelingo.other.NotelyUtility;
import com.shilpy.feelingo.other.RecyclerSwipeToDeleteHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * this is the home activity
 * it displays all the notes created by user
 */

public class HomeActivity extends AppCompatActivity implements RecyclerSwipeToDeleteHelper.RecyclerItemTouchHelperListener,
        View.OnClickListener {

    //    definig variables
    private Toolbar toolbar;
    private LinearLayout drawerlist;
//    private MenuItem action_filter, action_add;
    private RelativeLayout main_anim_layout;
    private TextView nodatattext;
    private FrameLayout drawer,menu;
    private RecyclerView notes_recyclerview;
    private View navigationView;
    private NotesRowAdapter notesRowAdapter;
    private List<NotelyDataModel> notelyDataModelsList;
    private ImageView add_note,green_dot;
    private boolean rowIsUnselected=false;
    private boolean isFilterApplied=false;

    //    ======================================================
//    definig views of navigation view
    private RelativeLayout nav_heading_layout, nav_hearted_layout, nav_favourite_layout, nav_poem_layout,
            nav_story_layout, apply_layout;
    private ImageView nav_heading_image, nav_hearted_image, nav_favourite_image, nav_poem_image, nav_story_image;
    private TextView nav_heading_text, nav_hearted_text, nav_favourite_text, nav_poem_text, nav_story_text;

    private boolean isDrawerOpen = false;
    private RelativeLayout selected_layout;
    private int backPressed=0;

    //    ==========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        setToolbar();
        setNavigationView();
    }

    /**
     * initializing views
     */
    private void initViews() {
        drawer = (FrameLayout) findViewById(R.id.drawer_layout);
        nodatattext = (TextView) findViewById(R.id.nodatattext);
        drawerlist = (LinearLayout) findViewById(R.id.drawerlist);
        toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        notes_recyclerview = (RecyclerView) findViewById(R.id.notes_recyclerview);
        main_anim_layout = (RelativeLayout) findViewById(R.id.main_anim_layout);
        add_note= (ImageView) findViewById(R.id.add_note);
        menu= (FrameLayout) findViewById(R.id.menu);
        green_dot= (ImageView) findViewById(R.id.green_dot);
        getAllNotes();
        slideAnimViewOut();

        menu.setOnClickListener(this);
        add_note.setOnClickListener(this);
    }

    /**
     * initializing navigation views
     */
    private void setNavigationView() {
        navigationView = findViewById(R.id.navigation_layout);

        nav_heading_layout = navigationView.findViewById(R.id.nav_heading_layout);
        nav_heading_text = navigationView.findViewById(R.id.nav_heading_text);
        nav_heading_image = navigationView.findViewById(R.id.nav_heading_image);

        nav_hearted_layout = navigationView.findViewById(R.id.nav_hearted_layout);
        nav_hearted_text = navigationView.findViewById(R.id.nav_hearted_text);
        nav_hearted_image = navigationView.findViewById(R.id.nav_hearted_image);

        nav_favourite_layout = navigationView.findViewById(R.id.nav_favourite_layout);
        nav_favourite_text = navigationView.findViewById(R.id.nav_favourite_text);
        nav_favourite_image = navigationView.findViewById(R.id.nav_favourite_image);

        nav_poem_layout = navigationView.findViewById(R.id.nav_poem_layout);
        nav_poem_text = navigationView.findViewById(R.id.nav_poem_text);
        nav_poem_image = navigationView.findViewById(R.id.nav_poem_image);

        nav_story_layout = navigationView.findViewById(R.id.nav_story_layout);
        nav_story_text = navigationView.findViewById(R.id.nav_story_text);
        nav_story_image = navigationView.findViewById(R.id.nav_story_image);

        apply_layout = (RelativeLayout) findViewById(R.id.apply_layout);

        nav_heading_layout.setOnClickListener(this);
        nav_hearted_layout.setOnClickListener(this);
        nav_favourite_layout.setOnClickListener(this);
        nav_poem_layout.setOnClickListener(this);
        nav_story_layout.setOnClickListener(this);
        apply_layout.setOnClickListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_item, menu);
//        action_filter = menu.findItem(R.id.action_filter);
//        action_add = menu.findItem(R.id.action_add);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_filter:
//                openNavigationView();
//                break;
//            case R.id.action_add:
//                Intent intent = new Intent(HomeActivity.this, AddNotesActivity.class);
//                startActivityForResult(intent, NotelyUtility.ACTIVITY_REQUEST_CODE);
//                break;
//        }
//        return true;
//    }

    /**
     * initializing toolbar
     */
    private void setToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * open navigation view
     */
    private void openNavigationView() {
        if(notelyDataModelsList!=null && notelyDataModelsList.size()>0) {
            if (!isDrawerOpen) {
                isDrawerOpen = true;
//            action_add.setVisible(false);
//            action_filter.setVisible(false);
                menu.setVisibility(View.GONE);
                add_note.setVisibility(View.GONE);
                slideMainActivityOnDrawerOpen();
                slideAnimViewIn();
            }
        } else {
            Toast.makeText(this,"Add some note to apply filter!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * close navigation view
     */
    private void closeNavigationView() {
        if (isDrawerOpen) {
            slideAnimViewOut();
            isDrawerOpen = false;
//            action_add.setVisible(true);
//            action_filter.setVisible(true);
            menu.setVisibility(View.VISIBLE);
            add_note.setVisibility(View.VISIBLE);
            slideMainActivityOnDrawerClose();
        }
    }

    private void slideAnimViewOut(){
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(drawerlist, "translationX", 0, layout_width);
        if(isDrawerOpen) {
            anim.setDuration(300);
        } else {
            anim.setDuration(0);
        }
        anim.start();
    }

    private void slideAnimViewIn(){
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(drawerlist, "translationX", layout_width, 0);
        anim.setDuration(300);
        anim.start();
    }

    /**
     * animation slide activity to the width of navigation view to show navigation view opening with animation
     */
    private void slideMainActivityOnDrawerOpen() {
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(main_anim_layout, "translationX", 0, -layout_width);
        anim.setDuration(300);
        anim.start();
    }

    /**
     * animation slide activity to the width of navigation view to show navigation view closing with animation
     * and to bring the activity back to its original position
     */
    private void slideMainActivityOnDrawerClose() {
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(main_anim_layout, "translationX", -layout_width, 0);
        anim.setDuration(300);
        anim.start();
    }

    @Override
    public void onBackPressed() {
        try {
            if (backPressed < 1) {
                Toast.makeText(this, "Press back again in order to exit", Toast.LENGTH_SHORT).show();
                backPressed++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressed = 0;
                    }
                }, 2000);
                return;
            } else {
                this.finish();
            }
            super.onBackPressed();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * get all the notes from database and set adapter of recycler view
     */
    private void getAllNotes() {
        notelyDataModelsList = NotelyDBHelper.getInstance(HomeActivity.this).getNotes();
        setAdapter(notelyDataModelsList);
    }

    /**
     * method to set the adapter to show rows of recycler view
     *
     * @param notelyDataModelsList is the list of NotelyDataModel class
     */
    private void setAdapter(List<NotelyDataModel> notelyDataModelsList) {
        if (notelyDataModelsList != null && notelyDataModelsList.size()>0) {
            nodatattext.setVisibility(View.GONE);
            Collections.sort(notelyDataModelsList, notelyListSortRev);
            if (notesRowAdapter != null) {
                notesRowAdapter.notifyDataChange(notelyDataModelsList);
            } else {
                notesRowAdapter = new NotesRowAdapter(notelyDataModelsList, HomeActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                notes_recyclerview.setLayoutManager(mLayoutManager);
                notes_recyclerview.setItemAnimator(new DefaultItemAnimator());
                notes_recyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                notes_recyclerview.setAdapter(notesRowAdapter);

                ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerSwipeToDeleteHelper(0, ItemTouchHelper.LEFT, this);
                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(notes_recyclerview);
            }
        } else {
            if (notesRowAdapter != null) {
                notesRowAdapter.notifyDataChange(notelyDataModelsList);
                nodatattext.setVisibility(View.VISIBLE);
                nodatattext.setText("No Data Found!");
            } else {
                nodatattext.setVisibility(View.VISIBLE);
                nodatattext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, AddNotesActivity.class);
                        startActivityForResult(intent, NotelyUtility.ACTIVITY_REQUEST_CODE);
                    }
                });
            }
        }
    }

    /**
     * RecyclerSwipeToDeleteHelper.RecyclerItemTouchHelperListener callback method
     * this method notifies when a element is swiped to delete from the recycler view row
     *
     * @param viewHolder
     * @param direction
     * @param position
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotesRowAdapter.NotelyViewHolder) {

            final NotelyDataModel notelyDataModel = notelyDataModelsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            removeRowFromDatabse(notelyDataModel);
            // remove the item from recycler view
            notelyDataModelsList.size();
            if(notesRowAdapter!=null) {
                notesRowAdapter.removeItem(viewHolder.getAdapterPosition());
            }
            if(notesRowAdapter!=null && notesRowAdapter.getItemCount()==0){
                nodatattext.setVisibility(View.VISIBLE);
                nodatattext.setText("+ Add Your First Note!");
            }
        }
    }

//    ======================================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NotelyUtility.ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                boolean isDataUpdated = data.getBooleanExtra(NotelyUtility.IS_DATA_UPDATED, false);
                if (isDataUpdated) {
                    getAllNotes();
                }
            }
        }
    }

//        ===========================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_note:
                Intent intent = new Intent(HomeActivity.this, AddNotesActivity.class);
                startActivityForResult(intent, NotelyUtility.ACTIVITY_REQUEST_CODE);
                break;
            case R.id.menu:
                openNavigationView();
                break;
            case R.id.nav_heading_layout:
                closeNavigationView();
                if(selected_layout==null && rowIsUnselected){
                    changeMenuItem();
                    isFilterApplied=false;
                }
                if(selected_layout!=null && !isFilterApplied){
                    changeMenuItem();
                    changeSelectedColorToWhite();
                    selected_layout=null;
                }
                if(selected_layout!=null){
//                    action_add.setVisible(false);
                    add_note.setVisibility(View.GONE);
                }
                break;
            case R.id.nav_hearted_layout:
                if(selected_layout!=null && selected_layout.getId()==R.id.nav_hearted_layout){
                    rowIsUnselected=true;
                    changeSelectedColorToWhite();
                    selected_layout=null;
                } else {
                    isFilterApplied=false;
                    changeSelectedColorToWhite();
                    selected_layout = nav_hearted_layout;
                    changeTextColorToGreen(nav_hearted_text);
                    changeImageColorGreen(nav_hearted_image);
                }
                break;
            case R.id.nav_favourite_layout:
                if(selected_layout!=null && selected_layout.getId()==R.id.nav_favourite_layout){
                    rowIsUnselected=true;
                    changeSelectedColorToWhite();
                    selected_layout=null;
                } else {
                    isFilterApplied=false;
                    changeSelectedColorToWhite();
                    selected_layout = nav_favourite_layout;
                    changeTextColorToGreen(nav_favourite_text);
                    changeImageColorGreen(nav_favourite_image);
                }
                break;
            case R.id.nav_poem_layout:
                if(selected_layout!=null && selected_layout.getId()==R.id.nav_poem_layout){
                    rowIsUnselected=true;
                    changeSelectedColorToWhite();
                    selected_layout=null;
                } else {
                    isFilterApplied=false;
                    changeSelectedColorToWhite();
                    selected_layout = nav_poem_layout;
                    changeTextColorToGreen(nav_poem_text);
                    changeImageColorGreen(nav_poem_image);
                }
                break;
            case R.id.nav_story_layout:
                if(selected_layout!=null && selected_layout.getId()==R.id.nav_story_layout){
                    rowIsUnselected=true;
                    changeSelectedColorToWhite();
                    selected_layout=null;
                } else {
                    isFilterApplied=false;
                    changeSelectedColorToWhite();
                    selected_layout = nav_story_layout;
                    changeTextColorToGreen(nav_story_text);
                    changeImageColorGreen(nav_story_image);
                }
                break;
            case R.id.apply_layout:
                closeNavigationView();
                applyFilter();
                break;
        }
    }

    /**
     * channge menu item and get all notes and show in view
     */
    private void changeMenuItem(){
        getAllNotes();
//        action_filter.setIcon(R.drawable.ic_filter);
//        action_add.setVisible(true);
        green_dot.setVisibility(View.GONE);
        add_note.setVisibility(View.VISIBLE);
    }

    /**
     * apply filter on the basis of selected item from navigation view
     */
    private void applyFilter(){
        NotelyFilter notelyFilter=new NotelyFilter();
        List<NotelyDataModel> filterdList;
        if(selected_layout!=null){
//            action_filter.setIcon(R.drawable.dot_green);
//            action_add.setVisible(false);
            green_dot.setVisibility(View.VISIBLE);
            add_note.setVisibility(View.GONE);
            isFilterApplied=true;
            switch (selected_layout.getId()){
                case R.id.nav_hearted_layout:
                    filterdList=notelyFilter.meetCriteriaHearted(notelyDataModelsList);
                    setAdapter(filterdList);
                    break;
                case R.id.nav_favourite_layout:
                    filterdList=notelyFilter.meetCriteriaFavourite(notelyDataModelsList);
                    setAdapter(filterdList);
                    break;
                case R.id.nav_poem_layout:
                    filterdList=notelyFilter.meetCriteriaPoem(notelyDataModelsList);
                    setAdapter(filterdList);
                    break;
                case R.id.nav_story_layout:
                    filterdList=notelyFilter.meetCriteriaStory(notelyDataModelsList);
                    setAdapter(filterdList);
                    break;
            }
        } else {
            if(rowIsUnselected) {
//                action_filter.setIcon(R.drawable.ic_filter);
//                action_add.setVisible(true);
                green_dot.setVisibility(View.GONE);
                add_note.setVisibility(View.VISIBLE);
                isFilterApplied=false;
                getAllNotes();
            }
        }
    }

    /**
     * on cancel of navigation filter set the selected view's items back to white color
     */
    private void changeSelectedColorToWhite(){
        if(selected_layout!=null) {
            switch (selected_layout.getId()) {
                case R.id.nav_hearted_layout:
                    changeTextColorToWhite(nav_hearted_text);
                    changeImageColorWhite(nav_hearted_image);
                    break;
                case R.id.nav_favourite_layout:
                    changeTextColorToWhite(nav_favourite_text);
                    changeImageColorWhite(nav_favourite_image);
                    break;
                case R.id.nav_poem_layout:
                    changeTextColorToWhite(nav_poem_text);
                    changeImageColorWhite(nav_poem_image);
                    break;
                case R.id.nav_story_layout:
                    changeTextColorToWhite(nav_story_text);
                    changeImageColorWhite(nav_story_image);
                    break;
            }
        }
    }

    public void changeTextColorToGreen(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.sea_green));
    }

    public void changeImageColorGreen(ImageView imageView) {
        imageView.setColorFilter(getResources().getColor(R.color.sea_green));
    }

    public void changeTextColorToWhite(TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.white_pressed));
    }

    public void changeImageColorWhite(ImageView imageView) {
        imageView.setColorFilter(getResources().getColor(R.color.white_pressed));
    }

//    ================================================================================

    /**
     * removes the deleted row from databse
     * @param notelyDataModel row's data which is deleted
     */
    private void removeRowFromDatabse(NotelyDataModel notelyDataModel){
        NotelyDBHelper.getInstance(HomeActivity.this).deleteNotes(notelyDataModel);
    }

    /**
     * Sorting in Reverse Order On the basis of updated date
     * last updated will be on top of the list
     */
    public Comparator<NotelyDataModel> notelyListSortRev = new Comparator<NotelyDataModel>() {
        public int compare(NotelyDataModel s1, NotelyDataModel s2) {
            if (Long.parseLong(s1.getCreatedAt()) > Long.parseLong(s2.getCreatedAt())) {
                return -1;
            } else if (Long.parseLong(s1.getCreatedAt()) < Long.parseLong(s2.getCreatedAt())) {
                return 1;
            }
            return 0;
        }
    };
}
