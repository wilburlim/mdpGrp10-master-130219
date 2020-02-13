package mdp.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import mdp.map.Map;

public class SocketCommunicator {
    
    private static final String _HOST_NAME = "192.168.56.1";
    private static final int _PORT = 5000;
    
    private static final int _MAX_MSG_LENGTH = 30;
    private static final String _ENCODING = "UTF-8";
    private static final int _ENCODING_BYTE_SIZE = 8;
    
    private static final int _CONNECTION_TIMEOUT = 20;
    
    private SocketChannel _socketChannel;
    ByteBuffer _inBuffer;
    ByteBuffer _outBuffer;
    
    public SocketCommunicator(Runnable callback) {
        long startTime = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (System.currentTimeMillis() - startTime >= _CONNECTION_TIMEOUT * 1000) {
                        timer.cancel();
                    }
                    System.out.println("Connecting...");
                    _socketChannel = SocketChannel.open();
                    _socketChannel.connect(new InetSocketAddress(_HOST_NAME, _PORT));
                    _inBuffer = ByteBuffer.allocate(_ENCODING_BYTE_SIZE * _MAX_MSG_LENGTH);
                    _outBuffer = ByteBuffer.allocate(_ENCODING_BYTE_SIZE * _MAX_MSG_LENGTH);
                    timer.cancel();
                    System.out.println("Connected!");
                   
                    callback.run();
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.println("Trying again in 5 second...");
                }
            }
        }, 0, 5000);
    }
    
    public String read() throws IOException {
        String str = "                    ";
        _inBuffer.clear();
        _inBuffer.asCharBuffer().put(str);
        _socketChannel.read(_inBuffer);
        String inputStr = new String(_inBuffer.array(), _ENCODING).trim();
        return inputStr;
    }
    
    public void echo(String message) throws IOException {
        System.out.println("Sending out message: " + message);
        _outBuffer.clear();
        _outBuffer = ByteBuffer.wrap(message.getBytes(Charset.forName(_ENCODING)));
        _socketChannel.write(_outBuffer);
    }
    
    public void closeSocketChannel() throws IOException {
        _socketChannel.close();
    }
}
