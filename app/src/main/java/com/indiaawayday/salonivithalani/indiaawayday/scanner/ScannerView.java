package com.indiaawayday.salonivithalani.indiaawayday.scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.zbar.BarcodeFormat;

/**
 * Created by bhupendrakumar on 7/17/16.
 */
public abstract class ScannerView implements Camera.PreviewCallback {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;
    private ImageScanner mScanner;
    private List<BarcodeFormat> mFormats;
    private Context mContext;

    static {
        System.loadLibrary("iconv");
    }

    public ScannerView(Context context) {
        this.mContext = context;
        mPreview = new CameraPreview(context, this, autoFocusCB);
        mFormats = new ArrayList<BarcodeFormat>();
        mAutoFocusHandler = new Handler();
        setupScanner();
    }

    public View getContentView() {
        return mPreview.getContentView();
    }

    public void addFormat(BarcodeFormat barcodeFormat) {
        mFormats.add(barcodeFormat);
    }

    public List<BarcodeFormat> getFormats() {
        return mFormats.isEmpty() ? BarcodeFormat.ALL_FORMATS : mFormats;
    }

    public void startCamera() {
        mCamera = Camera.open();

        if (mCamera == null) {
            onCameraNotAvailable();
            return;
        }
        mPreview.setCamera(mCamera);
        mPreview.showSurfaceView();
        mPreviewing = true;
    }

    public void setupScanner() {
        this.mScanner = new ImageScanner();
        this.mScanner.setConfig(0, 256, 3);
        this.mScanner.setConfig(0, 257, 3);
        this.mScanner.setConfig(0, 0, 0);
        Iterator i$ = getFormats().iterator();
        while (i$.hasNext()) {
            BarcodeFormat format = (BarcodeFormat) i$.next();
            this.mScanner.setConfig(format.getId(), 0, 1);
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();


        int width = size.width;
        int height = size.height;
        int returnCode;

        Image barcode = new Image(width, height, "Y800");
        barcode.setData(data);
        returnCode = mScanner.scanImage(barcode);
        if (returnCode != 0) {
            stopCamera();
            SymbolSet syms = mScanner.getResults();
            for (Symbol sym : syms) {
                if (!TextUtils.isEmpty(sym.getData())) {
                    Result result = new Result(sym.getType(), sym.getData());
                    handleResult(result);
                    break;
                }
            }
        }
    }

    public void stopCamera() {
        mCamera.cancelAutoFocus();
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mPreviewing = false;
    }

    public void pauseCamera() {
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mPreview.hideSurfaceView();
            mPreviewing = false;
            mCamera = null;
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = mContext.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    public Camera.Size getOptimalPreviewSize() {
        if (this.mCamera == null) {
            return null;
        } else {
            List sizes = this.mCamera.getParameters().getSupportedPreviewSizes();
            int w = getContentView().getWidth();
            int h = getContentView().getHeight();
            if (DisplayUtils.getScreenOrientation(mContext) == 1) {
                int ASPECT_TOLERANCE = h;
                h = w;
                w = ASPECT_TOLERANCE;
            }

            double ASPECT_TOLERANCE1 = 0.1D;
            double targetRatio = (double) w / (double) h;
            if (sizes == null) {
                return null;
            } else {
                Camera.Size optimalSize = null;
                double minDiff = 1.7976931348623157E308D;
                int targetHeight = h;
                Iterator i$ = sizes.iterator();

                Camera.Size size;
                while (i$.hasNext()) {
                    size = (Camera.Size) i$.next();
                    double ratio = (double) size.width / (double) size.height;
                    if (Math.abs(ratio - targetRatio) <= 0.1D && (double) Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = (double) Math.abs(size.height - targetHeight);
                    }
                }

                if (optimalSize == null) {
                    minDiff = 1.7976931348623157E308D;
                    i$ = sizes.iterator();

                    while (i$.hasNext()) {
                        size = (Camera.Size) i$.next();
                        if ((double) Math.abs(size.height - targetHeight) < minDiff) {
                            optimalSize = size;
                            minDiff = (double) Math.abs(size.height - targetHeight);
                        }
                    }
                }

                return optimalSize;
            }
        }
    }

    public abstract void onCameraNotAvailable();

    public abstract void handleResult(Result result);


}
