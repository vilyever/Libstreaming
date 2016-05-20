package com.vilyever.libstreaming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.popupcontroller.popup.PopupDirection;

import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.video.VideoQuality;

/**
 * StreamingSettingActivity
 * Created by vilyever on 2016/5/20.
 * Feature:
 */
public class StreamingSettingActivity extends AppCompatActivity {
    final StreamingSettingActivity self = this;
    
    
    /* Constructors */
    
    
    /* Public Methods */
    
    
    /* Properties */
    private Button ratioButton;
    protected Button getRatioButton() { if (this.ratioButton == null) {this.ratioButton = (Button) findViewById(R.id.ratioButton); } return this.ratioButton; }

    private Button frameRateButton;
    protected Button getFrameRateButton() { if (this.frameRateButton == null) {this.frameRateButton = (Button) findViewById(R.id.frameRateButton); } return this.frameRateButton; }

    private Button bitRateButton;
    protected Button getBitRateButton() { if (this.bitRateButton == null) {this.bitRateButton = (Button) findViewById(R.id.bitRateButton); } return this.bitRateButton; }

    private Button rotationButton;
    protected Button getRotationButton() { if (this.rotationButton == null) { this.rotationButton = (Button) findViewById(R.id.rotationButton); } return this.rotationButton; }

    private Button bootButton;
    protected Button getBootButton() { if (this.bootButton == null) {this.bootButton = (Button) findViewById(R.id.bootButton); } return this.bootButton; }

    private StreamingSettingRatioItemController ratioItemController;
    protected StreamingSettingRatioItemController getRatioItemController() {
        if (this.ratioItemController == null) {
            this.ratioItemController = new StreamingSettingRatioItemController(this);
            this.ratioItemController.setOnPopupDismissListener(new PopupController.OnPopupDismissListener() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                    self.internalUpdateButtonTitle();
                }
            });
        }
        return this.ratioItemController;
    }

    private StreamingSettingFrameRateItemController frameRateItemController;
    protected StreamingSettingFrameRateItemController getFrameRateItemController() {
        if (this.frameRateItemController == null) {
            this.frameRateItemController = new StreamingSettingFrameRateItemController(this);
            this.frameRateItemController.setOnPopupDismissListener(new PopupController.OnPopupDismissListener() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                    self.internalUpdateButtonTitle();
                }
            });
        }
        return this.frameRateItemController;
    }

    private StreamingSettingBitRateItemController bitRateItemController;
    protected StreamingSettingBitRateItemController getBitRateItemController() {
        if (this.bitRateItemController == null) {
            this.bitRateItemController = new StreamingSettingBitRateItemController(this);
            this.bitRateItemController.setOnPopupDismissListener(new PopupController.OnPopupDismissListener() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                    self.internalUpdateButtonTitle();
                }
            });
        }
        return this.bitRateItemController;
    }

    private StreamingSettingRotationItemController rotationItemController;
    protected StreamingSettingRotationItemController getRotationItemController() {
        if (this.rotationItemController == null) {
            this.rotationItemController = new StreamingSettingRotationItemController(this);
            this.rotationItemController.setOnPopupDismissListener(new PopupController.OnPopupDismissListener() {
                @Override
                public void onPopupWindowDismiss(PopupController controller) {
                    self.internalUpdateButtonTitle();
                }
            });
        }
        return this.rotationItemController;
    }
    
    /* Overrides */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.streaming_setting_activity);


        getRatioButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.getRatioItemController().popupInView(self.getWindow().getDecorView(), PopupDirection.Center);
            }
        });
        getFrameRateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.getFrameRateItemController().popupInView(self.getWindow().getDecorView(), PopupDirection.Center);
            }
        });
        getBitRateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.getBitRateItemController().popupInView(self.getWindow().getDecorView(), PopupDirection.Center);
            }
        });
        getBootButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionBuilder.getInstance().setVideoQuality(new VideoQuality(self.getRatioItemController().getRatioWidth(), self.getRatioItemController().getRatioHeight(), self.getFrameRateItemController().getFrameRate(), self.getBitRateItemController().getBitRate()))
                                            .setPreviewOrientation(self.getRotationItemController().getRotation());
                self.startActivity(new Intent(self, StreamingActivity.class));
            }
        });
        internalUpdateButtonTitle();
    }
    
    /* Delegates */
    
    
    /* Private Methods */
    private void internalUpdateButtonTitle() {
        getRatioButton().setText(String.format("分辨率：%d X %d", getRatioItemController().getRatioWidth(), getRatioItemController().getRatioHeight()));
        getFrameRateButton().setText(String.format("帧率：%d", getFrameRateItemController().getFrameRate()));
        getBitRateButton().setText(String.format("比特率：%d", getBitRateItemController().getBitRate()));
        getRotationButton().setText(String.format("画面旋转角度：%d", getRotationItemController().getRotation()));
    }
}