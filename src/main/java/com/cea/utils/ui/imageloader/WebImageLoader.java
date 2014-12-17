package com.cea.utils.ui.imageloader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cea.utils.R;
import com.cea.utils.ui.inject.Inject;
import com.cea.utils.ui.inject.InjectView;
import com.cea.utils.web.ManagedErrorsAsyncTask;
import com.cea.utils.web.WebServiceRequest;

/**
 * Created by Carlos on 30/05/2014.
 */
public class WebImageLoader extends FrameLayout {

    @InjectView(tag = "image_loader_image")
    private ImageView image;
    @InjectView(tag = "image_loader_progress")
    private ProgressBar wgtProgressImgLoad;

    public WebImageLoader(Context context) {
        super(context);
        View view = inflate(getContext(), R.layout.component_webimageloader, null);
        addView(view);
        Inject.inject(this, view);
    }

    public WebImageLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(getContext(), R.layout.component_webimageloader, null);
        if(!isInEditMode()) {
            addView(view);
            init(view, context, attrs);
        }
    }

    public WebImageLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(getContext(), R.layout.component_webimageloader, null);
        addView(view);
        init(view, context, attrs);
    }

    private void init(View view, Context context, AttributeSet attrs) {
        Inject.inject(this, view);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.WebImageLoader);
        imgDefaultResource = a.getResourceId(R.styleable.WebImageLoader_default_image, 0);
        errorImage = a.getResourceId(R.styleable.WebImageLoader_error_image, 0);
        if(imgDefaultResource != 0) {
            image.setImageResource(imgDefaultResource);
        }
        image.setAdjustViewBounds(a.getBoolean(R.styleable.WebImageLoader_adjustViewBounds, false));
        ViewGroup.LayoutParams imageParams = image.getLayoutParams();
        if(a.getBoolean(R.styleable.WebImageLoader_fill_width, false)){
            imageParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imageParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }

        image.setLayoutParams(imageParams);
        a.recycle();
    }

    public ViewGroup.LayoutParams getImageLayoutParams() {
        return image.getLayoutParams();
    }

    public void setImageLayoutParams(ViewGroup.LayoutParams imageLayoutParams) {
        this.image.setLayoutParams(imageLayoutParams);
    }

    public void adjustViewBounds(boolean adjust) {
        image.setAdjustViewBounds(adjust);
    }

    class AsyncImageLoader extends ManagedErrorsAsyncTask<ImageRequester, Void, Bitmap> {

        public AsyncImageLoader() {
            super(getContext(), false);
        }

        @Override
        protected void onPreExecute() {
            wgtProgressImgLoad.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(ImageRequester... requester) {
            try {
                Bitmap image = requester[0].request();
                return image;
            }
            catch (Exception ex){
                message = WebServiceRequest.serviceRequestExceptionManager(mContext, ex, false, "");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            wgtProgressImgLoad.setVisibility(View.GONE);
            if(bitmap != null) {
                image.setImageBitmap(bitmap);
            }
            else{
                if(errorImage != 0){
                    image.setImageResource(errorImage);
                }
                else{
                    if(imgDefaultResource != 0) {
                        image.setImageResource(imgDefaultResource);
                    }
                }
            }
        }
    }

    public void loadImage(ImageRequester imageRequester){
        new AsyncImageLoader().execute(imageRequester);
    }

    int imgDefaultResource;
    int errorImage;
    public void setDefaultImage(int imageResourceId){
        imgDefaultResource = imageResourceId;
        this.image.setImageResource(imageResourceId);
    }
}

