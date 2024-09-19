import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskExecutorImpl implements TaskExecutor {

    private final ThreadPoolExecutor executor;
    private final ConcurrentHashMap<UUID, Lock> groupMap;
    private final Queue<Runnable> taskQueue;

    TaskExecutorImpl(ThreadPoolExecutor executorPool) {
        this.executor = executorPool;
        taskQueue = new LinkedBlockingQueue<>();
        groupMap = new ConcurrentHashMap<>();
    }


    @Override
    public <T> Future<T> submitTask(Task<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (task == null) {
            throw new IllegalArgumentException("Parameter must not be null");
        }

        Runnable executorTask = () -> {
            Lock lock = groupMap.computeIfAbsent(task.taskGroup().groupUUID(), id -> new ReentrantLock());
            lock.lock();
            try {
                T taskResult = task.taskAction().call();
                System.out.println("Executing task group: " + task.taskGroup().groupUUID() + " : " + task.taskType());
                future.complete(taskResult);
            } catch (Exception e) {
//                throw new RuntimeException(e);
                future.completeExceptionally(e);
            }
            lock.unlock();
        };

        taskQueue.offer(executorTask);
        processTasks();
        return future;
    }

    private void processTasks() {
        while (!taskQueue.isEmpty()) {
            var task = taskQueue.poll();
            executor.submit(task);
        }
    }
}
