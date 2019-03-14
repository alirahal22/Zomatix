package sample.Model.Database;

public abstract class SQLThread extends Thread{

    public abstract boolean success();
    public abstract void failure();
    public abstract void onFinish();


    @Override
    public void run() {
        super.run();
        if(!success()){
            failure();
        }
        onFinish();
    }

}
