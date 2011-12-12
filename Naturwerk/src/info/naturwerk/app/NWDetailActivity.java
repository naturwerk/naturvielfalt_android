package info.naturwerk.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NWDetailActivity extends NWBaseActivity implements OnClickListener {
	private static final String TAG = NWDetailActivity.class.getSimpleName();
	private static final int CAMERA_PIC_REQUEST = 4321111;
	private NWObservation obs;
	private Button buttonGetPicture, buttonCreateObservation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		obs = ((NWApplication) super.getApplication()).getObservation();

		((TextView) findViewById(R.id.detailTextFamily)).setText(obs.faunaFamily);
		((TextView) findViewById(R.id.detailTextNameDe)).setText(obs.faunaNameDe);
		((TextView) findViewById(R.id.detailTextName)).setText(obs.faunaFamily);

		buttonCreateObservation = (Button) findViewById(R.id.buttonCreateObservation);
		buttonCreateObservation.setOnClickListener(this);
		buttonGetPicture = (Button) findViewById(R.id.buttonGetFoto);
		buttonGetPicture.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonGetFoto: {
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
			break;
		}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			ImageView image = (ImageView) findViewById(R.id.imageViewFoto);  
			image.setImageBitmap(thumbnail);
			
		}
	}

}
