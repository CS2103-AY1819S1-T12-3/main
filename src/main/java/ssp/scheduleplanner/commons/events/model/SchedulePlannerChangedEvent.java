package ssp.scheduleplanner.commons.events.model;

import ssp.scheduleplanner.commons.events.BaseEvent;
import ssp.scheduleplanner.model.ReadOnlySchedulePlanner;

/** Indicates the SchedulePlanner in the model has changed*/
public class SchedulePlannerChangedEvent extends BaseEvent {

    public final ReadOnlySchedulePlanner data;

    public SchedulePlannerChangedEvent(ReadOnlySchedulePlanner data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size();
    }
}
