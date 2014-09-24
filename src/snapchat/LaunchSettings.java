package snapchat;

import java.util.ArrayList;
import java.util.List;

import login.Login;
import utils.Utils;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchSettings extends UiAutomatorTestCase {
	private static String ID_LOGIN_BUTTON = "com.snapchat.android:id/landing_page_login_button";

	private static String ID_LOGIN_EMAIL = "com.snapchat.android:id/login_username_email";
	private static String ID_LOGIN_PASSWD = "com.snapchat.android:id/login_password";
	private static String ID_LOGIN_LOGIN = "com.snapchat.android:id/login_button";

	private static String ID_SNAP_BUTTON = "com.snapchat.android:id/camera_take_snap_button";
	// private static String ID_SNAP_PICTURE = "com.snapchat.android:id/pager";
	// private static String ID_SNAP_MESSAGE =
	// "com.snapchat.android:id/picture_caption";
	private static String ID_SNAP_SEND = "com.snapchat.android:id/picture_send_pic";

	private static String ID_LIST_RECIPIENT = "com.snapchat.android:id/send_to_list";
	private static String ID_LIST_NAME = "com.snapchat.android:id/name";
	private static String ID_LIST_SEND = "com.snapchat.android:id/send_to_bottom_panel_send_button";

	private static String ID_LIST_BACK = "com.snapchat.android:id/feed_back_button_area";

	protected void performLogin(String username, String password) {
		/* If the page has the login button then perform login */
		if (Utils.hasObject(ID_LOGIN_BUTTON)) {

			/* Click login button */
			assertTrue("Unable to click on the login button",
					Utils.clickAndWaitForNewWindow(ID_LOGIN_BUTTON));

			/* Insert username, password and click login button */
			assertTrue("Email field not available",
					Utils.setText(ID_LOGIN_EMAIL, username));
			assertTrue("Password field not available",
					Utils.setText(ID_LOGIN_PASSWD, password));
			assertTrue("Login button not available",
					Utils.clickAndWaitForNewWindow(ID_LOGIN_LOGIN));
		}
	}

	protected void snapAPicture() {
		snapAPicture("Test", new String[] { "mptcpllnsnd" });
	}

	protected void snapAPicture(String message, String[] recipients) {

		/* Click on the picture button and set the message */
		sleep(1000);
		if (!Utils.click(ID_SNAP_BUTTON)) {
			// in another menu, go back to main screen
			getUiDevice().pressBack();
			assertTrue("Snap button not available", Utils.click(ID_SNAP_BUTTON));
		}
		sleep(1000);
		/*
		 * not used for now, typing does not work as expected.
		 * assertTrue("Unable to display message",
		 * Utils.click(ID_SNAP_PICTURE)); sleep(1000);
		 * 
		 * assertTrue("Unable to set picture message",
		 * Utils.setText(ID_SNAP_MESSAGE, message)); getUiDevice().pressBack();
		 * sleep(1000);
		 */

		assertTrue("Send button not available",
				Utils.clickAndWaitForNewWindow(ID_SNAP_SEND));

		List<UiObject> available = Utils.getElems(ID_LIST_RECIPIENT,
				ID_LIST_NAME);
		assertTrue("Unable to retrieve the recipients", !available.isEmpty());

		List<String> checked = new ArrayList<String>();
		for (UiObject dest : available) {
			for (String recipient : recipients) {
				if (Utils.hasText(dest, recipient)
						&& !checked.contains(recipient)) {
					checked.add(recipient);
					assertTrue("Unable to select recipient",
							Utils.hasTextAndClick(dest, recipient));
				}
			}
		}
		assertTrue("Send button in recipient list not available",
				Utils.click(ID_LIST_SEND));

		/* Return to camera view */
		Utils.clickAndWaitForNewWindow(ID_LIST_BACK);
	}

	public void testDemo() throws UiObjectNotFoundException {
		assertTrue("OOOOOpps",
				Utils.openApp(this, "Snapchat", "com.snapchat.android"));
		performLogin(Login.getUsername(), Login.getPassword());
		// let's spam
		while (true) {
			snapAPicture();
			sleep(5000);
		}
	}

}