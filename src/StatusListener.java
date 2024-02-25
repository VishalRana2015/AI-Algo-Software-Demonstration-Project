import javax.swing.*;

public interface StatusListener<T> {
    void updateStatus(T obj);
}

class StatusListenerImpl<T> implements StatusListener<T> {
    JLabel label;
    StatusListenerImpl(JLabel button){
        this.label = button;
    }
    @Override
    public void updateStatus(T status) {
        this.label.setText("<html>" + status.toString() + "</html>");
    }
}