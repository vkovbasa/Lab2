package com.company;

public class ArrClass {
    private final int dim;
    private final int threadNum;
    public final int[] arr;

    public ArrClass(int dim, int threadNum) {
        this.dim = dim;
        arr = new int[dim];
        this.threadNum = threadNum;
        for(int i = 0; i < dim; i++){
            arr[i] = i;
        }
    }

    public long partSum(int startIndex, int finishIndex){
        long sum = 0;
        for(int i = startIndex; i < finishIndex; i++){
            sum += arr[i];
        }
        return sum;
    }

    private long sum = 0;

    synchronized private long getSum() {
        while (getThreadCount()<threadNum){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }

    synchronized public void collectSum(long sum){
        this.sum += sum;
    }

    private int threadCount = 0;
    synchronized public void incThreadCount(){
        threadCount++;
        notify();
    }

    private int getThreadCount() {
        return threadCount;
    }

    public long threadSum() {
        ThreadSum[] threadSums = new ThreadSum[threadNum];
        threadSums[0] = new ThreadSum(0, dim / 4, this);
        threadSums[1] = new ThreadSum(dim / 4, dim / 2, this);
        threadSums[2] = new ThreadSum(dim / 2, 3 * dim / 4, this);
        threadSums[3] = new ThreadSum(3 * dim / 4, dim, this);

        for (int i = 0; i < threadNum; i++) {
            threadSums[i].start();
        }

        return getSum();
    }

}
