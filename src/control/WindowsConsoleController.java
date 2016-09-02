package control;

import java.io.IOException;

import org.fusesource.jansi.internal.WindowsSupport;
import org.fusesource.jansi.internal.Kernel32.INPUT_RECORD;
import org.fusesource.jansi.internal.Kernel32.KEY_EVENT_RECORD;

import engine.GridData.DIR;

public class WindowsConsoleController implements GameController {

	@Override
	public DIR getDirection() {
		DIR dir = DIR.INVALID;
		try {
			INPUT_RECORD[] events;
			events = WindowsSupport.readConsoleInput(1);
			for (int i = 0; i < events.length; i++) {
				KEY_EVENT_RECORD keyEvent = events[i].keyEvent;
				if (keyEvent.keyDown) {
					if (keyEvent.uchar > 0) {
						continue;
					}
					switch (keyEvent.keyCode) {
					case 0x25: // VK_LEFT
						dir = DIR.LEFT;
						break;
					case 0x26: // VK_UP
						dir = DIR.UP;
						break;
					case 0x27: // VK_RIGHT
						dir = DIR.RIGHT;
						break;
					case 0x28: // VK_DOWN
						dir = DIR.DOWN;
						break;
					default:
						dir = DIR.INVALID;
						break;
					}
				} else {
					
					if (keyEvent.keyCode == 0x12
							&& keyEvent.uchar > 0) {
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dir;
	}

}
