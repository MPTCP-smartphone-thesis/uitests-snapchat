package snapchat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import login.Login;
import utils.Utils;
import android.os.RemoteException;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchSettings extends UiAutomatorTestCase {
	private static final String ID_LOGIN_BUTTON = "com.snapchat.android:id/login_and_signup_page_fragment_login_button";

	private static final String ID_LOGIN_EMAIL = "com.snapchat.android:id/login_username_email_field";
	private static final String ID_LOGIN_PASSWD = "com.snapchat.android:id/login_password_field";
	private static final String ID_LOGIN_LOGIN = "com.snapchat.android:id/log_in_button";

	private static final String ID_SNAP_BUTTON = "com.snapchat.android:id/camera_take_snap_button";
	private static final String ID_SNAP_MESSAGE = "com.snapchat.android:id/picture_caption";
	private static final String ID_SNAP_SEND = "com.snapchat.android:id/picture_send_pic";

	private static final String ID_LIST_RECIPIENT = "com.snapchat.android:id/send_to_list";
	private static final String ID_LIST_NAME = "com.snapchat.android:id/name";
	private static final String ID_LIST_SEND = "com.snapchat.android:id/send_to_bottom_panel_send_button";

	private static final String ID_LIST_BACK = "com.snapchat.android:id/feed_back_button_area";

	private static final int NB_SNAPS = 3;

	protected void performLogin(String username, String password) {
		/* If the page has the login button then perform login */
		if (Utils.hasObject(ID_LOGIN_BUTTON)) {
			/* Click login button */
			assertTrue("Unable to click on the login button",
					Utils.click(ID_LOGIN_BUTTON));
			sleep(500);

			/* Insert username, password and click login button */
			assertTrue("Email field not available",
					Utils.setText(ID_LOGIN_EMAIL, username));
			assertTrue("Password field not available",
					Utils.setText(ID_LOGIN_PASSWD, password));
			sleep(1000);
			assertTrue("Login button not available",
					Utils.clickAndWaitForNewWindow(ID_LOGIN_LOGIN));
			sleep(5000);
		}
	}

	protected void snapAPicture() {
		snapAPicture("Test", new String[] { "mptcpllnsnd" });
	}

	protected void snapAPicture(String message, String[] recipients) {

		/* Click on the picture button and set the message */
		sleep(1500);
		int i = 0;
		while (!Utils.click(ID_SNAP_BUTTON)) {
			// in another menu, go back to main screen
			getUiDevice().pressBack();
			sleep(500);
			if (i > 4) {
				Utils.customAssertTrue(this, "Snap button not available",
						Utils.click(ID_SNAP_BUTTON));
				break;
			}
			i++;
		}
		sleep(1000);

		Utils.customAssertTrue(this, "Not able to click on the screen",
				Utils.clickOnTheMiddle(this));
		sleep(750);
		if (!Utils.hasObject(ID_SNAP_MESSAGE)) { // try it twice
			Utils.clickOnTheMiddle(this);
			sleep(750);
		}

		boolean success = false;
		for (int j = 0; !success && j < 3; j++) {
			success = true;
			success &= Utils.setText(ID_SNAP_MESSAGE, new Date().toString());
			if (!success) {
				sleep(500);
				continue;
			}
			getUiDevice().pressBack(); // remove keyboard
			sleep(1000);

			success &= Utils.click(ID_SNAP_SEND);
			sleep(1000);
		}
		Utils.customAssertTrue(this, "Not able to send message", success);

		List<UiObject> available = Utils.getElems(ID_LIST_RECIPIENT,
				ID_LIST_NAME);
		Utils.customAssertTrue(this, "Unable to retrieve the recipients",
				!available.isEmpty());

		List<String> checked = new ArrayList<String>();
		for (UiObject dest : available) {
			for (String recipient : recipients) {
				if (Utils.hasText(dest, recipient)
						&& !checked.contains(recipient)) {
					checked.add(recipient);
					Utils.customAssertTrue(this, "Unable to select recipient",
							Utils.hasTextAndClick(dest, recipient));
				}
			}
		}
		sleep(2000);
		if (!Utils.hasObject(ID_LIST_SEND)) {
			System.err.println("Button not found !?");
			sleep(1000);
			getUiDevice().click(1000, 1700);
		}
 else {
			sleep(1000);
			Utils.customAssertTrue(this,
					"Send button in recipient list not available",
					Utils.click(ID_LIST_SEND));
		}
		sleep(1000);
		// else

		/* Return to camera view */
		Utils.clickAndWaitForNewWindow(ID_LIST_BACK);
	}

	public void testDemo() throws UiObjectNotFoundException {
		assertTrue("OOOOOpps",
				Utils.openApp(this, "Snapchat",
						"com.snapchat.android",
						"com.snapchat.android.LandingPageActivity"));
		performLogin(Login.getUsername(), Login.getPassword());
		try {
			getUiDevice().setOrientationNatural();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// let's spam
		for (int i = 0; i < NB_SNAPS; i++) {
			snapAPicture();
			sleep(5000);
		}
	}

}
