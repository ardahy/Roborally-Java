package model;

/**
 * timer for game
 * @author Ilinur
 */
public class TimerForGame {
    private int second;
    private int minute;

    public TimerForGame(String currentTime){
        String [] time = currentTime.split(":");
        minute = Integer.parseInt(time[0]);
        second = Integer.parseInt(time[1]);
    }

    /**
     * get current time
     * @return
     */
    public String getCurrentTime(){
        return minute + ":" + second;
    }

    /**
     * seconds countdown
     */
    public void oneSecondPassed(){
        second --;
    }
}
