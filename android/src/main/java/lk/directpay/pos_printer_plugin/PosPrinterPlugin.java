package lk.directpay.pos_printer_plugin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.pax.dal.ICardReaderHelper;
import com.pax.dal.ICashDrawer;
import com.pax.dal.IDAL;
import com.pax.dal.IDalCommManager;
import com.pax.dal.IDeviceInfo;
import com.pax.dal.IFingerprintReader;
import com.pax.dal.IIDReader;
import com.pax.dal.IIDReaderEx;
import com.pax.dal.IIcc;
import com.pax.dal.IKeyBoard;
import com.pax.dal.IMag;
import com.pax.dal.IPed;
import com.pax.dal.IPedBg;
import com.pax.dal.IPedNp;
import com.pax.dal.IPedTrSys;
import com.pax.dal.IPhoneManager;
import com.pax.dal.IPicc;
import com.pax.dal.IPrinter;
import com.pax.dal.IPuk;
import com.pax.dal.IScanCodec;
import com.pax.dal.IScanner;
import com.pax.dal.IScannerHw;
import com.pax.dal.ISignPad;
import com.pax.dal.ISys;
import com.pax.dal.IWifiProbe;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.entity.EPedType;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.EScannerType;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.dal.pedkeyisolation.IPedKeyIsolation;
import com.pax.neptunelite.api.NeptuneLiteUser;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** PosPrinterPlugin */
public class PosPrinterPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
    Activity activity;
    Context context;
    private IPrinter printer;
    private static IDAL dal;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "pos_printer_plugin");
    channel.setMethodCallHandler(this);
      this.context = flutterPluginBinding.getApplicationContext();

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
      Log.d("printReceipt","method"+call.method);
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } 
    if (call.method.equals("printReceipt")){
        Log.d("printReceipt","method Called");
        String dateTime = call.argument("dateTime");
        String merchantName = call.argument("merchantName");
        String tranId = call.argument("tranId");
        String reference = call.argument("reference");
        String status = call.argument("status");
        String amount=call.argument("amount");
        String qrReference=call.argument("qrReference");

      printSlip(dateTime,merchantName,tranId,status,amount,reference,qrReference);
      result.success("Success");
    }
      if (call.method.equals("printSummary")){
          Log.d("printSummary","method Called");
          String dateTime = call.argument("dateTime");
          String merchantId = call.argument("merchantId");
          String counterId = call.argument("counterId");
          String amount=call.argument("amount");

          printSummary(dateTime,merchantId,counterId,amount);
          result.success("Success");
      }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public void printSlip(final String dateTime, final String merchantName, final String tranId, final String status, final String amount, final String reference,final
  String qrReference)
  {
      new Thread(new Runnable()
      {
          public void run()
          {
              TesterPageComposing testerPageComposing = new TesterPageComposing(context, dateTime,merchantName,tranId,status,amount,reference,qrReference);
              testerPageComposing.run();
              Bitmap bitmap = testerPageComposing.getBitmap();
              try
              {
                  android.util.Log.d("printReceipt", "printSlip dal");
                  dal = NeptuneLiteUser.getInstance().getDal(context);
              }
              catch (Exception e)
              {
                  android.util.Log.d("printReceipt", "printSlip exception "+e);
                  e.printStackTrace();
              }
              android.util.Log.d("printReceipt", "printSlip getPrinter ");
              printer = dal.getPrinter();

              try
              {
                  printer.init();
                  printer.setGray(1);
                  printer.printBitmap(bitmap);
                  printer.start();
              }
              catch (Exception e)
              {
                  android.util.Log.d("printReceipt", "printSlip exception 2"+e);
                  e.printStackTrace();
              }
          }
      }).start();
  }

    public void printSummary(final String dateTime, final String merchantId, final String counterId, final String amount)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                SummaryPageComposing testerPageComposing = new SummaryPageComposing(context, dateTime,merchantId,counterId,amount);
                testerPageComposing.run();
                Bitmap bitmap = testerPageComposing.getBitmap();
                try
                {
                    android.util.Log.d("printSummary", "printSlip dal");
                    dal = NeptuneLiteUser.getInstance().getDal(context);
                }
                catch (Exception e)
                {
                    android.util.Log.d("printSummary", "printSlip exception "+e);
                    e.printStackTrace();
                }
                android.util.Log.d("printSummary", "printSlip getPrinter ");
                printer = dal.getPrinter();

                try
                {
                    printer.init();
                    printer.setGray(1);
                    printer.printBitmap(bitmap);
                    printer.start();
                }
                catch (Exception e)
                {
                    android.util.Log.d("printReceipt", "printSlip exception 2"+e);
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
