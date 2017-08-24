package ${groupId}.server;

public interface Server {
	
	void start() throws Exception;

    void restart() throws Exception;

    void shutdown();
}
