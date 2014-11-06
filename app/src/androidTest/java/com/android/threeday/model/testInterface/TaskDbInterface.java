package com.android.threeday.model.testInterface;

/**
 * Created by user on 2014/11/1.
 */
public interface TaskDbInterface{
    public void testGetTask( ) throws Exception;
    public void testAddTask( ) throws Exception;
    public void testDeleteTask( ) throws Exception;
    public void testSetRemain( ) throws Exception;
    public void testSetRemainTime( ) throws Exception;
    public void testFillTasks( ) throws Exception;
    public void testSetDone( ) throws Exception;
    public void testSetDoneTime( ) throws Exception;
    public void testSetEvaluation( ) throws Exception;
    public void testUpdateTask( ) throws Exception;
}
