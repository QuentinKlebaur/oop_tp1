class StopThread extends Thread {

    public StopThread(ApplicationServeur server) {
        _server = server;
    }

    private ApplicationServeur _server;

    public void run() {
        _server.stop();
    }
}