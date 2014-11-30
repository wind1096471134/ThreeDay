package com.android.threeday.model.threeDay;

/**
 * Created by user on 2014/10/29.
 */
public class TaskItem implements Cloneable{
    private long mId;
    private int mEvaluation;
    private int mDayType;
    private String mInformation;
    private String mTime;
    private String mRemainTime;
    private String mDoneTime;
    private boolean mToRemain;
    private boolean mDone;

    public TaskItem(int id, int dayType, String time){
        this.mId = id;
        this.mTime = time;
        this.mDayType = dayType;
    }

    public long getId( ){
        return this.mId;
    }

    public void setId(long id){
        this.mId = id;
    }

    public String getTime( ){
        return this.mTime;
    }

    public void setTime(String time){
        this.mTime = time;
    }

    public int getDayType( ){
        return this.mDayType;
    }

    public void setDayType(int dayType){
        this.mDayType = dayType;
    }

    public void setInformation(String information){
        this.mInformation = information;
    }

    public String getInformation( ){
        return this.mInformation;
    }

    public void setRemain(boolean remain){
        this.mToRemain = remain;
        if(remain == false){
            this.mRemainTime = null;
        }
    }

    public boolean getRemain( ){
        return this.mToRemain;
    }

    public void setRemainTime(String time){
        this.mRemainTime = time;
    }

    public String getRemainTime( ){
        return this.mRemainTime;
    }

    public void setDone(boolean done){
        this.mDone = done;
        if(done == false){
            this.mDoneTime = null;
        }
    }

    public boolean getDone( ){
        return this.mDone;
    }

    public void setDoneTime(String time){
        this.mDoneTime = time;
    }

    public String getDoneTime( ){
        return this.mDoneTime;
    }

    public void setEvaluation(int evaluation){
        this.mEvaluation = evaluation;
    }

    public int getEvaluation( ){
        return this.mEvaluation;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
