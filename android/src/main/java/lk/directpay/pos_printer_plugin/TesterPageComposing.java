package lk.directpay.pos_printer_plugin;
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

public class TesterPageComposing extends ATester
{
    Context context;
    String body;
    private PaxGLPage iPaxGLPage;
    private Bitmap bitmap;
    private String dateTime;
    private String merchantName;
    private String reference;
    private String status;
    private String amount;

    private static final int FONT_BIG = 28;
    private static final int FONT_NORMAL = 26;
    private static final int FONT_BIGEST = 35;

    public TesterPageComposing(Context context, String dateTime,String merchantName, String reference,String status,String amount)
    {
        this.context = context;
        this.body = body;
        this.dateTime=dateTime;
        this.merchantName=merchantName;
        this.reference=reference;
        this.status=status;
        this.amount=amount;
    }

    @Override
    public void run()
    {
        iPaxGLPage = PaxGLPage.getInstance(context);
        IPage page = iPaxGLPage.createPage();

        //page.setTypefaceObj(Typeface.createFromAsset(context.getAssets(), "Fangsong.ttf"));
        page.setTypefaceObj(Typeface.createFromAsset(context.getAssets(), "Arimo-Regular.ttf"));
        Log.d("Fonts", page.getTypefaceObj().toString());

        page.addLine().addUnit(getImageFromAssetsFile("peoples_pay_logo.png"), EAlign.CENTER);

//        page.addLine().addUnit("REACH SLIP", FONT_BIGEST, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("- - - - - - - - - - - - - - - - - - - - - - - - - - - -", FONT_NORMAL, EAlign.CENTER);
        page.addLine().addUnit("", FONT_NORMAL);
        page.addLine().addUnit("Date                            : " + dateTime, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Transaction Status              : " + status, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Paid To                         : " + merchantName, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Amount                          : " + amount, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit("Transaction ID                  : " + reference, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);


        //page.addLine().addUnit("Branch              : " + branch, FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);
        page.addLine().addUnit(page.createUnit().setText(body).setAlign(EAlign.CENTER).setFontSize(FONT_BIG).setTextStyle(IUnit.TEXT_STYLE_NORMAL).setWeight(1.0f));

        page.addLine().addUnit("- - - - - - - - - - - - - - - - - - - - - - - - - - - -", FONT_NORMAL, EAlign.CENTER);
        //page.addLine().addUnit("------------------------------------------------", FONT_NORMAL, EAlign.CENTER);

        page.addLine().addUnit("Thank You For Banking With Us", FONT_NORMAL, EAlign.CENTER, IUnit.TEXT_STYLE_NORMAL);

        page.addLine().addUnit("\n\n", FONT_NORMAL, EAlign.CENTER);

        int width = 520;
        Bitmap bitmap = iPaxGLPage.pageToBitmap(page, width);

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