package com.shilpy.feelingo.other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shilpysamaddar on 27/01/18.
 */

public class NotelyFilter {
    /**
     * filter the list on the basis of hearted item
     * @param notelyDataModelList list of notes
     * @return list that contains only hearted items
     */
    public List<NotelyDataModel> meetCriteriaHearted(List<NotelyDataModel> notelyDataModelList){
        List<NotelyDataModel> notelyDataHeartedList=new ArrayList<>();
        for (int index=0; index<notelyDataModelList.size(); index++){
            if(notelyDataModelList.get(index).isHearted()) {
                notelyDataHeartedList.add(notelyDataModelList.get(index));
            }
        }
        return notelyDataHeartedList;
    }
    /**
     * filter the list on the basis of favourite item
     * @param notelyDataModelList list of notes
     * @return list that contains only favourite items
     */
    public List<NotelyDataModel> meetCriteriaFavourite(List<NotelyDataModel> notelyDataModelList){
        List<NotelyDataModel> notelyDataFavouriteList=new ArrayList<>();
        for (int index=0; index<notelyDataModelList.size(); index++){
            if(notelyDataModelList.get(index).isFavourite()) {
                notelyDataFavouriteList.add(notelyDataModelList.get(index));
            }
        }
        return notelyDataFavouriteList;
    }

    /**
     * filter the items which are only poem
     * @param notelyDataModelList list of notes
     * @return list that contains only poems
     */
    public List<NotelyDataModel> meetCriteriaPoem(List<NotelyDataModel> notelyDataModelList){
        List<NotelyDataModel> notelyDataPoemList=new ArrayList<>();
        for (int index=0; index<notelyDataModelList.size(); index++){
            if(notelyDataModelList.get(index).getType().equalsIgnoreCase(NotelyDataModel.TYPE_POEM)) {
                notelyDataPoemList.add(notelyDataModelList.get(index));
            }
        }
        return notelyDataPoemList;
    }

    /**
     * filter the items which are only story
     * @param notelyDataModelList list of notes
     * @return list that contains only story
     */
    public List<NotelyDataModel> meetCriteriaStory(List<NotelyDataModel> notelyDataModelList){
        List<NotelyDataModel> notelyDataStoryList=new ArrayList<>();
        for (int index=0; index<notelyDataModelList.size(); index++) {
            if (notelyDataModelList.get(index).getType().equalsIgnoreCase(NotelyDataModel.TYPE_STORY)) {
                notelyDataStoryList.add(notelyDataModelList.get(index));
            }
        }
        return notelyDataStoryList;
    }
}
