package com.ooyala.sample.players;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.ooyala.android.OoyalaPlayer;
import com.ooyala.android.PlayerDomain;
import com.ooyala.android.configuration.Options;
import com.ooyala.sample.R;

import com.ooyala.android.skin.OoyalaSkinLayout;
import com.ooyala.android.skin.OoyalaSkinLayoutController;
import com.ooyala.android.skin.configuration.SkinOptions;
import org.json.JSONObject;


/**
 * This activity illustrates how you can play basic playback video using the Skin SDK
 * you can also play Ooyala and VAST advertisements programmatically
 * through the SDK
 *
 */
public class OoyalaSkinPlayerActivity extends AbstractHookActivity {

	protected OoyalaSkinLayoutController playerLayoutController;
	private OoyalaSkinLayout skinLayout;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_skin_simple_layout);
		completePlayerSetup(asked);
	}

	@Override
	void completePlayerSetup(boolean asked) {
		// Get the SkinLayout from our layout xml
		skinLayout = (OoyalaSkinLayout)findViewById(R.id.ooyalaSkin);

		// Create the OoyalaPlayer, with some built-in UI disabled
		PlayerDomain domain1 = new PlayerDomain(domain);
		Options options = new Options.Builder().setShowNativeLearnMoreButton(false).setShowPromoImage(false).setUseExoPlayer(true).build();
		player = new OoyalaPlayer(pcode, domain1, options);

		//Create the SkinOptions, and setup React
		JSONObject overrides = createSkinOverrides();
		SkinOptions skinOptions = new SkinOptions.Builder().setSkinOverrides(overrides).build();
		playerLayoutController = new OoyalaSkinLayoutController(getApplication(), skinLayout, player, skinOptions);
		//Add observer to listen to fullscreen open and close events
		playerLayoutController.addObserver(this);
		player.addObserver(this);

		if (player.setEmbedCode(embedCode)) {
			//Uncomment for autoplay
			//player.play();
		}
		else {
			Log.e(TAG, "Asset Failure");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (playerLayoutController != null) {
			playerLayoutController.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	/** Start DefaultHardwareBackBtnHandler **/
	@Override
	public void invokeDefaultOnBackPressed() {
		super.onBackPressed();
	}
	/** End DefaultHardwareBackBtnHandler **/

	/** Start Activity methods for Skin **/
	@Override
	protected void onPause() {
		super.onPause();
		if (playerLayoutController != null) {
			playerLayoutController.onPause();
		}
		Log.d(TAG, "Player Activity Stopped");
		if (player != null) {
			player.suspend();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (playerLayoutController != null) {
			playerLayoutController.onResume( this, this );
		}
		Log.d(TAG, "Player Activity Restarted");
		if (player != null) {
			player.resume();
		}
	}

	@Override
	public void onBackPressed() {
		if (playerLayoutController != null) {
			playerLayoutController.onBackPressed();
		} else {
			super.onBackPressed();
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (playerLayoutController != null) {
			playerLayoutController.onDestroy();
		}
	}
	/** End Activity methods for Skin **/

	/**
	 * Create skin overrides to show up in the skin.
	 * Default commented. Uncomment to show changes to the start screen.
	 * @return the overrides to apply to the skin.json in the assets folder
	 */
	private JSONObject createSkinOverrides() {
		JSONObject overrides = new JSONObject();
//    JSONObject startScreenOverrides = new JSONObject();
//    JSONObject playIconStyleOverrides = new JSONObject();
//    try {
//      playIconStyleOverrides.put("color", "red");
//      startScreenOverrides.put("playButtonPosition", "bottomLeft");
//      startScreenOverrides.put("playIconStyle", playIconStyleOverrides);
//      overrides.put("startScreen", startScreenOverrides);
//    } catch (Exception e) {
//      Log.e(TAG, "Exception Thrown", e);
//    }
		return overrides;
	}

}
