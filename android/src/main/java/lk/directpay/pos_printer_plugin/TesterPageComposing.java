package lk.directpay.pos_printer_plugin;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import com.pax.gl.page.IPage;
import com.pax.gl.page.IPage.EAlign;
import com.pax.gl.page.IPage.ILine.IUnit;
import com.pax.gl.page.PaxGLPage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TesterPageComposing extends ATester
{
    Context context;
    String body;
    private PaxGLPage iPaxGLPage;
    private Bitmap bitmap;
    private String dateTime;
    private String merchantName;
    private String tranId;
    private String status;
    private String amount;
    private String reference;
    private String qrReference;

    private static final int FONT_BIG = 28;
    private static final int FONT_NORMAL = 26;
    private static final int FONT_BIGEST = 35;

    public TesterPageComposing(Context context, String dateTime,String merchantName, String tranId,String status,String amount,String reference,String qrReference)
    {
        this.context = context;
        this.body = body;
        this.dateTime=dateTime;
        this.merchantName=merchantName;
        this.tranId=tranId;
        this.status=status;
        this.amount=amount;
        this.reference=reference;
        this.qrReference=qrReference;
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
        Log.d("printReceipt", "QR reference "+qrReference);

        page.addLine().addUnit(getImageFromAssetsFile("peoples_bank_logo.jpg"), EAlign.CENTER);

//        page.addLine().addUnit("REACH SLIP", FONT_BIGEST, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("- - - - - - - - - - - - - - - - - - - - - - - - - - - -", FONT_NORMAL, EAlign.CENTER);
        page.addLine().addUnit("", FONT_NORMAL);
        page.addLine().addUnit("Generated At", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( millisInString, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Transaction Date", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( dateTime, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Transaction Status", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( status, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Paid To", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( merchantName, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Amount", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( amount, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Transaction ID", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( tranId, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Reference Number", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit( reference, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("QR Reference", FONT_NORMAL, EAlign.LEFT, IUnit.TEXT_STYLE_NORMAL).addUnit(qrReference, FONT_NORMAL, EAlign.RIGHT, IUnit.TEXT_STYLE_NORMAL);





        //page.addLine().addUnit("Branch              : " + branch, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit(page.createUnit().setText(body).setAlign(EAlign.CENTER).setFontSize(FONT_BIG).setTextStyle(IUnit.TEXT_STYLE_NORMAL).setWeight(1.0f));

        page.addLine().addUnit("- - - - - - - - - - - - - - - - - - - - - - - - - - - -", FONT_NORMAL, EAlign.CENTER);
        //page.addLine().addUnit("------------------------------------------------", FONT_NORMAL, EAlign.CENTER);

        page.addLine().addUnit("Thank You For Your Payment", FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);

        page.addLine().addUnit("\n\n", FONT_NORMAL, EAlign.CENTER);
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