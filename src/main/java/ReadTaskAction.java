import java.util.concurrent.Callable;

//Task for Task.READ type
public class ReadTaskAction implements Callable<String> {

    @Override
    public String call() throws Exception {


        String str = "Read Result";
        return str;
    }
}
