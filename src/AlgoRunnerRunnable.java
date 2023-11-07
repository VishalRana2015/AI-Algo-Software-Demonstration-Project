public interface AlgoRunnerRunnable extends Runnable{
    public boolean takeExit();
    public void setExit(boolean exit);
    int getDelay();
    void setDelay(int delay);

   // public boolean goalTest(Node node, int drow, int dcol);

}
