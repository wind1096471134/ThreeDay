package com.android.threeday.model.addTask;

import android.test.ActivityInstrumentationTestCase2;

import com.android.threeday.activity.addTaskActivity.AddTaskActivity;

import java.util.ArrayList;

/**
 * Created by user on 2014/11/27.
 */
public class AddTaskLabelModelTest extends ActivityInstrumentationTestCase2<AddTaskActivity> {
    private AddTaskLabelModel mAddTaskLabelModel;

    public AddTaskLabelModelTest( ){
        super(AddTaskActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.mAddTaskLabelModel = new AddTaskLabelModel(getActivity());
    }

    public void testInstanceNotNull( ) throws Exception{
        assertNotNull(this.mAddTaskLabelModel);
    }

    public void testGetLabels( ) throws Exception{
        ArrayList<Label> arrayList = this.mAddTaskLabelModel.getLabels();
        assertNotNull(arrayList);
    }

    public void testAddLabel( ) throws Exception{
        assertTrue(this.mAddTaskLabelModel.addLabel("wind"));
        assertTrue(this.mAddTaskLabelModel.getLabels().get(0).getLabelName().equals("wind"));
    }

    public void testDeleteLabel( ) throws Exception{
        boolean result = this.mAddTaskLabelModel.addLabel("wind");
        assertTrue(result);
        Label label = this.mAddTaskLabelModel.getLabels().get(0);
        result = this.mAddTaskLabelModel.deleteLabel(0);
        assertTrue(result);
        assertTrue(!this.mAddTaskLabelModel.getLabels().contains(label));
    }

}
