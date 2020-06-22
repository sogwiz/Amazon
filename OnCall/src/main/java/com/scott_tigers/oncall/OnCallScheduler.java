package com.scott_tigers.oncall;

import java.util.Date;
import java.util.List;

public class OnCallScheduler extends Scheduler {

    public OnCallScheduler(Date startDate, Engineer[] engineers) {
	super(startDate, engineers);
    }

    @Override
    protected Schedule createSchedule(List<Engineer> candidateSchedule) {
	return new OnCallSchedule(candidateSchedule, startDate, getRotationSize());
    }

    @Override
    protected int getRotationSize() {
	return Constants.ON_CALLS_PER_DAY;
    }

}
