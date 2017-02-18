import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class UI {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("��ս");
		final JButton button = new JButton("��ʼ");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (button.getText().equals("��ʼ")) {
					button.setText("�ر�");
					new Thread(new Runnable() {
						@Override
						public void run() {
							Server server = new Server();
							server.init();
						}
					}).start();

				} else {
					System.exit(0);
				}
			}
		});
		frame.getContentPane().add(button);
		frame.setSize(200, 200);
		ShowHelper.showCenter(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
