import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    /**
     * Enumeration of task types.
     */
    public enum TaskType {
        READ,
        WRITE,
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        TaskGroup taskGroupOne = new TaskGroup(UUID.randomUUID());
        TaskGroup taskGroupTwo = new TaskGroup(UUID.randomUUID());
        TaskGroup taskGroupThree = new TaskGroup(UUID.randomUUID());

        Callable<Boolean> writeCallable = () -> {
//            System.out.println("Executing Write Task: " + Thread.currentThread().getName());
            Thread.sleep(1000);
            return true;
        };
        Callable<String> readCallable = () -> {
//            System.out.println("Executing Read Task: " + Thread.currentThread().getName());
            Thread.sleep(1000);
            return "Read task return from " + Thread.currentThread().getName();
        };

        Task<String> readTask = new Task<>(UUID.randomUUID(), taskGroupOne, TaskType.READ, readCallable);
        Task<String> readTask2 = new Task<>(UUID.randomUUID(), taskGroupOne, TaskType.READ, readCallable);
        Task<String> readTask3 = new Task<>(UUID.randomUUID(), taskGroupTwo, TaskType.READ, readCallable);

        Task<String> readTask4 = new Task<>(UUID.randomUUID(), taskGroupThree, TaskType.READ, readCallable);
        Task<String> readTask5 = new Task<>(UUID.randomUUID(), taskGroupThree, TaskType.READ, readCallable);
        Task<String> readTask6 = new Task<>(UUID.randomUUID(), taskGroupThree, TaskType.READ, readCallable);

        Task<Boolean> writeTask = new Task<>(UUID.randomUUID(), taskGroupOne, TaskType.WRITE, writeCallable);
        Task<Boolean> writeTask2 = new Task<>(UUID.randomUUID(), taskGroupOne, TaskType.WRITE, writeCallable);
        Task<Boolean> writeTask3 = new Task<>(UUID.randomUUID(), taskGroupTwo, TaskType.WRITE, writeCallable);
        Task<Boolean> writeTask4 = new Task<>(UUID.randomUUID(), taskGroupTwo, TaskType.WRITE, writeCallable);
        Task<Boolean> writeTask5 = new Task<>(UUID.randomUUID(), taskGroupTwo, TaskType.WRITE, writeCallable);


        int numCores = Runtime.getRuntime().availableProcessors();
        final ThreadPoolExecutor executorPool = (ThreadPoolExecutor)
                Executors.newFixedThreadPool(numCores);
        ;
        TaskExecutor executor = new TaskExecutorImpl(executorPool);

        Future<String> futureResult1 = executor.submitTask(readTask);
        Future<String> futureResult2 = executor.submitTask(readTask2);
        Future<String> futureResult3 = executor.submitTask(readTask3);
        Future<String> futureResult4 = executor.submitTask(readTask4);
        Future<String> futureResult5 = executor.submitTask(readTask5);
        Future<String> futureResult6 = executor.submitTask(readTask6);

        Future<Boolean> futureResult7 = executor.submitTask(writeTask);
        Future<Boolean> futureResult8 = executor.submitTask(writeTask3);
        Future<Boolean> futureResult9 = executor.submitTask(writeTask2);
        Future<Boolean> futureResult10 = executor.submitTask(writeTask4);
        Future<Boolean> futureResult11 = executor.submitTask(writeTask5);

        System.out.println("****************** result *****************");
        System.out.println(futureResult1.get());
        System.out.println(futureResult2.get());
        System.out.println(futureResult3.get());
        System.out.println(futureResult4.get());
        System.out.println(futureResult5.get());
        System.out.println(futureResult6.get());
        System.out.println(futureResult7.get());
        System.out.println(futureResult8.get());
        System.out.println(futureResult9.get());
        System.out.println(futureResult10.get());
        System.out.println(futureResult11.get());

        executorPool.shutdown();

    }
//
//    public interface TaskExecutor {
//        /**
//         * Submit new task to be queued and executed.
//         *
//         * @param task Task to be executed by the executor. Must not be null.
//         * @return Future for the task asynchronous computation result.
//         */
//        <T> Future<T> submitTask(Task<T> task);
//    }

//    /**
//     * Representation of computation to be performed by the {@link TaskExecutor}.
//     *
//     * @param taskUUID Unique task identifier.
//     * @param taskGroup Task group.
//     * @param taskType Task type.
//     * @param taskAction Callable representing task computation and returning the result.
//     * @param <T> Task computation result value type.
//     */
//    public record Task<T>(
//    UUID taskUUID,
//    TaskGroup taskGroup,
//    TaskType taskType,
//    Callable<T> taskAction
//  ) {
//    public Task {
//            if (taskUUID == null || taskGroup == null || taskType == null || taskAction == null) {
//                throw new IllegalArgumentException("All parameters must not be null");
//            }
//        }
//    }

//    /**
//     * Task group.
//     *
//     * @param groupUUID Unique group identifier.
//     */
//    public record TaskGroup(
//            UUID groupUUID
//    ) {
//    public TaskGroup {
//            if (groupUUID == null) {
//                throw new IllegalArgumentException("All parameters must not be null");
//            }
//        }
//    }

}
