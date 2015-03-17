package com.tlunter.amtrak;

import java.util.List;

/**
 * Created by toddlunter on 12/12/14.
 */
public interface TrainDrawer {
    public void drawLoading();
    public void drawProgress(Integer progress);
    public void drawTrains(List<Train> trains);
}
