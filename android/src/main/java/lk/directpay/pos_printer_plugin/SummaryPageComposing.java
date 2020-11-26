package lk.directpay.pos_printer_plugin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;

import com.pax.gl.page.IPage;
import com.pax.gl.page.PaxGLPage;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryPageComposing extends ATester {
    Context context;
    private PaxGLPage iPaxGLPage;
    private Bitmap bitmap;
    private String dateTime;
    private String merchantId;
    private String counterId;
    private String amount;

    private static final int FONT_BIG = 28;
    private static final int FONT_NORMAL = 26;
    private static final int FONT_BIGEST = 35;

    public SummaryPageComposing(Context context, String dateTime,String merchantId, String counterId,String amount)
    {
        this.context = context;
        this.dateTime=dateTime;
        this.merchantId=merchantId;
        this.counterId=counterId;
        this.amount=amount;
    }

    @Override
    public void run()
    {
        iPaxGLPage = PaxGLPage.getInstance(context);
        IPage page = iPaxGLPage.createPage();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String millisInString  = dateFormat.format(new Date());

        //page.setTypefaceObj(Typeface.createFromAsset(context.getAssets(), "Fangsong.ttf"));
        page.setTypefaceObj(Typeface.createFromAsset(context.getAssets(), "Arimo-Regular.ttf"));
        Log.d("printReceipt", "Fonts"+page.getTypefaceObj().toString());

        page.addLine().addUnit(getImageFromAssetsFile("peoples_logo.jpeg"), IPage.EAlign.CENTER);

//        page.addLine().addUnit("REACH SLIP", FONT_BIGEST, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("- - - - - - - - - - - - - - - - - - - - - - - - - - - -", FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit("", FONT_NORMAL);
        page.addLine().addUnit("Generated At", FONT_NORMAL, IPage.EAlign.LEFT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL).addUnit( dateTime, FONT_NORMAL, IPage.EAlign.RIGHT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Merchant ID", FONT_NORMAL, IPage.EAlign.LEFT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL).addUnit( merchantId, FONT_NORMAL, IPage.EAlign.RIGHT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Counter ID", FONT_NORMAL, IPage.EAlign.LEFT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL).addUnit( counterId, FONT_NORMAL, IPage.EAlign.RIGHT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Total Amount", FONT_NORMAL, IPage.EAlign.LEFT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL).addUnit( amount, FONT_NORMAL, IPage.EAlign.RIGHT, IPage.ILine.IUnit.TEXT_STYLE_NORMAL);


        //page.addLine().addUnit("Branch              : " + branch, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);

        page.addLine().addUnit("- - - - - - - - - - - - - - - - - - - - - - - - - - - -", FONT_NORMAL, IPage.EAlign.CENTER);
        //page.addLine().addUnit("------------------------------------------------", FONT_NORMAL, EAlign.CENTER);

        page.addLine().addUnit("Thank You For Your Payment", FONT_NORMAL, IPage.EAlign.CENTER, IPage.ILine.IUnit.TEXT_STYLE_NORMAL);

        page.addLine().addUnit("\n\n", FONT_NORMAL, IPage.EAlign.CENTER);
        Log.d("printReceipt", "bitmap "+page.getTypefaceObj().toString());
        int width = 520;
        Bitmap bitmap = iPaxGLPage.pageToBitmap(page, width);
        Log.d("printReceipt", "setbitmap "+width);
        setBitmap(bitmap);
    }

    public Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        return this.bitmap;
    }
}